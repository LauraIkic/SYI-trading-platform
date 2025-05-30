package at.ikic.tradingPlatform.kafka.consumer;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderResponseConsumer {

    @Autowired
    private WalletService walletService;

    @KafkaListener(topics = KafkaConstant.ORDER_RESPONSE_TOPIC, groupId = KafkaConstant.CRYPTO_GROUP, containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consume(Order order) {
        System.out.println("=== KAFKA ORDER RESPONSE EMPFANGEN ===");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Order Status: " + order.getStatus());
        System.out.println("User ID: " + order.getUserId());

        try {
            String status = order.getStatus().toString();
            
            if ("EXECUTED".equals(status)) {
                System.out.println("Order wurde ausgeführt - Update Wallet für User: " + order.getUserId());
                
                walletService.updateWalletAfterOrder(order);
                
                System.out.println("Wallet erfolgreich aktualisiert");
            } else {
                System.out.println("Order Status: " + status + " - keine Wallet-Aktualisierung nötig");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Wallet-Update: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
