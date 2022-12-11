package com.souzadriano.som.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.souzadriano.som.entities.Order;
import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.jpaentities.ItemEntity;
import com.souzadriano.som.jpaentities.OrderEntity;
import com.souzadriano.som.jpaentities.UserEntity;
import com.souzadriano.som.mappers.OrderMapper;
import com.souzadriano.som.repositories.ItemRepository;
import com.souzadriano.som.repositories.OrderRepository;
import com.souzadriano.som.repositories.UserRepository;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private OrderMapper orderMapper;
	private ItemRepository itemRepository;
	private UserRepository userRepository;
	private StockManagerService stockManagerService;
	private OrderStatusService orderStatusService;

	public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ItemRepository itemRepository,
			UserRepository userRepository, StockManagerService stockManagerService, OrderStatusService orderStatusService) {
		this.orderRepository = orderRepository;
		this.orderMapper = orderMapper;
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.stockManagerService = stockManagerService;
		this.orderStatusService = orderStatusService;
	}

	public List<Order> findAll() {
		return orderMapper.toObject(this.orderRepository.findAll(Sort.by("creationDate")));
	}

	@Transactional
	public Order create(Integer quantity, Long itemId, Long userId) {
		ItemEntity itemEntity = itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		UserEntity userEntity = userRepository.findOneByUserIdAndDisabled(userId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		OrderEntity entity = new OrderEntity(new Date(), quantity, OrderStatus.PENDING, itemEntity, userEntity);
		OrderEntity orderSaved = orderRepository.save(entity);
		OrderStatus status = stockManagerService.didAOrder(orderSaved);
		return orderMapper.toObject(orderStatusService.updateStatus(orderSaved, status));
	}

	public Order findOne(Long orderId) {
		OrderEntity entity = this.orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
		return orderMapper.toObject(entity);
	}

	public Order cancelOrder(Long orderId) {
		OrderEntity entity = this.orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
		entity.setStatus(OrderStatus.CANCELED);
		return orderMapper.toObject(orderRepository.save(entity));
	}
}
