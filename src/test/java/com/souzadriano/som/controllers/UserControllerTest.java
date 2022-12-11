package com.souzadriano.som.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.souzadriano.som.entities.User;
import com.souzadriano.som.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

	private static final String A_USER_1_NAME = "A_USER_1";
	private static final String A_USER_1_EMAIL = "A_USER_1@localhost";

	private static final String A_USER_2_NAME = "A_USER_2";
	private static final String A_USER_2_EMAIL = "A_USER_2@localhost";

	private static final String USERS_PATH = "/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	private void beforeEach() {
		userRepository.deleteAll();
	}

	@Test
	public void list() throws Exception {
		createUser1();
		createUser2();
		assertThat(mockMvc.perform(get(USERS_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].name", is(A_USER_1_NAME))) //
				.andExpect(jsonPath("$[0].email", is(A_USER_1_EMAIL))) //
				.andExpect(jsonPath("$[1].name", is(A_USER_2_NAME))) //
				.andExpect(jsonPath("$[1].email", is(A_USER_2_EMAIL))) //
		);
	}

	@Test
	public void getOne() throws Exception {
		User user1 = createUser1();
		assertThat(mockMvc.perform(get(USERS_PATH + "/" + user1.getUserId())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", is(user1.getUserId().intValue()))) //
				.andExpect(jsonPath("$.name", is(A_USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is(A_USER_1_EMAIL))) //
		);
	}

	@Test
	public void create() throws Exception {
		assertThat(mockMvc
				.perform(post(USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUser1())))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", isA(Number.class))) //
				.andExpect(jsonPath("$.name", is(A_USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is(A_USER_1_EMAIL))) //
		);
	}

	@Test
	public void update() throws Exception {
		User user1 = createUser1();
		User userUpdate = new User(user1.getUserId(), "UPDATED" + user1.getName(), "UPDATED" + user1.getEmail());
		assertThat(mockMvc
				.perform(put(USERS_PATH + "/" + user1.getUserId()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userUpdate)))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", is(user1.getUserId().intValue()))) //
				.andExpect(jsonPath("$.name", is("UPDATED" + A_USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is("UPDATED" + A_USER_1_EMAIL))) //
		);
	}

	@Test
	public void deleteUser() throws Exception {
		User user1 = createUser1();
		assertThat(mockMvc.perform(delete(USERS_PATH + "/" + user1.getUserId())).andExpect(status().isOk()));
		assertThat(mockMvc.perform(get(USERS_PATH)).andExpect(jsonPath("$", hasSize(0))));
	}

	private User createUser(User user2)
			throws UnsupportedEncodingException, Exception, JsonProcessingException, JsonMappingException {
		String content = mockMvc
				.perform(post(USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user2)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(content, User.class);
	}

	private User createUser2() throws Exception, JsonProcessingException {
		return createUser(newUser2());
	}

	private User createUser1() throws Exception, JsonProcessingException {
		return createUser(newUser1());
	}

	private User newUser1() {
		return new User(A_USER_1_NAME, A_USER_1_EMAIL);
	}

	private User newUser2() {
		return new User(A_USER_2_NAME, A_USER_2_EMAIL);
	}

}
