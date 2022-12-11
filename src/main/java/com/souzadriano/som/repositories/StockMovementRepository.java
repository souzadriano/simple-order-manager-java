package com.souzadriano.som.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.souzadriano.som.jpaentities.StockMovementEntity;

public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Long> {

	@Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovementEntity sm WHERE sm.operation = 'ADDED' AND sm.item.id = :itemId ")
	public Integer sumAddedMovementsByItemId(Long itemId);

	@Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM OrderStockMovementEntity osm RIGHT JOIN osm.stockMovement sm LEFT JOIN osm.order o WHERE sm.operation = 'SUBTRACTED' AND sm.item.id = :itemId and (o.status IS NULL OR o.status <> 'CANCELED')")
	public Integer sumSubtractedMovementsNotCanceledByItemId(Long itemId);

	@Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM OrderStockMovementEntity osm JOIN osm.stockMovement sm WHERE sm.operation = 'SUBTRACTED' AND osm.order.orderId = :orderId")
	public Integer sumSubtractedMovementsByOrderId(Long orderId);

	@Query("SELECT sm FROM OrderStockMovementEntity osm JOIN osm.stockMovement sm WHERE osm.order.orderId = :orderId")
	public List<StockMovementEntity> findAllByOrderId(Long orderId);
}
