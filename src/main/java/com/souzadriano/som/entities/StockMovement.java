package com.souzadriano.som.entities;

import java.io.Serializable;
import java.util.Date;

public class StockMovement implements Serializable {

	private static final long serialVersionUID = 6712921818996046850L;

	private Long stockMovementId;
	private Date creationDate;
	private Integer quantity;
	private Item item;

	public StockMovement() {
		super();
	}

	public StockMovement(Date creationDate, Integer quantity, Item item) {
		super();
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
	}

	public StockMovement(Long stockMovementId, Date creationDate, Integer quantity, Item item) {
		super();
		this.stockMovementId = stockMovementId;
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
	}

	public Long getStockMovementId() {
		return stockMovementId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Item getItem() {
		return item;
	}

}
