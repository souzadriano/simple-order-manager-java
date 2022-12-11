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
import com.souzadriano.som.controllers.dtos.StockMovementCreateDTO;
import com.souzadriano.som.entities.Item;
import com.souzadriano.som.entities.StockMovement;

@Component
public class StockMovementTestHelper {

	public static final Integer STOCK_MOVIMENT_1_QUANTITY = 10;

	public static final Integer STOCK_MOVIMENT_2_QUANTITY = 20;

	public static final String STOCK_MOVEMENT_PATH = "/stock-movements";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ItemTestHelper itemTestHelper;

	public StockMovement createStockMovement(StockMovementCreateDTO stockMovement)
			throws UnsupportedEncodingException, Exception, JsonProcessingException, JsonMappingException {
		String content = mockMvc
				.perform(post(STOCK_MOVEMENT_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(stockMovement)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(content, StockMovement.class);
	}

	public StockMovement createStockMovement2() throws Exception, JsonProcessingException {
		Item item = itemTestHelper.createItem2();
		return createStockMovement(newStockMovement2(item.getItemId()));
	}

	public StockMovement createStockMovement1() throws Exception, JsonProcessingException {
		Item item = itemTestHelper.createItem1();
		return createStockMovement(newStockMovement1(item.getItemId()));
	}

	public StockMovementCreateDTO newStockMovement1(Long itemId) {
		return new StockMovementCreateDTO(STOCK_MOVIMENT_1_QUANTITY, itemId);
	}

	public StockMovementCreateDTO newStockMovement2(Long itemId) {
		return new StockMovementCreateDTO(STOCK_MOVIMENT_2_QUANTITY, itemId);
	}

}
