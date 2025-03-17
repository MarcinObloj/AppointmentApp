package com.financial.experts.module.expert.service;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.repository.ExpertRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertService {


    private final ExpertRepository expertRepository;

    @Autowired
    public ExpertService(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }
    public List<Expert> getAllExperts() {
        return expertRepository.findAll();
    }

    public Optional<Expert> getExpertById(Long id) {
        return expertRepository.findById(id);
    }

    public Expert createExpert(Expert expert) {
        return expertRepository.save(expert);
    }

    public Expert updateExpert(Long id, Expert expertDetails) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        expert.setDescription(expertDetails.getDescription());
        expert.setExperienceYears(expertDetails.getExperienceYears());
        return expertRepository.save(expert);
    }

    public void deleteExpert(Long id) {
        expertRepository.deleteById(id);
    }
}