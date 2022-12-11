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

import com.souzadriano.som.entities.OrderStatus;

@Entity
@Table(name = "som_order")
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = -3717932774694215056L;

	@Id
	@GeneratedValue(generator = "order_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
	@Column(name = "order_id")
	private Long orderId;

	@NotNull
	@Column(name = "creation_date")
	private Date creationDate;

	@NotNull
	private Integer quantity;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private ItemEntity item;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public OrderEntity() {
		super();
	}

	public OrderEntity(@NotNull Date creationDate, @NotNull Integer quantity, @NotNull OrderStatus status,
			@NotNull ItemEntity item, @NotNull UserEntity user) {
		super();
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.status = status;
		this.item = item;
		this.user = user;
	}

	public OrderEntity(Long orderId, @NotNull Date creationDate, @NotNull Integer quantity, @NotNull OrderStatus status,
			@NotNull ItemEntity item, @NotNull UserEntity user) {
		super();
		this.orderId = orderId;
		this.creationDate = creationDate;
		this.quantity = quantity;
		this.status = status;
		this.item = item;
		this.user = user;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
