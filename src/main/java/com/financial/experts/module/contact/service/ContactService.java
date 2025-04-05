package com.financial.experts.module.contact.service;

import com.financial.experts.module.contact.dto.ContactDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendContactEmail(ContactDTO dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Ustawienia wiadomości
            helper.setTo("yourcompany@example.com");
            helper.setSubject("New Contact Message from " + dto.getFirstName() + " " + dto.getLastName());

            // Przygotowanie danych dla szablonu
            Context context = new Context();
            context.setVariable("firstName", dto.getFirstName());
            context.setVariable("lastName", dto.getLastName());
            context.setVariable("email", dto.getEmail());
            context.setVariable("phone", dto.getPhone());
            context.setVariable("message", dto.getMessage());


            String htmlContent = templateEngine.process("contact-email", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}