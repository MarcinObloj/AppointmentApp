package com.financial.experts.module.contact.service;

import com.financial.experts.module.contact.dto.ContactDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private ContactService contactService;

    @Test
    void sendContactEmail_ShouldSendEmailSuccessfully() throws Exception {
        // Arrange
        ContactDTO contactDTO = createTestContactDTO();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("contact-email"), any(Context.class)))
                .thenReturn("<html>Email content</html>");

        // Act
        contactService.sendContactEmail(contactDTO);

        // Assert
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendContactEmail_ShouldThrowRuntimeException_WhenEmailFails() throws Exception {
        // Arrange
        ContactDTO contactDTO = createTestContactDTO();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("contact-email"), any(Context.class)))
                .thenReturn("<html>Email content</html>");

        // Symulacja błędu SMTP - poprawne rzucenie wyjątku
        doThrow(new RuntimeException("Failed to send email", new MessagingException("SMTP error")))
                .when(mailSender).send(mimeMessage);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> contactService.sendContactEmail(contactDTO));

        assertEquals("Failed to send email", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(MessagingException.class, exception.getCause().getClass());
    }

    @Test
    void sendContactEmail_ShouldSetCorrectContextVariables() throws Exception {
        // Arrange
        ContactDTO contactDTO = createTestContactDTO();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("contact-email"), any(Context.class)))
                .thenAnswer(invocation -> {
                    Context context = invocation.getArgument(1);
                    assertEquals(contactDTO.getFirstName(), context.getVariable("firstName"));
                    assertEquals(contactDTO.getLastName(), context.getVariable("lastName"));
                    assertEquals(contactDTO.getEmail(), context.getVariable("email"));
                    assertEquals(contactDTO.getPhone(), context.getVariable("phone"));
                    assertEquals(contactDTO.getMessage(), context.getVariable("message"));
                    return "<html>Email content</html>";
                });

        // Act
        contactService.sendContactEmail(contactDTO);

        // Assert
        verify(templateEngine).process(eq("contact-email"), any(Context.class));
    }

    private ContactDTO createTestContactDTO() {
        ContactDTO dto = new ContactDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("123456789");
        dto.setMessage("Test message content");
        return dto;
    }
}
