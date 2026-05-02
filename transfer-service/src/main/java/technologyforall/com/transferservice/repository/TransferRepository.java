package technologyforall.com.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technologyforall.com.transferservice.model.Transfer;
import technologyforall.com.transferservice.model.TransferStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByReferenceNumber(String referenceNumber);
    List<Transfer> findByBusinessId(Long businessId);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findByVirtualAccountNumber(String virtualAccountNumber);
}
