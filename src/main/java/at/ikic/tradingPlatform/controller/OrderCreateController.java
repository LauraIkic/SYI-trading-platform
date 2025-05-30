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
    private ResponseEntity<Order> createOrder(@RequestBody OrderCreateDto data){
        Coin c =  coinConsumer.coins.stream()
                .filter(coin -> coin.getId().equals(data.getCoinId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Coin not found"));

        BigDecimal currentPrice = BigDecimal.valueOf(c.getCurrentPrice());
        if (false == walletService.balanceSufficient(currentPrice)) {
             throw new IllegalArgumentException("Balance not sufficient");
        }

        Order order = new Order();
        orderCreateMapper.mapToEntity(order, data, c);
        orderRepository.save(order);

        orderService.addOrderToMarket(order);

        return ResponseEntity.ok(order);
    }
}
