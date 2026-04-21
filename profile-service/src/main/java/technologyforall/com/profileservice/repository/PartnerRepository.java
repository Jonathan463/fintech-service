package technologyforall.com.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technologyforall.com.profileservice.model.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long> {
}
