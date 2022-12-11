package com.souzadriano.som.entities;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -8623425263918596756L;

	private Long userId;
	private String name;
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
