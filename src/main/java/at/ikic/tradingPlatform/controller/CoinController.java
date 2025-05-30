package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.entity.Coin;
import at.ikic.tradingPlatform.kafka.consumer.CoinConsumer;
import at.ikic.tradingPlatform.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CoinController {

    @Autowired
    private CoinConsumer coinConsumer;

    @Autowired
    private CoinRepository coinRepository;

    @GetMapping("/coin")
    public List<Coin> list() {
        return coinRepository.findAll(); // liest alle Coins aus der Datenbank
    }

    @GetMapping("/coin/{id}")
    public Coin read(@PathVariable String id) {
        return coinConsumer.coins.stream()
                .filter(coin -> coin.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Coin not found"));
    }

}
