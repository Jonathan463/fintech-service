package technologyforall.com.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technologyforall.com.profileservice.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
