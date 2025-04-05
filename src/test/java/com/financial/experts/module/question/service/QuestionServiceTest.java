package com.financial.experts.module.question.service;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.module.question.dto.QuestionRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void addQuestion_ShouldSuccessfullyAddQuestion_WhenClientExists() {
        // Arrange
        Long clientId = 1L;
        String questionContent = "Test question content";

        QuestionRequestDTO requestDTO = new QuestionRequestDTO();
        requestDTO.setClientId(clientId);
        requestDTO.setContent(questionContent);

        User client = new User();
        client.setId(clientId);

        Question expectedQuestion = new Question();
        expectedQuestion.setClient(client);
        expectedQuestion.setContent(questionContent);

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(questionRepository.save(any(Question.class))).thenReturn(expectedQuestion);

        // Act
        Question result = questionService.addQuestion(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clientId, result.getClient().getId());
        assertEquals(questionContent, result.getContent());

        verify(userRepository, times(1)).findById(clientId);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void addQuestion_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        Long nonExistentClientId = 99L;

        QuestionRequestDTO requestDTO = new QuestionRequestDTO();
        requestDTO.setClientId(nonExistentClientId);
        requestDTO.setContent("Test content");

        when(userRepository.findById(nonExistentClientId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> questionService.addQuestion(requestDTO));

        assertEquals("Client not found", exception.getMessage());
        verify(userRepository, times(1)).findById(nonExistentClientId);
        verify(questionRepository, never()).save(any());
    }

    @Test
    void addQuestion_ShouldSetCorrectQuestionProperties() {
        // Arrange
        Long clientId = 1L;
        String questionContent = "Detailed question content";

        QuestionRequestDTO requestDTO = new QuestionRequestDTO();
        requestDTO.setClientId(clientId);
        requestDTO.setContent(questionContent);

        User client = new User();
        client.setId(clientId);

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
            Question question = invocation.getArgument(0);
            // Verify properties are set correctly before saving
            assertEquals(client, question.getClient());
            assertEquals(questionContent, question.getContent());
            return question;
        });

        // Act
        Question result = questionService.addQuestion(requestDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(clientId);
        verify(questionRepository).save(any(Question.class));
    }
}