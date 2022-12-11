package com.souzadriano.som.controllers.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class OrderUpdateDTO implements Serializable {

	private static final long serialVersionUID = 1797181782915577609L;

	@NotNull
	private Long orderId;

	@NotNull
	private Integer quantity;

	public OrderUpdateDTO() {
		super();
	}

	public OrderUpdateDTO(@NotNull Long orderId, @NotNull Integer quantity) {
		super();
		this.orderId = orderId;
		this.quantity = quantity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

}
