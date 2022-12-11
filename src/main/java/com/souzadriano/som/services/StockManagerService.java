package com.souzadriano.som.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.entities.StockMovementOperation;
import com.souzadriano.som.entities.StockRequest;
import com.souzadriano.som.jpaentities.OrderEntity;
import com.souzadriano.som.jpaentities.OrderStockMovementEntity;
import com.souzadriano.som.jpaentities.StockMovementEntity;
import com.souzadriano.som.repositories.OrderRepository;
import com.souzadriano.som.repositories.OrderStockMovementRepository;
import com.souzadriano.som.repositories.StockMovementRepository;

@Service
public class StockManagerService {

	private OrderRepository orderRepository;
	private StockMovementRepository stockMovementRepository;
	private OrderStockMovementRepository orderStockMovementRepository;
	private OrderStatusService orderStatusService;

	public StockManagerService(StockMovementRepository stockMovementRepository,
			OrderStockMovementRepository orderStockMovementRepository, OrderRepository orderRepository, OrderStatusService orderStatusService) {
		this.stockMovementRepository = stockMovementRepository;
		this.orderStockMovementRepository = orderStockMovementRepository;
		this.orderRepository = orderRepository;
		this.orderStatusService = orderStatusService;
	}

	public OrderStatus didAOrder(OrderEntity order) {

		Integer numberAddedStock = stockMovementRepository.sumAddedMovementsByItemId(order.getItem().getItemId());
		Integer numberSubtractStock = stockMovementRepository
				.sumSubtractedMovementsNotCanceledByItemId(order.getItem().getItemId());

		StockRequest stockRequest = new StockRequest(numberAddedStock, numberSubtractStock, order.getQuantity());
		if (stockRequest.getQuantityToSubtractFromStock() == 0) {
			return OrderStatus.PENDING_STOCK;
		}
		saveOrderMovementStockAndMovimentStock(order, stockRequest);

		return stockRequest.isFulfilled() ? OrderStatus.COMPLETED : OrderStatus.PENDING_STOCK;
	}

	private void saveOrderMovementStockAndMovimentStock(OrderEntity order, StockRequest stockRequest) {
		StockMovementEntity stockMovementToSave = new StockMovementEntity(new Date(),
				stockRequest.getQuantityToSubtractFromStock(), order.getItem(), StockMovementOperation.SUBTRACTED);

		StockMovementEntity stockMovementSaved = stockMovementRepository.save(stockMovementToSave);

		OrderStockMovementEntity orderStockMovementToSave = new OrderStockMovementEntity(new Date(), order,
				stockMovementSaved);

		orderStockMovementRepository.save(orderStockMovementToSave);
	}

	public void didAStockAddedMovement(StockMovementEntity stockMovementEntity) {
		Integer numberAddedStock = stockMovementRepository
				.sumAddedMovementsByItemId(stockMovementEntity.getItem().getItemId());
		Integer numberSubtractStock = stockMovementRepository
				.sumSubtractedMovementsNotCanceledByItemId(stockMovementEntity.getItem().getItemId());

		List<OrderEntity> ordersPendingStock = orderRepository
				.findAllByItemItemIdAndStatus(stockMovementEntity.getItem().getItemId(), OrderStatus.PENDING_STOCK);
		for (OrderEntity order : ordersPendingStock) {
			Integer quantity = order.getQuantity();
			Integer subtractedMovimentsStock = stockMovementRepository.sumSubtractedMovementsByOrderId(order.getOrderId());
			StockRequest stockRequest = new StockRequest(numberAddedStock, numberSubtractStock, quantity - subtractedMovimentsStock);
			if (stockRequest.getQuantityToSubtractFromStock() == 0) {
				break;
			}
			saveOrderMovementStockAndMovimentStock(order, stockRequest);
			OrderStatus orderStatus = stockRequest.isFulfilled() ? OrderStatus.COMPLETED : OrderStatus.PENDING_STOCK;
			orderStatusService.updateStatus(order, orderStatus);
			if (!stockRequest.isFulfilled()) {
				break;
			}
			numberSubtractStock += stockRequest.getQuantityToSubtractFromStock();
		}
	}

}
