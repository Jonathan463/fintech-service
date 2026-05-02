package technologyforall.com.ledgerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technologyforall.com.ledgerservice.model.LedgerEntry;
import technologyforall.com.ledgerservice.model.LedgerEntryType;

import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByTransferId(Long transferId);
    List<LedgerEntry> findByBusinessId(Long businessId);
    List<LedgerEntry> findByReferenceNumber(String referenceNumber);
    List<LedgerEntry> findByEntryType(LedgerEntryType entryType);
    boolean existsByTransferIdAndEntryType(Long transferId, LedgerEntryType entryType);
}
