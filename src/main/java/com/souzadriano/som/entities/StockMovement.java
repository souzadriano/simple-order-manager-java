package com.souzadriano.som.entities;

import java.io.Serializable;
import java.util.Date;

public class StockMovement implements Serializable {

	private static final long serialVersionUID = 1597508896145652127L;

	private Long stockMovementId;
	private Date creationDate;
	private Integer quantity;
	private Item item;
	private StockMovementOperation operation;

	public StockMovement() {
		super();
	}

	public StockMovement(Date creationDate, Integer quantity, Item item, StockMovementOperation operation) {
		super();
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
		this.operation = operation;
	}

	public StockMovement(Long stockMovementId, Date creationDate, Integer quantity, Item item,
			StockMovementOperation operation) {
		super();
		this.stockMovementId = stockMovementId;
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
		this.operation = operation;
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

	public StockMovementOperation getOperation() {
		return operation;
	}

}
