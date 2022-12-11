package com.souzadriano.som.mappers;

import org.springframework.stereotype.Component;

import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.jpaentities.StockMovementEntity;

@Component
public class StockMovementMapper extends Mapper<StockMovement, StockMovementEntity> {

	private ItemMapper itemMapper;

	public StockMovementMapper(ItemMapper itemMapper) {
		this.itemMapper = itemMapper;
	}

	@Override
	public StockMovement toObject(StockMovementEntity entity) {
		return new StockMovement(entity.getStockMovementId(), entity.getCreationDate(), entity.getQuantity(),
				itemMapper.toObject(entity.getItem()), entity.getOperation());
	}

	@Override
	public StockMovementEntity toEntity(StockMovement object) {
		return new StockMovementEntity(object.getStockMovementId(), object.getCreationDate(), object.getQuantity(),
				itemMapper.toEntity(object.getItem()), object.getOperation());
	}

}
