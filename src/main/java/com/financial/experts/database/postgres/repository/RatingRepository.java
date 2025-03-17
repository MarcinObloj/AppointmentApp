package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.expert.id = :expertId")
    Double findAverageRatingByExpertId(@Param("expertId") Long expertId);
}