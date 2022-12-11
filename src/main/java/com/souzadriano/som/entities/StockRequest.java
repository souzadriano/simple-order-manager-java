package com.souzadriano.som.entities;

public class StockRequest {

	private Integer added;
	private Integer subtracted;
	private Integer possibleRequest = 0;
	private Integer request = 0;

	public StockRequest(Integer numberAdded, Integer numberSubtracted, Integer request) {
		super();
		this.added = numberAdded;
		this.subtracted = numberSubtracted;
		this.request = request;
		this.possibleRequest = getNumberInStock() < request ? getNumberInStock() : request;
	}

	private Integer getNumberInStock() {
		if (added - subtracted < 0) {
			return 0;
		}
		return added - subtracted;
	}

	public boolean isFulfilled() {
		return this.possibleRequest == this.request; 
	}
	
	public Integer getQuantityToSubtractFromStock() {
		return this.possibleRequest;
	}
}
