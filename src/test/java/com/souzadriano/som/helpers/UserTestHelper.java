package com.souzadriano.som.helpers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.souzadriano.som.entities.User;

@Component
public class UserTestHelper {

	public static final String USER_1_NAME = "A_USER_1";
	public static final String USER_1_EMAIL = "A_USER_1@localhost";

	public static final String USER_2_NAME = "A_USER_2";
	public static final String USER_2_EMAIL = "A_USER_2@localhost";

	public static final String USERS_PATH = "/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private User createUser(User user)
			throws UnsupportedEncodingException, Exception, JsonProcessingException, JsonMappingException {
		String content = mockMvc
				.perform(post(USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(content, User.class);
	}

	public User createUser2() throws Exception, JsonProcessingException {
		return createUser(newUser2());
	}

	public User createUser1() throws Exception, JsonProcessingException {
		return createUser(newUser1());
	}

	public User newUser1() {
		return new User(USER_1_NAME, USER_1_EMAIL);
	}

	public User newUser2() {
		return new User(USER_2_NAME, USER_2_EMAIL);
	}
}
