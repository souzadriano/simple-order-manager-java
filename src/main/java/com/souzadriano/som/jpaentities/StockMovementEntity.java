package com.souzadriano.som.jpaentities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.souzadriano.som.entities.StockMovementOperation;

@Entity
@Table(name = "stock_movement")
public class StockMovementEntity implements Serializable {

	private static final long serialVersionUID = 4797082171030116501L;

	@Id
	@GeneratedValue(generator = "stock_movement_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "stock_movement_seq", sequenceName = "stock_movement_seq", allocationSize = 1)
	@Column(name = "stock_movement_id")
	private Long stockMovementId;

	@NotNull
	@Column(name = "creation_date")
	private Date creationDate;

	@NotNull
	private Integer quantity;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private ItemEntity item;

	@NotNull
	@Enumerated(EnumType.STRING)
	private StockMovementOperation operation;

	public StockMovementEntity() {
		super();
	}

	public StockMovementEntity(@NotNull Date creationDate, @NotNull Integer quantity, @NotNull ItemEntity item,
			@NotNull StockMovementOperation operation) {
		super();
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
		this.operation = operation;
	}

	public StockMovementEntity(Long stockMovementId, @NotNull Date creationDate, @NotNull Integer quantity,
			@NotNull ItemEntity item, @NotNull StockMovementOperation operation) {
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

	public void setStockMovementId(Long stockMovementId) {
		this.stockMovementId = stockMovementId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}

	public StockMovementOperation getOperation() {
		return operation;
	}

	public void setOperation(StockMovementOperation operation) {
		this.operation = operation;
	}

}
