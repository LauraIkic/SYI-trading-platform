package at.ikic.tradingPlatform.deserializer;

import at.ikic.tradingPlatform.entity.Coin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.List;

public class ListCoinDeserializer implements Deserializer<List<Coin>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Coin> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(List.class, Coin.class));
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing List<Coin>", e);
        }
    }

    @Override
    public void close() {
        // No resource cleanup required
    }
}
