package com.financial.experts.module.contact.service;

import com.financial.experts.module.contact.dto.ContactDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    public void sendContactEmail(ContactDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("yourcompany@example.com"); // Company email address
        message.setSubject("New Contact Message from " + dto.getFirstName() + " " + dto.getLastName());
        message.setText("Sender: " + dto.getEmail() + "\n"
                + "Phone: " + dto.getPhone() + "\n\n"
                + dto.getMessage());
        mailSender.send(message);
    }
}