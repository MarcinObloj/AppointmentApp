package com.financial.experts.module.blog.service;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    void getAllQuestionsWithAnswers_ShouldReturnQuestions_WhenQuestionsExist() {
        // Arrange
        Question question1 = new Question();
        question1.setId(1L);


        Question question2 = new Question();
        question2.setId(2L);


        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

        // Act
        List<Question> result = blogService.getAllQuestionsWithAnswers();

        // Assert
        assertEquals(2, result.size());

        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void getAllQuestionsWithAnswers_ShouldReturnEmptyList_WhenNoQuestionsExist() {
        // Arrange
        when(questionRepository.findAll()).thenReturn(List.of());

        // Act
        List<Question> result = blogService.getAllQuestionsWithAnswers();

        // Assert
        assertTrue(result.isEmpty());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void getQuestionById_ShouldReturnQuestion_WhenQuestionExists() {
        // Arrange
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);


        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // Act
        Question result = blogService.getQuestionById(questionId);

        // Assert
        assertNotNull(result);
        assertEquals(questionId, result.getId());

        verify(questionRepository, times(1)).findById(questionId);
    }

    @Test
    void getQuestionById_ShouldThrowException_WhenQuestionNotFound() {
        // Arrange
        Long questionId = 99L;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> blogService.getQuestionById(questionId));

        assertEquals("Question not found", exception.getMessage());
        verify(questionRepository, times(1)).findById(questionId);
    }
}