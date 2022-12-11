package com.souzadriano.som.entities;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class User implements Serializable {

	private static final long serialVersionUID = -8623425263918596756L;

	private Long userId;
	
	@NotNull
	@NotBlank
	private String name;
	
	@NotNull
	@NotBlank
	@Email
	private String email;

	public User() {
		super();
	}

	public User(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public User(Long userId, String name, String email) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

}
