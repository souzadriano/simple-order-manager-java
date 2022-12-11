package com.souzadriano.som.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

	public List<ItemEntity> findByDisabled(Boolean disabled, Sort sort);
	
	public Optional<ItemEntity> findOneByItemIdAndDisabled(Long itemId, Boolean disabled);
}
