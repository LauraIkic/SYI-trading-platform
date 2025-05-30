package at.ikic.tradingPlatform.repository;

import at.ikic.tradingPlatform.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository <Wallet, UUID>{


}
