package com.souzadriano.som.services;

import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.jpaentities.OrderEntity;
import com.souzadriano.som.repositories.OrderRepository;

@Service
public class OrderStatusService {

	private OrderRepository orderRepository;
	private EmailService emailService;

	public OrderStatusService(OrderRepository orderRepository, EmailService emailService) {
		this.orderRepository = orderRepository;
		this.emailService = emailService;
	}

	public OrderEntity updateStatus(OrderEntity orderEntity, OrderStatus status) {
		orderEntity.setStatus(status);
		OrderEntity orderSaved = orderRepository.save(orderEntity);
		if (OrderStatus.COMPLETED.equals(status)) {
			sendEmail(orderSaved);
		}
		return orderSaved;
	}

	private void sendEmail(OrderEntity order) {
		StringBuilder body = new StringBuilder();
		body.append("Dear " + order.getUser().getName());
		body.append("\n\n");
		body.append("The order ");
		body.append(order.getOrderId());
		body.append(" with ");
		body.append(order.getQuantity());
		body.append(" ");
		body.append(order.getItem().getName());
		body.append(" has been completed.\n\n");
		body.append("Best regards,\n");
		body.append("Simple Order Manager");
		emailService.send(order.getUser().getEmail(), "Order " + order.getOrderId() + " is completed",
				body.toString());
	}
}
