package com.souzadriano.som.jpaentities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "som_user")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -8623425263918596756L;

	@Id
	@GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
	@Column(name = "user_id")
	private Long userId;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	@NotBlank
	private String email;

	@NotNull
	private Boolean disabled;

	public UserEntity() {
		super();
	}

	public UserEntity(String name, String email, Boolean disabled) {
		super();
		this.name = name;
		this.email = email;
		this.disabled = disabled;
	}

	public UserEntity(Long userId, String name, String email, Boolean disabled) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.disabled = disabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
