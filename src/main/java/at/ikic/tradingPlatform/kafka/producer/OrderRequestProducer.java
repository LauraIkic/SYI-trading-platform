package at.ikic.tradingPlatform.kafka.producer;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderRequestProducer {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrderToKafka(Order order) {
        kafkaTemplate.send(KafkaConstant.ORDER_TOPIC, order);
    }
}
