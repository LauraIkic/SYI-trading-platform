package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssetRepository extends JpaRepository <Asset, UUID>{

}
