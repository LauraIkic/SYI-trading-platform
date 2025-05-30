package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.dto.request.OrderCreateDto;
import at.ikic.tradingPlatform.entity.Coin;
import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.kafka.consumer.CoinConsumer;
import at.ikic.tradingPlatform.mapper.OrderCreateMapper;
import at.ikic.tradingPlatform.repository.OrderRepository;
import at.ikic.tradingPlatform.service.OrderService;
import at.ikic.tradingPlatform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class OrderCreateController {

    @Autowired
    private OrderCreateMapper orderCreateMapper;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CoinConsumer coinConsumer;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateDto data) {
        System.out.println("=== ORDER REQUEST RECEIVED ===");
        System.out.println("Request data: " + data);
        System.out.println("CoinId: " + data.getCoinId());
        System.out.println("Quantity: " + data.getQuantity());
        System.out.println("Type: " + data.getType());

        try {
            // Debug: Verfügbare Coins anzeigen
            System.out.println("Available coins: " + coinConsumer.coins.size());
            for (Coin coin : coinConsumer.coins) {
                System.out.println("  - " + coin.getId() + " (" + coin.getName() + ")");
            }

            Coin c = coinConsumer.coins.stream()
                    .filter(coin -> coin.getId().equals(data.getCoinId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Coin not found: " + data.getCoinId()));

            System.out.println("Found coin: " + c.getName());

            BigDecimal currentPrice = BigDecimal.valueOf(c.getCurrentPrice());
            System.out.println("Current price: " + currentPrice);

            // Temporär Wallet-Check deaktivieren
            // if (false == walletService.balanceSufficient(currentPrice)) {
            //     throw new IllegalArgumentException("Balance not sufficient");
            // }

            Order order = new Order();
            System.out.println("Creating order object...");

            orderCreateMapper.mapToEntity(order, data, c);
            System.out.println("Mapped to entity: " + order);

            orderRepository.save(order);
            System.out.println("Order saved with ID: " + order.getId());

            // AKTIVIERT: Market-Service
            orderService.addOrderToMarket(order);
            System.out.println("Order sent to market");

            return ResponseEntity.ok(order);

        } catch (Exception e) {
            System.err.println("=== ORDER CREATION FAILED ===");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Cause: " + e.getCause());
            e.printStackTrace();

            // Detaillierte Fehler-Response
            return ResponseEntity.status(500).body(null);
        }
    }
}
