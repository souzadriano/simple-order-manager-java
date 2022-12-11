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
import com.souzadriano.som.controllers.dtos.OrderCreateDTO;
import com.souzadriano.som.entities.Item;
import com.souzadriano.som.entities.Order;
import com.souzadriano.som.entities.User;

@Component
public class OrderTestHelper {

	public static final String ORDER_PATH = "/orders";

	public static final Integer ORDER_1_QUANTITY = 10;

	public static final Integer ORDER_2_QUANTITY = 20;

	@Autowired
	private UserTestHelper userTestHelper;

	@Autowired
	private ItemTestHelper itemTestHelper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Order createOrder(OrderCreateDTO order)
			throws UnsupportedEncodingException, Exception, JsonProcessingException, JsonMappingException {
		String content = mockMvc
				.perform(post(ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(content, Order.class);
	}

	public Order createOrder1() throws Exception, JsonProcessingException {
		Item item = itemTestHelper.createItem1();
		User user = userTestHelper.createUser1();
		return createOrder(newOrder1(item.getItemId(), user.getUserId()));
	}

	public Order createOrder2() throws Exception, JsonProcessingException {
		Item item = itemTestHelper.createItem2();
		User user = userTestHelper.createUser2();
		return createOrder(newOrder2(item.getItemId(), user.getUserId()));
	}

	public OrderCreateDTO newOrder1(Long itemId, Long userId) {
		return new OrderCreateDTO(ORDER_1_QUANTITY, itemId, userId);
	}

	public OrderCreateDTO newOrder2(Long itemId, Long userId) {
		return new OrderCreateDTO(ORDER_2_QUANTITY, itemId, userId);
	}
}
