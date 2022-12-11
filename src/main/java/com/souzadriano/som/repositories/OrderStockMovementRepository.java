package com.souzadriano.som.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.OrderStockMovementEntity;

public interface OrderStockMovementRepository extends JpaRepository<OrderStockMovementEntity, Long> {

}
