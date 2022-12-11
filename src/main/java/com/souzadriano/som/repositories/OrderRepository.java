package com.souzadriano.som.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.jpaentities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	public List<OrderEntity> findAllByItemItemIdAndStatus(Long itemId, OrderStatus pendingStock);

}
