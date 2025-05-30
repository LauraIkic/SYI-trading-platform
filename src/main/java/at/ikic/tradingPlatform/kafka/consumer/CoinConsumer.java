package at.ikic.tradingPlatform.kafka.consumer;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.entity.Coin;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@Service
public class CoinConsumer {

    public List<Coin> coins;

    @KafkaListener(topics = KafkaConstant.CRYPTO_COIN_TOPIC, groupId = KafkaConstant.CRYPTO_GROUP, containerFactory = "kafkaListenerContainerFactoryCoin")
    public void consume(List<Coin> coins)
    {
        this.coins = coins;
    }
}
