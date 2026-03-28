package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.entity.ProgressSubmission;
import com.phdprogress.phd_progress.entity.User;
import com.phdprogress.phd_progress.exception.EmailNotificationException;
import com.phdprogress.phd_progress.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${app.mail.from:no-reply@phd-progress.local}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    @Async("notificationTaskExecutor")
    public void notifyAdvisorSubmissionSubmitted(ProgressSubmission submission, User actor, User advisor) {
        sendWorkflowEmail(
                "SUBMISSION_SUBMITTED",
                advisor,
                buildSubject("Submission submitted for advisor review", submission),
                buildBody(submission, actor, "Student submitted the submission. Please review and take action.")
        );
    }

    @Override
    @Async("notificationTaskExecutor")
    public void notifyDirectorAdvisorApproved(ProgressSubmission submission, User actor, User director) {
        sendWorkflowEmail(
                "ADVISOR_APPROVED",
                director,
                buildSubject("Submission approved by advisor", submission),
                buildBody(submission, actor, "Advisor approved the submission. Director review is now required.")
        );
    }

    @Override
    @Async("notificationTaskExecutor")
    public void notifyStudentAdvisorRejected(ProgressSubmission submission, User actor, User student) {
        sendWorkflowEmail(
                "ADVISOR_REJECTED",
                student,
                buildSubject("Submission rejected by advisor", submission),
                buildBody(submission, actor, "Advisor rejected the submission. Please review comments and update as needed.")
        );
    }

    @Override
    @Async("notificationTaskExecutor")
    public void notifyStudentDirectorApproved(ProgressSubmission submission, User actor, User student) {
        sendWorkflowEmail(
                "DIRECTOR_APPROVED",
                student,
                buildSubject("Submission approved by director", submission),
                buildBody(submission, actor, "Director approved the submission. The workflow has been completed.")
        );
    }

    @Override
    @Async("notificationTaskExecutor")
    public void notifyStudentDirectorRejected(ProgressSubmission submission, User actor, User student) {
        sendWorkflowEmail(
                "DIRECTOR_REJECTED",
                student,
                buildSubject("Submission rejected by director", submission),
                buildBody(submission, actor, "Director rejected the submission. Please review comments and next steps.")
        );
    }

    private void sendWorkflowEmail(String eventType, User recipient, String subject, String body) {
        if (recipient == null || recipient.getEmail() == null || recipient.getEmail().isBlank()) {
            log.warn("email_skipped eventType={} reason=no_recipient_email", eventType);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(recipient.getEmail());
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(message);
            log.info("email_send_success eventType={} recipient={}", eventType, recipient.getEmail());
        } catch (MessagingException | RuntimeException exception) {
            log.error("email_send_failure eventType={} recipient={} reason={}",
                    eventType, recipient.getEmail(), exception.getMessage(), exception);
            throw new EmailNotificationException("Failed to send email notification", exception);
        }
    }

    private String buildSubject(String prefix, ProgressSubmission submission) {
        return prefix + " [Submission #" + submission.getId() + "]";
    }

    private String buildBody(ProgressSubmission submission, User actor, String message) {
        return """
                PhD Progress Notification

                Submission ID: %d
                Submission Title: %s
                Current Status: %s
                Triggered By: %s

                Message:
                %s
                """.formatted(
                submission.getId(),
                submission.getTitle(),
                submission.getStatus(),
                actor.getUsername(),
                message
        );
    }
}
