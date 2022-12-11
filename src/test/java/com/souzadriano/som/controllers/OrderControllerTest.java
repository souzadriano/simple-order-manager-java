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
import com.souzadriano.som.controllers.dtos.OrderCreateDTO;
import com.souzadriano.som.entities.Item;
import com.souzadriano.som.entities.Order;
import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.entities.User;
import com.souzadriano.som.helpers.ItemTestHelper;
import com.souzadriano.som.helpers.OrderTestHelper;
import com.souzadriano.som.helpers.UserTestHelper;
import com.souzadriano.som.repositories.OrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderTestHelper orderTestHelper;

	@Autowired
	private ItemTestHelper itemTestHelper;
	
	@Autowired
	private UserTestHelper userTestHelper;

	@Autowired
	private OrderRepository orderRepository;

	@BeforeEach
	private void beforeEach() {
		orderRepository.deleteAll();
	}

	@Test
	public void list() throws Exception {
		Order order1 = orderTestHelper.createOrder1();
		Order order2 = orderTestHelper.createOrder2();
		assertThat(mockMvc.perform(get(OrderTestHelper.ORDER_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$[0].item.itemId", is(order1.getItem().getItemId().intValue()))) //
				.andExpect(jsonPath("$[0].user.userId", is(order1.getUser().getUserId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(OrderTestHelper.ORDER_2_QUANTITY))) //
				.andExpect(jsonPath("$[1].item.itemId", is(order2.getItem().getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].user.userId", is(order2.getUser().getUserId().intValue()))) //
		);
	}

	@Test
	public void getOne() throws Exception {
		Order order1 = orderTestHelper.createOrder1();
		assertThat(mockMvc
				.perform(get(OrderTestHelper.ORDER_PATH + "/" + order1.getOrderId())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", is(order1.getOrderId().intValue()))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.item.itemId", is(order1.getItem().getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(order1.getUser().getUserId().intValue()))) //
		);
	}

	@Test
	public void create() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);
	}

	@Test
	public void createWithEmptyQuantity() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new OrderCreateDTO(null, item1.getItemId(), user1.getUserId()))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void createWithEmptyItem() throws Exception {
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new OrderCreateDTO(OrderTestHelper.ORDER_1_QUANTITY, null, user1.getUserId()))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void createWithEmptyUser() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new OrderCreateDTO(OrderTestHelper.ORDER_1_QUANTITY, item1.getItemId(), null))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void update() throws Exception {
		Order order1 = orderTestHelper.createOrder1();
		assertThat(mockMvc
				.perform(put(OrderTestHelper.ORDER_PATH + "/" + order1.getOrderId())
						.contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void deleteOrder() throws Exception {
		Order order1 = orderTestHelper.createOrder1();
		assertThat(mockMvc
				.perform(
						delete(OrderTestHelper.ORDER_PATH + "/" + order1.getOrderId()))
				.andExpect(status().isOk()));
		assertThat(
				mockMvc.perform(get(OrderTestHelper.ORDER_PATH))
						.andExpect(jsonPath("$", hasSize(1))) //
						.andExpect(jsonPath("$[0].quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
						.andExpect(jsonPath("$[0].item.itemId", is(order1.getItem().getItemId().intValue()))) //
						.andExpect(jsonPath("$[0].user.userId", is(order1.getUser().getUserId().intValue()))) //
						.andExpect(jsonPath("$[0].status", is(OrderStatus.CANCELED.toString()))) //
		);
	}

}
