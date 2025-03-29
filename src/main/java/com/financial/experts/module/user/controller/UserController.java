package com.financial.experts.module.user.controller;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("experts/{expertId}")
    public Expert getExpertWithUserById(@PathVariable Long expertId){
        return userService.getExpertWithUserById(expertId);
    }
    @GetMapping("/experts")
    public List<Expert> getExpertsByCityAndSpecialization(
            @RequestParam String city,
            @RequestParam String specialization) {
        return userService.getExpertsByCityAndSpecialization(city, specialization);
    }
}
