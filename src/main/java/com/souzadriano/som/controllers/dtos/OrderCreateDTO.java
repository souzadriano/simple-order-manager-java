package com.souzadriano.som.controllers.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class OrderCreateDTO implements Serializable {

	private static final long serialVersionUID = 4718666837768252900L;

	@NotNull
	private Integer quantity;
	@NotNull
	private Long itemId;
	@NotNull
	private Long userId;

	public OrderCreateDTO() {
		super();
	}

	public OrderCreateDTO(@NotNull Integer quantity, @NotNull Long itemId, @NotNull Long userId) {
		super();
		this.quantity = quantity;
		this.itemId = itemId;
		this.userId = userId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Long getItemId() {
		return itemId;
	}

	public Long getUserId() {
		return userId;
	}

}
