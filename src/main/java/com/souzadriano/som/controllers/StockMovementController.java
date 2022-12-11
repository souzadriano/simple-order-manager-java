package com.souzadriano.som.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.souzadriano.som.controllers.dtos.StockMovementCreateDTO;
import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.entities.StockMovementOperation;
import com.souzadriano.som.services.StockMovementService;

@RestController
public class StockMovementController {

	private StockMovementService stockMovementService;

	public StockMovementController(StockMovementService stockMovementService) {
		this.stockMovementService = stockMovementService;
	}
	
	@GetMapping("/stock-movements")
	public List<StockMovement> findAll() {
		return stockMovementService.findAll();
	}

	@PostMapping("/stock-movements")
	public StockMovement create(@Valid @RequestBody StockMovementCreateDTO stockMovement) {
		return stockMovementService.create(stockMovement.getQuantity(), stockMovement.getItemId(),
				StockMovementOperation.ADDED);
	}
	
	@GetMapping("/stock-movements/{stockMovementId}")
	public StockMovement findOne(@PathVariable Long stockMovementId) {
		return stockMovementService.findOne(stockMovementId);
	}
	
	@DeleteMapping("/stock-movements/{stockMovementId}")
	public StockMovement delete(@PathVariable Long stockMovementId) {
		return stockMovementService.delete(stockMovementId);
	}
}
