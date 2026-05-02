# Fintech Service

A production-grade **event-driven fintech microservices ecosystem** built with Spring Boot and Apache Kafka. The system processes financial transfers end-to-end — from business/partner profile management through fee calculation, double-entry bookkeeping, and real-time notifications — using an asynchronous choreography pattern.

---

## Architecture Overview

```
POST /api/transfers
  └─► transfer-service  ──[transfer.initiated]──►  charge-service
                                                        │
                                               [charge.calculated]
                                                        │
                                                        ▼
                                                  ledger-service  ──► profile-service (REST)
                                                        │
                                                [ledger.recorded]
                                                        │
                                                        ▼
                                               transfer-service  (marks COMPLETED)
                                                        │
                                               [transfer.completed]
                                                        │
                                                        ▼
                                             notification-service
```

All Kafka topics use `businessId` as the message key, guaranteeing ordered processing per business across 3 partitions. Every consumer applies **at-least-once + idempotency** guards, achieving effectively exactly-once financial processing.

---

## Services

| Service | Port | Database | Role |
|---|---|---|---|
| `profile-service` | 8080 | PostgreSQL :5432 | Business & Partner CRUD |
| `virtual-account-service` | 8081 | PostgreSQL :5433 | Virtual account generation |
| `charge-service` | 8082 | PostgreSQL :5434 | Tiered fee engine (Kafka consumer + producer) |
| `transfer-service` | 8083 | PostgreSQL :5435 | Transfer lifecycle orchestrator |
| `ledger-service` | 8085 | PostgreSQL :5436 | Double-entry bookkeeping |
| `notification-service` | 8086 | None | Transfer completion notifications |

### Kafka Cluster

3-broker KRaft cluster (no ZooKeeper), replication factor 3, `acks=all` on all producers.

| Broker | Host Port |
|---|---|
| kafka-broker-1 | 9092 |
| kafka-broker-2 | 9094 |
| kafka-broker-3 | 9095 |

### Kafka Topics

| Topic | Producer | Consumer |
|---|---|---|
| `transfer.initiated` | transfer-service | charge-service |
| `charge.calculated` | charge-service | ledger-service |
| `ledger.recorded` | ledger-service | transfer-service |
| `transfer.completed` | transfer-service | notification-service |
| `*.DLT` | all consumers (after 3 retries) | — |

---

## Fee Engine (ChargeTier)

Fees are calculated by `charge-service` using a tiered rate with a floor and a cap:

| Tier | Amount Range | Rate | Min Fee | Max Fee |
|---|---|---|---|---|
| MICRO | ≤ ₦5,000 | 1.5% | ₦10 | ₦75 |
| LOW | ≤ ₦50,000 | 1.0% | ₦25 | ₦500 |
| MID | ≤ ₦1,000,000 | 0.5% | ₦50 | ₦5,000 |
| HIGH | > ₦1,000,000 | 0.25% | ₦1,000 | ₦10,000 |

---

## Ledger Entries (Double-Entry Bookkeeping)

For every transfer, `ledger-service` creates three atomic entries in a single `@Transactional` block:

| Entry Type | Description |
|---|---|
| `CREDIT` | Full transfer amount credited to destination account |
| `CHARGE_DEBIT` | Fee debited from business |
| `PARTNER_PAYOUT` | Fee split distributed to each partner (pro-rata by ratio) |

Partner ratios are fetched from `profile-service` at ledger time. All partner ratios for a business must sum to exactly 100%.

---

## API Reference

### profile-service :8080

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/businesses` | Create a business |
| `GET` | `/api/businesses` | List all businesses |
| `GET` | `/api/businesses/{id}` | Get business by ID |
| `DELETE` | `/api/businesses/{id}` | Delete a business |
| `POST` | `/api/businesses/{id}/partners` | Add a partner to a business |
| `GET` | `/api/businesses/{id}/partners` | List partners for a business |
| `DELETE` | `/api/businesses/partners/{partnerId}` | Delete a partner |

### virtual-account-service :8081

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/virtual-accounts` | Generate a virtual account |

### charge-service :8082

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/charges` | Calculate and save a charge (HTTP) |

### transfer-service :8083

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/transfers` | Initiate a transfer (returns `202 Accepted`) |

### ledger-service :8085

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/ledger/transfer/{transferId}` | Ledger entries for a transfer |
| `GET` | `/api/ledger/business/{businessId}` | Ledger entries for a business |
| `GET` | `/api/ledger/reference/{referenceNumber}` | Ledger entries by reference |

---

## Prerequisites

- **Docker** ≥ 24 and **Docker Compose** ≥ 2.20
- **Java 21** + **Maven** (only needed for local development without Docker)

---

## Running the Full System

```bash
# Clone the repo
git clone https://github.com/Jonathan463/fintech-service.git
cd fintech-service

# Build images and start all services
docker-compose up --build
```

This single command starts:
- 3 Kafka brokers (KRaft mode)
- 5 PostgreSQL databases
- 6 Spring Boot microservices

Wait ~30 seconds for Kafka and the databases to become healthy before the services finish connecting.

### Running a Single Service

Each service has its own `docker-compose.yml` for isolated development:

```bash
# Example: run only ledger-service + its database
cd ledger-service
docker-compose up --build
```

> **Note:** When running in isolation, set `SPRING_KAFKA_BOOTSTRAP_SERVERS` and `SERVICES_PROFILE_SERVICE_BASE_URL` to point to your running infrastructure.

---

## Reliability Guarantees

| Property | Implementation |
|---|---|
| **At-least-once delivery** | `enable.auto.commit=false`, manual ack |
| **Idempotency** | `existsByTransferId` check + unique DB constraint before save |
| **No silent message loss** | `acks=all`, replication factor 3, `min.insync.replicas=2` |
| **Dead-letter handling** | `DeadLetterPublishingRecoverer` with `FixedBackOff(3, 1000ms)` on all consumers |
| **Ordered processing per business** | `businessId` used as Kafka partition key |
| **Atomic ledger writes** | All three ledger entries written in one `@Transactional` block |

---

## Project Structure

```
fintech-service/
├── docker-compose.yml              # Full system orchestration
├── kafka-infra/
│   └── docker-compose.yml          # Kafka cluster only
├── profile-service/                # Business & Partner management
├── virtual-account-service/        # Virtual account generation
├── charge-service/                 # Fee engine
├── transfer-service/               # Transfer orchestration
├── ledger-service/                 # Double-entry bookkeeping
└── notification-service/           # Completion notifications
```

Each service follows the same internal structure:
```
<service>/
├── Dockerfile                      # Multi-stage Maven → JRE Alpine build
├── docker-compose.yml              # Per-service compose (DB + app)
├── pom.xml
└── src/main/java/.../
    ├── config/        # Kafka topic & consumer config
    ├── controller/    # REST endpoints
    ├── dto/           # Request / Response DTOs
    ├── event/         # Kafka event POJOs
    ├── kafka/         # Consumers & Producers
    ├── model/         # JPA entities & enums
    ├── repository/    # Spring Data JPA repositories
    └── service/       # Business logic
```

---

## Tech Stack

- **Java 21** / **Spring Boot 3**
- **Spring Data JPA** + **PostgreSQL 17**
- **Spring Kafka** + **Apache Kafka** (KRaft, 3-broker cluster)
- **Lombok**
- **Docker** / **Docker Compose**
- **Maven** (multi-stage builds)
