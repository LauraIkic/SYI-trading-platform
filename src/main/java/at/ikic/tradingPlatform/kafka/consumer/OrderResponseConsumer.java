package at.ikic.tradingPlatform.kafka.consumer;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.repository.OrderRepository;
import at.ikic.tradingPlatform.repository.UserRepository;
import at.ikic.tradingPlatform.service.OrderService;
import at.ikic.tradingPlatform.service.PortfolioService;
import at.ikic.tradingPlatform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderResponseConsumer {

    @Autowired
    private WalletService walletService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @KafkaListener(topics = KafkaConstant.ORDER_RESPONSE_TOPIC, groupId = KafkaConstant.CRYPTO_GROUP, containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consume(Order order) {
        System.out.println("=== KAFKA ORDER RESPONSE EMPFANGEN ===");

        System.out.println("Order ID: " + order.getId());
        System.out.println("Order Status: " + order.getStatus());
        System.out.println("User ID: " + order.getUserId());

        UUID userId = order.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));

        try {
            String status = order.getStatus().toString();
            if ("EXECUTED".equals(status)) {
                List<Order> orders = orderRepository.findAllByUserIdAndCoinIdAndPriceAndQuantity(userId, order.getCoinId(),order.getPrice(), order.getQuantity());
                order = orders.get(orders.size() -1);

                walletService.updateWalletAfterOrder(order, user);
                orderService.updateOrderState(order);
                portfolioService.updatePortfolioAfterPurchase(user, order);
            } else {
                System.out.println("Order Status: " + status + " - keine Wallet-Aktualisierung nötig");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Wallet-Update: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
