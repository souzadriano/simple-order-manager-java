package com.souzadriano.som.controllers.dtos;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StockMovementCreateDTO implements Serializable {

	private static final long serialVersionUID = -4913343832928776890L;

	@NotNull
	@Min(1)
	private Integer quantity;

	@NotNull
	private Long itemId;

	public StockMovementCreateDTO() {
		super();
	}

	public StockMovementCreateDTO(@NotNull Integer quantity, @NotNull Long itemId) {
		super();
		this.quantity = quantity;
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Long getItemId() {
		return itemId;
	}

}
