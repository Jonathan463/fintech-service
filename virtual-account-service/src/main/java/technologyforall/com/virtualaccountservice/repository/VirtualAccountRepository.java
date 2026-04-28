package technologyforall.com.virtualaccountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technologyforall.com.virtualaccountservice.model.VirtualAccount;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {

    boolean existsByVirtualAccount(String virtualAccount);
}
