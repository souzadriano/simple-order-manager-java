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
import com.souzadriano.som.controllers.dtos.StockMovementCreateDTO;
import com.souzadriano.som.entities.Item;
import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.entities.StockMovementOperation;
import com.souzadriano.som.helpers.ItemTestHelper;
import com.souzadriano.som.helpers.StockMovementTestHelper;
import com.souzadriano.som.repositories.ItemRepository;
import com.souzadriano.som.repositories.StockMovementRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class StockMovementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StockMovementTestHelper stockMovementTestHelper;

	@Autowired
	private ItemTestHelper itemTestHelper;
	
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private StockMovementRepository stockMovementRepository;

	@BeforeEach
	private void beforeEach() {
		stockMovementRepository.deleteAll();
		itemRepository.deleteAll();
	}

	@Test
	public void list() throws Exception {
		StockMovement stockMovement1 = stockMovementTestHelper.createStockMovement1();
		StockMovement stockMovement2 = stockMovementTestHelper.createStockMovement2();
		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].quantity", is(StockMovementTestHelper.STOCK_MOVIMENT_1_QUANTITY))) //
				.andExpect(jsonPath("$[0].item.itemId", is(stockMovement1.getItem().getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(StockMovementTestHelper.STOCK_MOVIMENT_2_QUANTITY))) //
				.andExpect(jsonPath("$[1].item.itemId", is(stockMovement2.getItem().getItemId().intValue()))) //
		);
	}

	@Test
	public void getOne() throws Exception {
		StockMovement stockMovement1 = stockMovementTestHelper.createStockMovement1();
		assertThat(mockMvc
				.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH + "/" + stockMovement1.getStockMovementId())) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.stockMovementId", is(stockMovement1.getStockMovementId().intValue()))) //
				.andExpect(jsonPath("$.quantity", is(StockMovementTestHelper.STOCK_MOVIMENT_1_QUANTITY))) //
				.andExpect(jsonPath("$.item.itemId", is(stockMovement1.getItem().getItemId().intValue()))) //
		);
	}

	@Test
	public void create() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		assertThat(mockMvc
				.perform(post(StockMovementTestHelper.STOCK_MOVEMENT_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(stockMovementTestHelper.newStockMovement1(item1.getItemId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.stockMovementId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(StockMovementTestHelper.STOCK_MOVIMENT_1_QUANTITY))) //
				.andExpect(jsonPath("$.operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
		);
	}

	@Test
	public void createWithEmptyQuantity() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		assertThat(mockMvc
				.perform(post(StockMovementTestHelper.STOCK_MOVEMENT_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new StockMovementCreateDTO(null, item1.getItemId()))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void createWithEmptyItem() throws Exception {
		assertThat(mockMvc
				.perform(post(StockMovementTestHelper.STOCK_MOVEMENT_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new StockMovementCreateDTO(StockMovementTestHelper.STOCK_MOVIMENT_1_QUANTITY, null))))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void update() throws Exception {
		StockMovement stockMovement1 = stockMovementTestHelper.createStockMovement1();
		assertThat(mockMvc
				.perform(put(StockMovementTestHelper.STOCK_MOVEMENT_PATH + "/" + stockMovement1.getStockMovementId())
						.contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().is4xxClientError()) //
		);
	}

	@Test
	public void deleteStockMovement() throws Exception {
		StockMovement stockMovement1 = stockMovementTestHelper.createStockMovement1();
		assertThat(mockMvc
				.perform(
						delete(StockMovementTestHelper.STOCK_MOVEMENT_PATH + "/" + stockMovement1.getStockMovementId()))
				.andExpect(status().isOk()));
		assertThat(
				mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH))
						.andExpect(jsonPath("$", hasSize(2))) //
						.andExpect(jsonPath("$[1].quantity", is(StockMovementTestHelper.STOCK_MOVIMENT_1_QUANTITY))) //
						.andExpect(jsonPath("$[1].item.itemId", is(stockMovement1.getItem().getItemId().intValue()))) //
						.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
		);
	}

}
