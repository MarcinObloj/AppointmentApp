package com.financial.experts.module.user.service;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpertRepository expertRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getExpertWithUserById_ShouldReturnExpertWithUser_WhenBothExist() {
        // Arrange
        Long expertId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        Expert expert = new Expert();
        expert.setId(expertId);
        expert.setUser(user); // Set full user object

        when(expertRepository.findById(expertId)).thenReturn(Optional.of(expert));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Expert result = userService.getExpertWithUserById(expertId);

        // Assert
        assertNotNull(result);
        assertEquals(expertId, result.getId());
        assertNotNull(result.getUser());
        assertEquals(userId, result.getUser().getId());

        verify(expertRepository).findById(expertId);
        verify(userRepository).findById(userId);
    }

    @Test
    void getExpertWithUserById_ShouldThrowException_WhenExpertNotFound() {
        // Arrange
        Long expertId = 99L;
        when(expertRepository.findById(expertId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getExpertWithUserById(expertId));

        assertEquals("Expert not found", exception.getMessage());
        verify(expertRepository).findById(expertId);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void getExpertWithUserById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Long expertId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        Expert expert = new Expert();
        expert.setId(expertId);
        expert.setUser(user);

        when(expertRepository.findById(expertId)).thenReturn(Optional.of(expert));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getExpertWithUserById(expertId));

        assertEquals("User not found", exception.getMessage());
        verify(expertRepository).findById(expertId);
        verify(userRepository).findById(userId);
    }

    @Test
    void getExpertsByCityAndSpecialization_ShouldReturnExperts_WhenFound() {
        // Arrange
        String city = "New York";
        String specialization = "Financial Planning";

        Expert expert1 = new Expert();
        expert1.setId(1L);
        expert1.setCity(city);

        Expert expert2 = new Expert();
        expert2.setId(2L);
        expert2.setCity(city);

        List<Expert> expectedExperts = List.of(expert1, expert2);

        when(expertRepository.findByCityAndSpecialization(city, specialization))
                .thenReturn(expectedExperts);

        // Act
        List<Expert> result = userService.getExpertsByCityAndSpecialization(city, specialization);

        // Assert
        assertEquals(2, result.size());
        verify(expertRepository).findByCityAndSpecialization(city, specialization);
    }

    @Test
    void getExpertsByCityAndSpecialization_ShouldReturnEmptyList_WhenNoneFound() {
        // Arrange
        String city = "Unknown";
        String specialization = "Rare Specialty";

        when(expertRepository.findByCityAndSpecialization(city, specialization))
                .thenReturn(Collections.emptyList());

        // Act
        List<Expert> result = userService.getExpertsByCityAndSpecialization(city, specialization);

        // Assert
        assertTrue(result.isEmpty());
        verify(expertRepository).findByCityAndSpecialization(city, specialization);
    }
}