package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.ExpertSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpertSpecializationRepository extends JpaRepository<ExpertSpecialization, Long> {

}
