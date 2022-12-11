package com.souzadriano.som.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.entities.StockMovementOperation;
import com.souzadriano.som.jpaentities.ItemEntity;
import com.souzadriano.som.jpaentities.StockMovementEntity;
import com.souzadriano.som.mappers.StockMovementMapper;
import com.souzadriano.som.repositories.ItemRepository;
import com.souzadriano.som.repositories.StockMovementRepository;

@Service
public class StockMovementService {

	private StockMovementRepository stockMovementRepository;
	private StockMovementMapper stockMovementMapper;
	private ItemRepository itemRepository;
	private StockManagerService stockManagerService;

	public StockMovementService(StockMovementRepository stockMovementRepository,
			StockMovementMapper stockMovementMapper, ItemRepository itemRepository,
			StockManagerService stockManagerService) {
		this.stockMovementRepository = stockMovementRepository;
		this.stockMovementMapper = stockMovementMapper;
		this.itemRepository = itemRepository;
		this.stockManagerService  = stockManagerService;
	}

	public List<StockMovement> findAll() {
		return stockMovementMapper.toObject(this.stockMovementRepository.findAll(Sort.by("creationDate")));
	}

	public StockMovement create(Integer quantity, Long itemId, StockMovementOperation operation) {
		ItemEntity itemEntity = itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		StockMovementEntity stockMovementToSave = new StockMovementEntity(new Date(), quantity, itemEntity, operation);
		StockMovementEntity stockMovementSaved = stockMovementRepository.save(stockMovementToSave);
		if (StockMovementOperation.ADDED.equals(operation)) {
			stockManagerService.didAStockAddedMovement(stockMovementSaved);
		}
		return stockMovementMapper.toObject(stockMovementSaved);
	}

	public StockMovement findOne(Long stockMovementId) {
		StockMovementEntity entity = this.stockMovementRepository.findById(stockMovementId)
				.orElseThrow(IllegalArgumentException::new);
		return stockMovementMapper.toObject(entity);
	}

	public StockMovement delete(Long stockMovementId) {
		StockMovementEntity entity = this.stockMovementRepository.findById(stockMovementId)
				.orElseThrow(IllegalArgumentException::new);
		StockMovementEntity entityToDelete = new StockMovementEntity(new Date(), entity.getQuantity(), entity.getItem(),
				StockMovementOperation.SUBTRACTED);
		return stockMovementMapper.toObject(stockMovementRepository.save(entityToDelete));
	}
}
