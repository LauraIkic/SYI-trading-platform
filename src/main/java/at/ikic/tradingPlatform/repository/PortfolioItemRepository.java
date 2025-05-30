package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Portfolio;
import at.ikic.tradingPlatform.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, UUID> {
    
    // Finde ein spezifisches Item in einem Portfolio
    Optional<PortfolioItem> findByPortfolioAndCoinId(Portfolio portfolio, String coinId);
    
    // Finde alle Items eines Portfolios
    List<PortfolioItem> findByPortfolio(Portfolio portfolio);
}
