package com.souzadriano.som.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

	public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ItemRepository itemRepository,
			UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.orderMapper = orderMapper;
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	public List<Order> findAll() {
		return orderMapper.toObject(this.orderRepository.findAll(Sort.by("creationDate")));
	}

	public Order create(Integer quantity, Long itemId, Long userId) {
		ItemEntity itemEntity = itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		UserEntity userEntity = userRepository.findOneByUserIdAndDisabled(userId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		OrderEntity entity = new OrderEntity(new Date(), quantity, OrderStatus.PENDING_STOCK, itemEntity, userEntity);
		return orderMapper.toObject(orderRepository.save(entity));
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