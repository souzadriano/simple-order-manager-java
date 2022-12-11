package com.souzadriano.som.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzadriano.som.jpaentities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
