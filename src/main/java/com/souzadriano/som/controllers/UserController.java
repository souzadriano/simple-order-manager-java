package com.souzadriano.som.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.souzadriano.som.entities.User;
import com.souzadriano.som.services.UserService;

@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<User> findAll() {
		return userService.findAll();
	}

	@PostMapping("/users")
	public User create(@Valid @RequestBody User user) {
		return userService.create(user);
	}

	@GetMapping("/users/{userId}")
	public User findOne(@PathVariable Long userId) {
		return userService.findOne(userId);
	}

	@PutMapping("/users/{userId}")
	public User update(@PathVariable Long userId, @Valid @RequestBody User user) {
		return userService.update(userId, user);
	}

	@DeleteMapping("/users/{userId}")
	public void delete(@PathVariable Long userId) {
		userService.delete(userId);
	}

}
