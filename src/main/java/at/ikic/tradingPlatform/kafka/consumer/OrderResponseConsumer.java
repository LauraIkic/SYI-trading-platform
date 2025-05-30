package at.ikic.tradingPlatform.kafka.consumer;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.entity.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderResponseConsumer {

    @KafkaListener(topics = KafkaConstant.ORDER_RESPONSE_TOPIC, groupId = KafkaConstant.CRYPTO_GROUP, containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consume(Order order) {
        System.out.println("PROCESSED ORDER RETURNED: " + order);
    }
}
