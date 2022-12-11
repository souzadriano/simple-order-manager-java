package com.souzadriano.som;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.entities.StockMovementOperation;
import com.souzadriano.som.entities.User;
import com.souzadriano.som.helpers.ItemTestHelper;
import com.souzadriano.som.helpers.OrderTestHelper;
import com.souzadriano.som.helpers.StockMovementTestHelper;
import com.souzadriano.som.helpers.UserTestHelper;
import com.souzadriano.som.repositories.ItemRepository;
import com.souzadriano.som.repositories.OrderRepository;
import com.souzadriano.som.repositories.OrderStockMovementRepository;
import com.souzadriano.som.repositories.StockMovementRepository;
import com.souzadriano.som.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class StockManagerTest {

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
	private StockMovementTestHelper stockMovementTestHelper;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private StockMovementRepository stockMovementRepository;

	@Autowired
	private OrderStockMovementRepository orderStockMovementRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	private void beforeEach() {
		orderStockMovementRepository.deleteAll();
		stockMovementRepository.deleteAll();
		orderRepository.deleteAll();
		itemRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void createAOrderWithoutStock() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);
	}

	@Test
	public void createAOrderWithStock() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();

		int stockQuantityCreated = OrderTestHelper.ORDER_1_QUANTITY;
		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated, item1.getItemId()));

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);

		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item1.getItemId().intValue()))) //
		);
	}

	@Test
	public void createAOrderWithHalfInStock() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		int stockQuantityCreated = OrderTestHelper.ORDER_1_QUANTITY / 2;
		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated, item1.getItemId()));

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);
		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item1.getItemId().intValue()))) //
		);
	}

	@Test
	public void createAOrderWithHalfInStockAndAfterCreateAStockWithAnotherHalf() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		int stockQuantityCreated = OrderTestHelper.ORDER_1_QUANTITY / 2;
		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated, item1.getItemId()));

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);
		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item1.getItemId().intValue()))) //
		);

		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated, item1.getItemId()));
		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(4))) //
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[2].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[2].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[2].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[3].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[3].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[3].item.itemId", is(item1.getItemId().intValue()))) //
		);
	}

	@Test
	public void createMultipleOrdersWithDoubleMinusOneInStock() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		User user1 = userTestHelper.createUser1();
		int stockQuantityCreated = (OrderTestHelper.ORDER_1_QUANTITY * 2) - 1;
		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated, item1.getItemId()));

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);

		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(3))) //
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[1].quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$[2].quantity", is(OrderTestHelper.ORDER_1_QUANTITY - 1))) //
				.andExpect(jsonPath("$[2].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[2].item.itemId", is(item1.getItemId().intValue()))) //
		);
	}

	@Test
	public void createMultipleOrdersWithDoubleMinusOneInStockAndMultiplesItems() throws Exception {
		Item item1 = itemTestHelper.createItem1();
		Item item2 = itemTestHelper.createItem2();
		User user1 = userTestHelper.createUser1();
		User user2 = userTestHelper.createUser2();

		int stockQuantityCreated1 = (OrderTestHelper.ORDER_1_QUANTITY * 2) - 1;
		int stockQuantityCreated2 = (OrderTestHelper.ORDER_2_QUANTITY * 2) - 1;

		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated1, item1.getItemId()));
		stockMovementTestHelper
				.createStockMovement(new StockMovementCreateDTO(stockQuantityCreated2, item2.getItemId()));

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder2(item2.getItemId(), user2.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_2_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item2.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user2.getUserId().intValue()))) //
		);

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder1(item1.getItemId(), user1.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item1.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user1.getUserId().intValue()))) //
		);

		assertThat(mockMvc
				.perform(post(OrderTestHelper.ORDER_PATH).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper
								.writeValueAsString(orderTestHelper.newOrder2(item2.getItemId(), user2.getUserId()))))
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.orderId", isA(Number.class))) //
				.andExpect(jsonPath("$.quantity", is(OrderTestHelper.ORDER_2_QUANTITY))) //
				.andExpect(jsonPath("$.status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$.item.itemId", is(item2.getItemId().intValue()))) //
				.andExpect(jsonPath("$.user.userId", is(user2.getUserId().intValue()))) //
		);

		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(6))) //
				//
				.andExpect(jsonPath("$[0].quantity", is(stockQuantityCreated1))) //
				.andExpect(jsonPath("$[0].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[0].item.itemId", is(item1.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[1].quantity", is(stockQuantityCreated2))) //
				.andExpect(jsonPath("$[1].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[1].item.itemId", is(item2.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[2].quantity", is(OrderTestHelper.ORDER_1_QUANTITY))) //
				.andExpect(jsonPath("$[2].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[2].item.itemId", is(item1.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[3].quantity", is(OrderTestHelper.ORDER_2_QUANTITY))) //
				.andExpect(jsonPath("$[3].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[3].item.itemId", is(item2.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[4].quantity", is(OrderTestHelper.ORDER_1_QUANTITY - 1))) //
				.andExpect(jsonPath("$[4].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[4].item.itemId", is(item1.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[5].quantity", is(OrderTestHelper.ORDER_2_QUANTITY - 1))) //
				.andExpect(jsonPath("$[5].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[5].item.itemId", is(item2.getItemId().intValue()))) //
		);
		
		assertThat(mockMvc.perform(get(OrderTestHelper.ORDER_PATH)) //
				.andExpect(jsonPath("$", hasSize(4))) //
				.andExpect(jsonPath("$[0].status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$[1].status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$[2].status", is(OrderStatus.PENDING_STOCK.toString()))) //
				.andExpect(jsonPath("$[3].status", is(OrderStatus.PENDING_STOCK.toString()))) //
		);

		stockMovementTestHelper.createStockMovement(new StockMovementCreateDTO(2, item1.getItemId()));
		stockMovementTestHelper.createStockMovement(new StockMovementCreateDTO(2, item2.getItemId()));

		assertThat(mockMvc.perform(get(StockMovementTestHelper.STOCK_MOVEMENT_PATH)) //
				.andExpect(jsonPath("$", hasSize(10))) //
				//
				.andExpect(jsonPath("$[6].quantity", is(2))) //
				.andExpect(jsonPath("$[6].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[6].item.itemId", is(item1.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[7].quantity", is(1))) //
				.andExpect(jsonPath("$[7].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[7].item.itemId", is(item1.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[8].quantity", is(2))) //
				.andExpect(jsonPath("$[8].operation", is(StockMovementOperation.ADDED.toString()))) //
				.andExpect(jsonPath("$[8].item.itemId", is(item2.getItemId().intValue()))) //
				//
				.andExpect(jsonPath("$[9].quantity", is(1))) //
				.andExpect(jsonPath("$[9].operation", is(StockMovementOperation.SUBTRACTED.toString()))) //
				.andExpect(jsonPath("$[9].item.itemId", is(item2.getItemId().intValue()))) //
		);
		
		assertThat(mockMvc.perform(get(OrderTestHelper.ORDER_PATH)) //
				.andExpect(jsonPath("$", hasSize(4))) //
				.andExpect(jsonPath("$[0].status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$[1].status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$[2].status", is(OrderStatus.COMPLETED.toString()))) //
				.andExpect(jsonPath("$[3].status", is(OrderStatus.COMPLETED.toString()))) //
		);

	}
}
