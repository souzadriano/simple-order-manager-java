package com.souzadriano.som.jpaentities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_stock_movement")
public class OrderStockMovementEntity implements Serializable {

	private static final long serialVersionUID = 3113971581609175407L;

	@Id
	@GeneratedValue(generator = "order_stock_movement_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "order_stock_movement_seq", sequenceName = "order_stock_movement_seq", allocationSize = 1)
	@Column(name = "order_stock_movement_id")
	private Long orderStockMovementId;

	@NotNull
	@Column(name = "creation_date")
	private Date creationDate;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_movement_id", nullable = false)
	private StockMovementEntity stockMovement;

	public OrderStockMovementEntity() {
		super();
	}

	public OrderStockMovementEntity(Date creationDate, OrderEntity order, StockMovementEntity stockMovement) {
		super();
		this.creationDate = creationDate;
		this.order = order;
		this.stockMovement = stockMovement;
	}

	public Long getOrderStockMovementId() {
		return orderStockMovementId;
	}

	public void setOrderStockMovementId(Long orderStockMovementId) {
		this.orderStockMovementId = orderStockMovementId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public StockMovementEntity getStockMovement() {
		return stockMovement;
	}

	public void setStockMovement(StockMovementEntity stockMovement) {
		this.stockMovement = stockMovement;
	}

}
