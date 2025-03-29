package com.financial.experts.module.user.service;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final ExpertRepository expertRepository;

    public Expert getExpertWithUserById(Long expertId){
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        User user = userRepository.findById(expert.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        expert.setUser(user);
        return expert;
    }

    public List<Expert> getExpertsByCityAndSpecialization(String city, String specialization) {
        return expertRepository.findByCityAndSpecialization(city, specialization);
    }
}