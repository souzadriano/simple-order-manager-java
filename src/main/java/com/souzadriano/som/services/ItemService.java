package com.souzadriano.som.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.Item;
import com.souzadriano.som.jpaentities.ItemEntity;
import com.souzadriano.som.mappers.ItemMapper;
import com.souzadriano.som.repositories.ItemRepository;

@Service
public class ItemService {

	private ItemRepository itemRepository;
	private ItemMapper itemMapper;

	public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
	}

	public List<Item> findAll() {
		return itemMapper.toObject(itemRepository.findByDisabled(Boolean.FALSE, Sort.by("name")));
	}

	public Item create(Item item) {
		ItemEntity entity = itemMapper.toEntity(item);
		entity.setItemId(null);
		return itemMapper.toObject(itemRepository.save(entity));
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE)
				.map(entity -> itemMapper.toObject(entity)).orElseThrow(IllegalArgumentException::new);
	}

	public Item update(Long itemId, Item item) {
		itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE).map(entity -> itemMapper.toObject(entity))
				.orElseThrow(IllegalArgumentException::new);
		ItemEntity entity = itemMapper.toEntity(item);
		return itemMapper.toObject(itemRepository.save(entity));
	}

	public void delete(Long itemId) {
		ItemEntity itemEntity = itemRepository.findOneByItemIdAndDisabled(itemId, Boolean.FALSE)
				.orElseThrow(IllegalArgumentException::new);
		itemEntity.setDisabled(Boolean.TRUE);
		itemRepository.save(itemEntity);
	}

}
