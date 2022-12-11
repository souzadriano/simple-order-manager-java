package com.souzadriano.som.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.jpaentities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	public List<OrderEntity> findAllByItemItemIdAndStatus(Long itemId, OrderStatus pendingStock);

	@Query("SELECT o FROM OrderStockMovementEntity osm JOIN osm.order o WHERE osm.stockMovement.stockMovementId = :stockMovementId")
	public List<OrderEntity> findAllByStockMovementId(Long stockMovementId);

	public List<OrderEntity> findAllByStatus(OrderStatus completed);
}
