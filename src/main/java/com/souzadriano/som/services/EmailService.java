package com.souzadriano.som.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.EmailLogStatus;
import com.souzadriano.som.jpaentities.EmailLogEntity;
import com.souzadriano.som.repositories.EmailLogRepository;

@Service
public class EmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
	
	private JavaMailSender emailSender;
	private EmailLogRepository emailLogRepository;
	
	public EmailService(JavaMailSender emailSender, EmailLogRepository emailLogRepository) {
		this.emailSender = emailSender;
		this.emailLogRepository = emailLogRepository;
	}
	
	@Async
	public void send(String to, String subject, String body) {
		EmailLogEntity emailLogEntity = new EmailLogEntity(new Date(), to, subject, body, EmailLogStatus.CREATED, body);
		try {
			emailLogEntity = emailLogRepository.save(emailLogEntity);
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(to);
			mailMessage.setSubject(subject);
			mailMessage.setText(body);
			emailSender.send(mailMessage);
			emailLogEntity.setStatus(EmailLogStatus.SENDED);
			emailLogEntity.setLog("Sended");
			emailLogEntity = emailLogRepository.save(emailLogEntity);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			emailLogEntity.setStatus(EmailLogStatus.ERROR);
			emailLogEntity.setLog(e.getMessage());
		}
	}
}
