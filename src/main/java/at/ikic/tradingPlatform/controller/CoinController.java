package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.entity.Coin;
import at.ikic.tradingPlatform.kafka.consumer.CoinConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CoinController {

    @Autowired
    private CoinConsumer coinConsumer;

    @GetMapping("/coin")
    public List<Coin> list (){
        return coinConsumer.coins;
    }

    @GetMapping("/coin/{id}")
    public Coin read(@PathVariable String id) {
        return coinConsumer.coins.stream()
                .filter(coin -> coin.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Coin not found"));
    }

}
