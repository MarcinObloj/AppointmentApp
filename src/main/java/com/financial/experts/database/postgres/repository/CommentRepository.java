package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByExpertId(Long expertId);


    List<Comment> findByClientId(Long clientId);


    List<Comment> findByExpertIdAndClientId(Long expertId, Long clientId);
}