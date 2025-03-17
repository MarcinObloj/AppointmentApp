package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    @Query("SELECT b FROM BlogPost b WHERE b.expert.id = :expertId ORDER BY b.createdAt DESC")
    List<BlogPost> findLatestPostsByExpertId(@Param("expertId") Long expertId);
}