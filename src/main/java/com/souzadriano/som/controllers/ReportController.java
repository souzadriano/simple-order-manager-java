package com.souzadriano.som.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.souzadriano.som.entities.Order;
import com.souzadriano.som.entities.OrderStockMovements;
import com.souzadriano.som.entities.StockMovement;
import com.souzadriano.som.jpaentities.EmailLogEntity;
import com.souzadriano.som.repositories.EmailLogRepository;
import com.souzadriano.som.services.ReportService;

@RestController
public class ReportController {

	private ReportService reportService;
	private EmailLogRepository emailLogRepository;

	public ReportController(ReportService reportService, EmailLogRepository emailLogRepository) {
		this.reportService = reportService;
		this.emailLogRepository = emailLogRepository;
	}

	@GetMapping("/reports/orders/{orderId}/stock-movements")
	public List<StockMovement> reportStockMovementsByOrderId(@PathVariable Long orderId) {
		return reportService.reportStockMovementsByOrderId(orderId);
	}

	@GetMapping("/reports/stock-movements/{stockMovementId}/orders")
	public List<Order> reportOrdersByStockMovementId(@PathVariable Long stockMovementId) {
		return reportService.reportOrdersByStockMovementId(stockMovementId);
	}

	@GetMapping("/reports/emails/logs")
	public List<EmailLogEntity> reportEmailLogs() {
		return emailLogRepository.findAll(Sort.by("creationDate"));
	}
	
	@GetMapping("/reports/orders-and-stock-movements")
	public List<OrderStockMovements> ordersAndStockMovements() {
		return reportService.ordersAndStockMovements();
	}

	@GetMapping(value = "/reports/write-logs", produces = MediaType.TEXT_PLAIN_VALUE)
	public byte[] writeLogWithOrdersCompletedAndStockMovementsAndEmailLogs(HttpServletResponse response ) throws IOException {
		response.setContentType("text/plain");
	    response.setHeader("Content-Disposition","attachment;filename=SimpleOrderManager.txt");
		return reportService.writeLogWithOrdersCompletedAndStockMovementsAndEmailLogs();
	}
}
