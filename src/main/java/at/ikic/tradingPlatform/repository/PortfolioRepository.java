package at.ikic.tradingPlatform.repository;


import at.ikic.tradingPlatform.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

}
