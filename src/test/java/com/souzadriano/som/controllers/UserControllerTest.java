package com.souzadriano.som.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souzadriano.som.entities.User;
import com.souzadriano.som.helpers.UserTestHelper;
import com.souzadriano.som.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

	@Autowired
	private UserTestHelper userTestHelper;

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
		userTestHelper.createUser1();
		userTestHelper.createUser2();
		assertThat(mockMvc.perform(get(UserTestHelper.USERS_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].name", is(UserTestHelper.USER_1_NAME))) //
				.andExpect(jsonPath("$[0].email", is(UserTestHelper.USER_1_EMAIL))) //
				.andExpect(jsonPath("$[1].name", is(UserTestHelper.USER_2_NAME))) //
				.andExpect(jsonPath("$[1].email", is(UserTestHelper.USER_2_EMAIL))) //
		);
	}

	@Test
	public void getOne() throws Exception {
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc.perform(get(UserTestHelper.USERS_PATH + "/" + user1.getUserId())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", is(user1.getUserId().intValue()))) //
				.andExpect(jsonPath("$.name", is(UserTestHelper.USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is(UserTestHelper.USER_1_EMAIL))) //
		);
	}

	@Test
	public void create() throws Exception {
		assertThat(mockMvc
				.perform(post(UserTestHelper.USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userTestHelper.newUser1())))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", isA(Number.class))) //
				.andExpect(jsonPath("$.name", is(UserTestHelper.USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is(UserTestHelper.USER_1_EMAIL))) //
		);
	}

	@Test
	public void createWithEmptyName() throws Exception {
		assertThat(mockMvc
				.perform(post(UserTestHelper.USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new User("", UserTestHelper.USER_1_EMAIL))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void createWithEmptyEmail() throws Exception {
		assertThat(mockMvc
				.perform(post(UserTestHelper.USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new User(UserTestHelper.USER_1_NAME, ""))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void createWithWrongEmail() throws Exception {
		assertThat(mockMvc
				.perform(post(UserTestHelper.USERS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new User(UserTestHelper.USER_1_NAME, "ABC"))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void update() throws Exception {
		User user1 = userTestHelper.createUser1();
		User userUpdate = new User(user1.getUserId(), "UPDATED" + user1.getName(), "UPDATED" + user1.getEmail());
		assertThat(mockMvc
				.perform(put(UserTestHelper.USERS_PATH + "/" + user1.getUserId()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userUpdate)))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.userId", is(user1.getUserId().intValue()))) //
				.andExpect(jsonPath("$.name", is("UPDATED" + UserTestHelper.USER_1_NAME))) //
				.andExpect(jsonPath("$.email", is("UPDATED" + UserTestHelper.USER_1_EMAIL))) //
		);
	}

	@Test
	public void updateWithEmptyName() throws Exception {
		User user1 = userTestHelper.createUser1();
		User userUpdate = new User(user1.getUserId(), "", UserTestHelper.USER_1_EMAIL);
		assertThat(mockMvc.perform(put(UserTestHelper.USERS_PATH + "/" + user1.getUserId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userUpdate))).andExpect(status().is4xxClientError()));
	}

	@Test
	public void updateWithEmptyEmail() throws Exception {
		User user1 = userTestHelper.createUser1();
		User userUpdate = new User(user1.getUserId(), UserTestHelper.USER_1_NAME, "");
		assertThat(mockMvc.perform(put(UserTestHelper.USERS_PATH + "/" + user1.getUserId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userUpdate))).andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void updateWithWrongEmail() throws Exception {
		User user1 = userTestHelper.createUser1();
		User userUpdate = new User(user1.getUserId(), UserTestHelper.USER_1_NAME, "ABC");
		assertThat(mockMvc.perform(put(UserTestHelper.USERS_PATH + "/" + user1.getUserId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userUpdate))).andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void deleteUser() throws Exception {
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc.perform(delete(UserTestHelper.USERS_PATH + "/" + user1.getUserId())).andExpect(status().isOk()));
		assertThat(mockMvc.perform(get(UserTestHelper.USERS_PATH)).andExpect(jsonPath("$", hasSize(0))));
	}

	

}
