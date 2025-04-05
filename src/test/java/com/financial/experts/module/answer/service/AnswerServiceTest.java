package com.financial.experts.module.answer.service;

import com.financial.experts.database.postgres.entity.*;
import com.financial.experts.database.postgres.repository.*;
import com.financial.experts.module.answer.dto.AnswerResponseDto;
import com.financial.experts.module.answer.exception.AnswerException;
import com.financial.experts.module.expert.dto.ExpertDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExpertRepository expertRepository;

    @InjectMocks
    private AnswerService answerService;

    @Test
    void createAnswer_ShouldReturnResponseDto_WhenInputIsValid() {
        // Arrange
        Answer answer = new Answer();
        answer.setContent("Test answer content");

        Question question = new Question();
        question.setId(1L);

        Expert expert = new Expert();
        expert.setId(1L);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhotoUrl("photo.jpg");
        expert.setUser(user);

        Answer savedAnswer = new Answer();
        savedAnswer.setId(1L);
        savedAnswer.setContent(answer.getContent());
        savedAnswer.setCreatedAt(LocalDateTime.now());
        savedAnswer.setQuestion(question);
        savedAnswer.setExpert(expert);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(expertRepository.findById(1L)).thenReturn(Optional.of(expert));
        when(answerRepository.save(any(Answer.class))).thenReturn(savedAnswer);

        // Act
        AnswerResponseDto result = answerService.createAnswer(answer, 1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(savedAnswer.getId(), result.getId());
        assertEquals(savedAnswer.getContent(), result.getContent());
        assertEquals("John", result.getExpert().getFirstName());
        assertEquals("Doe", result.getExpert().getLastName());

        verify(questionRepository, times(1)).findById(1L);
        verify(expertRepository, times(1)).findById(1L);
        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void createAnswer_ShouldThrowException_WhenQuestionNotFound() {
        // Arrange
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        AnswerException exception = assertThrows(AnswerException.class,
                () -> answerService.createAnswer(new Answer(), 1L, 1L));

        assertEquals("Question not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void createAnswer_ShouldThrowException_WhenExpertNotFound() {
        // Arrange
        Question question = new Question();
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(expertRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        AnswerException exception = assertThrows(AnswerException.class,
                () -> answerService.createAnswer(new Answer(), 1L, 1L));

        assertEquals("Expert not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void mapToResponseDto_ShouldHandleNullUser() {
        // Arrange
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setContent("Test content");
        answer.setCreatedAt(LocalDateTime.now());

        Expert expert = new Expert();
        expert.setId(1L);
        expert.setStreet("Test St");
        expert.setCity("Test City");
        answer.setExpert(expert);

        // Act
        AnswerResponseDto result = answerService.mapToResponseDto(answer);

        // Assert
        assertNotNull(result);
        assertEquals("Unknown", result.getExpert().getFirstName());
        assertEquals("Expert", result.getExpert().getLastName());
        assertEquals("", result.getExpert().getPhotoUrl());
    }
}