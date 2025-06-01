package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository <Order, UUID>{
    List<Order> findByUserId(UUID uuid);

    List<Order> findAllByUserIdAndCoinIdAndPriceAndQuantity(
            UUID userId, String coinId, double price, double quantity);
}
