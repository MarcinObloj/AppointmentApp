package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {

    @Query("SELECT e FROM Expert e JOIN e.user u WHERE u.role = 'EXPERT' AND e.experienceYears >= :minExperience")
    List<Expert> findExpertsWithMinExperience(@Param("minExperience") int minExperience);

    Optional<Expert> findByUserId(Long userId);
}