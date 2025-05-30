package at.ikic.tradingPlatform.repository;


import at.ikic.tradingPlatform.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoinRepository extends JpaRepository <Coin, String>{

}
