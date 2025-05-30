package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.repository.OrderRepository;
import at.ikic.tradingPlatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class OrderReadController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        try {
            UUID userId = authService.getAuthenticatedUser().getId();
            
            // Alle Orders für diesen User laden
            List<Order> orders = orderRepository.findByUserIdOrderByIdDesc(userId);
            
            System.out.println("Orders gefunden für User " + userId + ": " + orders.size());
            
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Orders: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
