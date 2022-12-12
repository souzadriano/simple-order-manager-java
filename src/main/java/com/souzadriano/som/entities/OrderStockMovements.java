package com.souzadriano.som.entities;

import java.io.Serializable;
import java.util.List;

public class OrderStockMovements implements Serializable {

	private static final long serialVersionUID = 5335835970257636840L;

	private Order order;
	private List<StockMovement> stockMovements;

	public OrderStockMovements() {
		super();
	}

	public OrderStockMovements(Order order, List<StockMovement> stockMovements) {
		super();
		this.order = order;
		this.stockMovements = stockMovements;
	}

	public Order getOrder() {
		return order;
	}

	public List<StockMovement> getStockMovements() {
		return stockMovements;
	}

}
