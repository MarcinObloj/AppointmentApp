package com.financial.experts.module.expert.service;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpertServiceTest {

    @Mock
    private ExpertRepository expertRepository;

    @InjectMocks
    private ExpertService expertService;

    @Test
    void getAllExperts_ShouldReturnAllExperts() {
        // Arrange
        Expert expert1 = new Expert();
        expert1.setId(1L);
        Expert expert2 = new Expert();
        expert2.setId(2L);

        when(expertRepository.findAll()).thenReturn(Arrays.asList(expert1, expert2));

        // Act
        List<Expert> experts = expertService.getAllExperts();

        // Assert
        assertEquals(2, experts.size());
        verify(expertRepository, times(1)).findAll();
    }

    @Test
    void getExpertById_ShouldReturnExpert_WhenExpertExists() {
        // Arrange
        Long expertId = 1L;
        Expert expert = new Expert();
        expert.setId(expertId);

        when(expertRepository.findById(expertId)).thenReturn(Optional.of(expert));

        // Act
        Optional<Expert> foundExpert = expertService.getExpertById(expertId);

        // Assert
        assertTrue(foundExpert.isPresent());
        assertEquals(expertId, foundExpert.get().getId());
        verify(expertRepository, times(1)).findById(expertId);
    }

    @Test
    void getExpertById_ShouldReturnEmpty_WhenExpertNotExists() {
        // Arrange
        Long expertId = 99L;
        when(expertRepository.findById(expertId)).thenReturn(Optional.empty());

        // Act
        Optional<Expert> foundExpert = expertService.getExpertById(expertId);

        // Assert
        assertFalse(foundExpert.isPresent());
        verify(expertRepository, times(1)).findById(expertId);
    }

    @Test
    void createExpert_ShouldSaveAndReturnExpert() {
        // Arrange
        Expert newExpert = new Expert();
        newExpert.setDescription("Test Expert");

        when(expertRepository.save(newExpert)).thenReturn(newExpert);

        // Act
        Expert savedExpert = expertService.createExpert(newExpert);

        // Assert
        assertNotNull(savedExpert);
        assertEquals("Test Expert", savedExpert.getDescription());
        verify(expertRepository, times(1)).save(newExpert);
    }

    @Test
    void updateExpert_ShouldUpdateAndReturnExpert_WhenExpertExists() {
        // Arrange
        Long expertId = 1L;
        Expert existingExpert = new Expert();
        existingExpert.setId(expertId);
        existingExpert.setDescription("Old Description");
        existingExpert.setExperienceYears(5);

        Expert updatedDetails = new Expert();
        updatedDetails.setDescription("New Description");
        updatedDetails.setExperienceYears(10);

        when(expertRepository.findById(expertId)).thenReturn(Optional.of(existingExpert));
        when(expertRepository.save(existingExpert)).thenReturn(existingExpert);

        // Act
        Expert updatedExpert = expertService.updateExpert(expertId, updatedDetails);

        // Assert
        assertEquals("New Description", updatedExpert.getDescription());
        assertEquals(10, updatedExpert.getExperienceYears());
        verify(expertRepository, times(1)).findById(expertId);
        verify(expertRepository, times(1)).save(existingExpert);
    }

    @Test
    void updateExpert_ShouldThrowException_WhenExpertNotExists() {
        // Arrange
        Long expertId = 99L;
        Expert updatedDetails = new Expert();

        when(expertRepository.findById(expertId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> expertService.updateExpert(expertId, updatedDetails));
        verify(expertRepository, times(1)).findById(expertId);
        verify(expertRepository, never()).save(any());
    }

    @Test
    void deleteExpert_ShouldDeleteExpert() {
        // Arrange
        Long expertId = 1L;
        doNothing().when(expertRepository).deleteById(expertId);

        // Act
        expertService.deleteExpert(expertId);

        // Assert
        verify(expertRepository, times(1)).deleteById(expertId);
    }
}