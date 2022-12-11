package com.souzadriano.som.jpaentities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item")
public class ItemEntity implements Serializable {

	private static final long serialVersionUID = -8623425263918596756L;

	@Id
	@GeneratedValue(generator = "item_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "item_seq", sequenceName = "item_seq", allocationSize = 1)
	@Column(name = "item_id")
	private Long itemId;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	private Boolean disabled;

	public ItemEntity() {
		super();
	}

	public ItemEntity(Long itemId) {
		super();
		this.itemId = itemId;
	}

	public ItemEntity(String name, Boolean disabled) {
		super();
		this.name = name;
		this.disabled = disabled;
	}

	public ItemEntity(Long itemId, String name, Boolean disabled) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.disabled = disabled;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
