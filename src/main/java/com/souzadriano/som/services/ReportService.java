package com.souzadriano.som.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.souzadriano.som.entities.Order;
import com.souzadriano.som.entities.OrderStatus;
import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.jpaentities.EmailLogEntity;
import com.souzadriano.som.jpaentities.OrderEntity;
import com.souzadriano.som.jpaentities.StockMovementEntity;
import com.souzadriano.som.mappers.OrderMapper;
import com.souzadriano.som.mappers.StockMovementMapper;
import com.souzadriano.som.repositories.EmailLogRepository;
import com.souzadriano.som.repositories.OrderRepository;
import com.souzadriano.som.repositories.StockMovementRepository;

@Service
public class ReportService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

	private StockMovementRepository stockMovementRepository;
	private StockMovementMapper stockMovementMapper;
	private OrderRepository orderRepository;
	private OrderMapper orderMapper;
	private EmailLogRepository emailLogRepository;

	public ReportService(StockMovementRepository stockMovementRepository, StockMovementMapper stockMovementMapper,
			OrderRepository orderRepository, OrderMapper orderMapper, EmailLogRepository emailLogRepository) {
		this.stockMovementRepository = stockMovementRepository;
		this.stockMovementMapper = stockMovementMapper;
		this.orderRepository = orderRepository;
		this.orderMapper = orderMapper;
		this.emailLogRepository = emailLogRepository;
	}

	public List<StockMovement> reportStockMovementsByOrderId(Long orderId) {
		return stockMovementMapper.toObject(stockMovementRepository.findAllByOrderId(orderId));
	}

	public List<Order> reportOrdersByStockMovementId(Long stockMovementId) {
		return orderMapper.toObject(orderRepository.findAllByStockMovementId(stockMovementId));
	}

	public byte[] writeLogWithOrdersCompletedAndStockMovementsAndEmailLogs() throws IOException {
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");

		List<OrderEntity> orders = orderRepository.findAllByStatus(OrderStatus.COMPLETED);
		StringBuilder sb = new StringBuilder();
		sb.append("ORDERS:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append("Order ID\t");
		sb.append("Creation Date\t");
		sb.append("Quantity\t");
		sb.append("Item\t");
		sb.append("User");
		sb.append(System.lineSeparator());

		orders.forEach(order -> {
			sb.append(order.getOrderId());
			sb.append("\t");
			sb.append(df.format(order.getCreationDate()));
			sb.append("\t");
			sb.append(order.getQuantity());
			sb.append("\t");
			sb.append(order.getItem().getName());
			sb.append("\t");
			sb.append(order.getUser().getName());
			sb.append(System.lineSeparator());
		});
		
		
		List<StockMovementEntity> stockMovements = stockMovementRepository.findAll(Sort.by("creationDate"));
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("STOCK MOVEMENTS:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Stock Movement ID\t");
		sb.append("Creation Date\t");
		sb.append("Quantity\t");
		sb.append("Item\t");
		sb.append("Operation");
		sb.append(System.lineSeparator());
		
		stockMovements.forEach(movement -> {
			sb.append(movement.getStockMovementId());
			sb.append("\t");
			sb.append(df.format(movement.getCreationDate()));
			sb.append("\t");
			sb.append(movement.getQuantity());
			sb.append("\t");
			sb.append(movement.getItem().getName());
			sb.append("\t");
			sb.append(movement.getOperation().name());
			sb.append(System.lineSeparator());
		});
		
		List<EmailLogEntity> emailLogs = emailLogRepository.findAll(Sort.by("creationDate"));
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("E-MAIL LOGS:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("E-Mail Log ID\t");
		sb.append("Creation Date\t");
		sb.append("Status\t");
		sb.append("To\t");
		sb.append("Subject\t");
		sb.append("Text\t");
		sb.append("Log");
		sb.append(System.lineSeparator());
		
		emailLogs.forEach(log -> {
			sb.append(log.getEmailLogId());
			sb.append("\t");
			sb.append(df.format(log.getCreationDate()));
			sb.append("\t");
			sb.append(log.getStatus().name());
			sb.append("\t");
			sb.append(log.getTo());
			sb.append("\t");
			sb.append(log.getSubject());
			sb.append("\t");
			sb.append(log.getBody().replace("\n", " "));
			sb.append("\t");
			sb.append(log.getLog().replace("\n", " "));
			sb.append(System.lineSeparator());
		});
		
		String fileName = System.getProperty("java.io.tmpdir") + "/SimpleOrderManager-"+UUID.randomUUID().toString()+".txt";
		
		Files.write(Paths.get(fileName),
				sb.toString().getBytes());
		LOG.info("File saved: " + fileName);
		
		return Files.readAllBytes(Paths.get(fileName));
	}
}
