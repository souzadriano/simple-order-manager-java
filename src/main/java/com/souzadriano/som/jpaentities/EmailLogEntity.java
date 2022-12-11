package com.souzadriano.som.jpaentities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.souzadriano.som.entities.EmailLogStatus;

@Entity
@Table(name = "email_log")
public class EmailLogEntity {

	@Id
	@GeneratedValue(generator = "email_log_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "email_log_seq", sequenceName = "email_log_seq", allocationSize = 1)
	@Column(name = "email_log_id")
	private Long emailLogId;

	@NotNull
	@Column(name = "creation_date")
	private Date creationDate;

	@NotNull
	@Column(name = "to_email")
	private String to;

	@NotNull
	private String subject;

	@NotNull
	@Lob
	@Column(columnDefinition = "text")
	private String body;

	@NotNull
	@Enumerated(EnumType.STRING)
	private EmailLogStatus status;

	@Lob
	@Column(columnDefinition = "text")
	private String log;

	public EmailLogEntity() {
		super();
	}

	public EmailLogEntity(Date creationDate, String to, String subject, String body, EmailLogStatus status,
			String log) {
		super();
		this.creationDate = creationDate;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.status = status;
		this.log = log;
	}

	public EmailLogEntity(Long emailLogId, Date creationDate, String to, String subject, String body,
			EmailLogStatus status, String log) {
		super();
		this.emailLogId = emailLogId;
		this.creationDate = creationDate;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.status = status;
		this.log = log;
	}

	public Long getEmailLogId() {
		return emailLogId;
	}

	public void setEmailLogId(Long emailLogId) {
		this.emailLogId = emailLogId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public EmailLogStatus getStatus() {
		return status;
	}

	public void setStatus(EmailLogStatus status) {
		this.status = status;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
