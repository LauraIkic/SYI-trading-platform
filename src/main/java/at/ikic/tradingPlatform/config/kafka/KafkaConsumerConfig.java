package at.ikic.tradingPlatform.config.kafka;

import at.ikic.tradingPlatform.constants.KafkaConstant;
import at.ikic.tradingPlatform.deserializer.OrderDeserializer;
import at.ikic.tradingPlatform.entity.Coin;
import at.ikic.tradingPlatform.deserializer.ListCoinDeserializer;
import at.ikic.tradingPlatform.entity.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, List<Coin>> consumerFactoryCoin() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.CRYPTO_GROUP);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ListCoinDeserializer listCoinDeserializer = new ListCoinDeserializer();

        return new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new ErrorHandlingDeserializer<>(listCoinDeserializer));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, List<Coin>>> kafkaListenerContainerFactoryCoin() {
        ConcurrentKafkaListenerContainerFactory<String, List<Coin>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryCoin());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Order> consumerFactoryOrder() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.CRYPTO_GROUP);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        OrderDeserializer orderDeserializer = new OrderDeserializer();

        return new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new ErrorHandlingDeserializer<>(orderDeserializer));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Order>> kafkaListenerContainerFactoryOrder() {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOrder());
        return factory;
    }
}
