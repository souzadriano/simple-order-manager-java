package com.souzadriano.som.services;

import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.jpaentities.OrderEntity;
import com.souzadriano.som.repositories.OrderRepository;

@Service
public class OrderStatusService {

	private OrderRepository orderRepository;
	
	public OrderStatusService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	public OrderEntity updateStatus(OrderEntity orderEntity, OrderStatus status) {
		orderEntity.setStatus(status);
		return orderRepository.save(orderEntity);
	}
}
