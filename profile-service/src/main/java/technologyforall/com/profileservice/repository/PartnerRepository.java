package technologyforall.com.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import technologyforall.com.profileservice.model.Partner;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findByBusinessId(Long businessId);

    @Query("SELECT COALESCE(SUM(p.ratio), 0) FROM Partner p WHERE p.business.id = :businessId")
    float sumRatioByBusinessId(@Param("businessId") Long businessId);
}
