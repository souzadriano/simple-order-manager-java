package com.souzadriano.som.entities;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = -7614905589181851122L;

	private Long orderId;
	private Date creationDate;
	private Integer quantity;
	private Item item;
	private User user;

	public Order() {
		super();
	}

	public Order(Date creationDate, Integer quantity, Item item, User user) {
		super();
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
		this.user = user;
	}

	public Order(Long orderId, Date creationDate, Integer quantity, Item item, User user) {
		super();
		this.orderId = orderId;
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.item = item;
		this.user = user;
	}

	public Long getOrderId() {
		return orderId;
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

	public User getUser() {
		return user;
	}

}
