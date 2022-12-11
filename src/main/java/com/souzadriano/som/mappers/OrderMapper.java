package com.souzadriano.som.mappers;

import org.springframework.stereotype.Component;

import com.souzadriano.som.entities.Order;
import com.souzadriano.som.jpaentities.OrderEntity;

@Component
public class OrderMapper extends Mapper<Order, OrderEntity> {

	private ItemMapper itemMapper;
	private UserMapper userMapper;
	
	public OrderMapper(ItemMapper itemMapper, UserMapper userMapper) {
		this.itemMapper = itemMapper;
		this.userMapper = userMapper;
	}
	
	@Override
	public Order toObject(OrderEntity entity) {
		return new Order(entity.getOrderId(), entity.getCreationDate(), entity.getQuantity(), itemMapper.toObject(entity.getItem()), userMapper.toObject(entity.getUser()), entity.getStatus());
	}

	@Override
	public OrderEntity toEntity(Order object) {
		return new OrderEntity(object.getOrderId(), object.getCreationDate(), object.getQuantity(), object.getStatus(), itemMapper.toEntity(object.getItem()), userMapper.toEntity(object.getUser()));
	}

}
