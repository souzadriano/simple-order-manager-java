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
import com.souzadriano.som.entities.Item;
import com.souzadriano.som.helpers.ItemTestHelper;
import com.souzadriano.som.repositories.ItemRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ItemTestHelper itemTestHelper;

	@Autowired
	private ItemRepository itemRepository;

	@BeforeEach
	private void beforeEach() {
		itemRepository.deleteAll();
	}

	@Test
	public void list() throws Exception {
		itemTestHelper.createItem1();
		itemTestHelper.createItem2();
		assertThat(mockMvc.perform(get(ItemTestHelper.ITEMS_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].name", is(ItemTestHelper.A_ITEM_1_NAME))) //
				.andExpect(jsonPath("$[1].name", is(ItemTestHelper.A_ITEM_2_NAME))) //
		);
	}

	@Test
	public void getOne() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		assertThat(mockMvc.perform(get(ItemTestHelper.ITEMS_PATH + "/" + item1.getItemId())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.name", is(ItemTestHelper.A_ITEM_1_NAME))) //
		);
	}

	@Test
	public void create() throws Exception {
		assertThat(mockMvc
				.perform(post(ItemTestHelper.ITEMS_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(itemTestHelper.newItem1())))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.itemId", isA(Number.class))) //
				.andExpect(jsonPath("$.name", is(ItemTestHelper.A_ITEM_1_NAME))) //
		);
	}

	@Test
	public void createWithEmptyName() throws Exception {
		assertThat(mockMvc.perform(post(ItemTestHelper.ITEMS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Item("")))).andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void update() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		Item itemUpdate = new Item(item1.getItemId(), "UPDATED" + item1.getName());
		assertThat(mockMvc
				.perform(put(ItemTestHelper.ITEMS_PATH + "/" + item1.getItemId()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(itemUpdate)))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.name", is("UPDATED" + ItemTestHelper.A_ITEM_1_NAME))) //
		);
	}

	@Test
	public void updateWithEmptyName() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		Item itemUpdate = new Item(item1.getItemId(), "");
		assertThat(mockMvc.perform(put(ItemTestHelper.ITEMS_PATH + "/" + item1.getItemId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(itemUpdate))).andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void deleteItem() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		assertThat(mockMvc.perform(delete(ItemTestHelper.ITEMS_PATH + "/" + item1.getItemId())).andExpect(status().isOk()));
		assertThat(mockMvc.perform(get(ItemTestHelper.ITEMS_PATH)).andExpect(jsonPath("$", hasSize(0))));
	}

	

}
