package com.souzadriano.som.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.StockMovementEntity;

public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Long> {

}
