package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    // Orders für einen User finden
    List<Order> findByUserIdOrderByIdDesc(UUID userId);
    
    //  Nur die letzten 10 Orders
    List<Order> findTop10ByUserIdOrderByIdDesc(UUID userId);
}
