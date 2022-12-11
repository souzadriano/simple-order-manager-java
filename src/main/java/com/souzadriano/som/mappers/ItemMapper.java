package com.souzadriano.som.mappers;

import org.springframework.stereotype.Component;

import com.souzadriano.som.entities.Item;
import com.souzadriano.som.jpaentities.ItemEntity;

@Component
public class ItemMapper extends Mapper<Item, ItemEntity> {

	@Override
	public Item toObject(ItemEntity entity) {
		return new Item(entity.getItemId(), entity.getName());
	}

	@Override
	public ItemEntity toEntity(Item object) {
		return new ItemEntity(object.getItemId(), object.getName(), Boolean.FALSE);
	}

}
