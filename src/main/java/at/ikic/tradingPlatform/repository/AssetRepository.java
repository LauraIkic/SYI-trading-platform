package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Asset;
import at.ikic.tradingPlatform.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository <Asset, UUID>{
}
