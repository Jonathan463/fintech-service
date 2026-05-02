package technologyforall.com.chargeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technologyforall.com.chargeservice.model.Charge;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    boolean existsByTransferId(Long transferId);
}
