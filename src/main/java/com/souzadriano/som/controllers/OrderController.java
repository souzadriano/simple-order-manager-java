package com.souzadriano.som.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.souzadriano.som.controllers.dtos.OrderCreateDTO;
import com.souzadriano.som.entities.Order;
import com.souzadriano.som.services.OrderService;

@RestController
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/orders")
	public List<Order> findAll() {
		return orderService.findAll();
	}

	@PostMapping("/orders")
	public Order create(@Valid @RequestBody OrderCreateDTO order) {
		return orderService.create(order.getQuantity(), order.getItemId(), order.getUserId());
	}

	@GetMapping("/orders/{orderId}")
	public Order findById(@PathVariable Long orderId) {
		return orderService.findOne(orderId);
	}

	@DeleteMapping("/orders/{orderId}")
	public Order cancelOrder(@PathVariable Long orderId) {
		return orderService.cancelOrder(orderId);
	}
}
