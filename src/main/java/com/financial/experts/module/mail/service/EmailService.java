package com.financial.experts.module.mail.service;

import com.financial.experts.database.postgres.entity.Appointment;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import com.financial.experts.shared.util.ApiConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    private final VerificationTokenRepository verificationTokenRepository;

    public void sendVerificationEmail(User user) throws MessagingException {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        String verificationLink = ApiConstant.VERIFICATION_LINK + token;

        Context context = new Context();
        context.setVariable("name", user.getFirstName());
        context.setVariable("verificationLink", verificationLink);
        String emailContent = templateEngine.process("email-verification",context);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Email Verification");
        helper.setText(emailContent, true);
        emailSender.send(message);

    }

    public void sendAppointmentConfirmation(User client, Expert expert, Appointment appointment) throws MessagingException {
        String subjectClient = "Appointment Confirmation";
        String subjectExpert = "New Appointment Booked";

        // Email for
        MimeMessage messageClient = emailSender.createMimeMessage();
        MimeMessageHelper helperClient = new MimeMessageHelper(messageClient, true, "UTF-8");
        helperClient.setTo(client.getEmail());
        helperClient.setSubject(subjectClient);
        String clientBody = "Dear " + client.getFirstName() + ",\n\n" +
                "Your appointment has been booked on " + appointment.getAppointmentDate() + " at " +
                appointment.getAppointmentTime() + ".\n\n" +
                "Best regards,\nYour Experts Team";
        helperClient.setText(clientBody, false);
        emailSender.send(messageClient);

        // Email for expert
        MimeMessage messageExpert = emailSender.createMimeMessage();
        MimeMessageHelper helperExpert = new MimeMessageHelper(messageExpert, true, "UTF-8");
        helperExpert.setTo(expert.getUser().getEmail());
        helperExpert.setSubject(subjectExpert);
        String expertBody = "Dear " + expert.getUser().getFirstName() + ",\n\n" +
                "A new appointment has been booked on " + appointment.getAppointmentDate() + " at " +
                appointment.getAppointmentTime() + " by client " +
                client.getFirstName() + " " + client.getLastName() + ".\n\n" +
                "Best regards,\nYour Experts Team";
        helperExpert.setText(expertBody, false);
        emailSender.send(messageExpert);
    }

}
