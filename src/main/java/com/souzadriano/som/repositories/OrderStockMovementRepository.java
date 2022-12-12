package com.souzadriano.som.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.souzadriano.som.jpaentities.OrderStockMovementEntity;

public interface OrderStockMovementRepository extends JpaRepository<OrderStockMovementEntity, Long> {

	
	@Query("FROM OrderStockMovementEntity osm JOIN FETCH osm.order o JOIN FETCH osm.stockMovement sm JOIN FETCH o.user JOIN FETCH o.item JOIN FETCH sm.item ")
	public List<OrderStockMovementEntity> findAllJoinFech();
}
