package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.kafka.producer.OrderRequestProducer;
import at.ikic.tradingPlatform.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private OrderRequestProducer orderRequestProducer;

    @Transactional
    public void addOrderToMarket(Order order) {
        orderRequestProducer.sendOrderToKafka(order);
    }
}
