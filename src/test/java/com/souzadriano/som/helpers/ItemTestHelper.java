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
import com.souzadriano.som.entities.Item;

@Component
public class ItemTestHelper {

	public static final String A_ITEM_1_NAME = "A_ITEM_1";

	public static final String A_ITEM_2_NAME = "A_ITEM_2";

	public static final String ITEMS_PATH = "/items";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Item createItem(Item item)
			throws UnsupportedEncodingException, Exception, JsonProcessingException, JsonMappingException {
		String content = mockMvc
				.perform(post(ITEMS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(item)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(content, Item.class);
	}

	public Item createItem2() throws Exception, JsonProcessingException {
		return createItem(newItem2());
	}

	public Item createItem1() throws Exception, JsonProcessingException {
		return createItem(newItem1());
	}

	public Item newItem1() {
		return new Item(A_ITEM_1_NAME);
	}

	public Item newItem2() {
		return new Item(A_ITEM_2_NAME);
	}

}
