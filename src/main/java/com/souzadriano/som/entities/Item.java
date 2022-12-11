package com.souzadriano.som.entities;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = -209989674594206806L;

	private Long itemId;
	private String name;

	public Item() {
		super();
	}

	public Item(String name) {
		super();
		this.name = name;
	}

	public Item(Long itemId, String name) {
		super();
		this.itemId = itemId;
		this.name = name;
	}

	public Long getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}

}
