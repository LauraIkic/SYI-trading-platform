package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.entity.*;
import at.ikic.tradingPlatform.enums.TransactionType;
import at.ikic.tradingPlatform.enums.WalletTransactionType;
import at.ikic.tradingPlatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PortfolioService {

    @Autowired
    private AuthService authService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Transactional
    public void updatePortfolioAfterPurchase(User user, Order order) {
       try {
           Portfolio portfolio = user.getPortfolio();
           List<Asset> assetList = portfolio.getAssets();

           Asset asset = assetList.stream()
                   .filter(assetItem -> assetItem.getCoinId().equals(order.getCoinId()))
                   .findFirst().orElse(null);

           if (null != asset) {
               System.out.println("found asset");
               System.out.println(asset.getId());
               asset.setQuantity(asset.getQuantity() + order.getQuantity());
               BigDecimal newValue = BigDecimal.valueOf(order.getQuantity() * order.getPrice() + asset.getValue().doubleValue());
               asset.setValue(newValue);
               assetRepository.save(asset);
           } else {
               asset = new Asset();
               asset.setValue(BigDecimal.valueOf(order.getQuantity() * order.getPrice()));
               asset.setQuantity(order.getQuantity());
               Coin c = coinRepository.findById(order.getCoinId()).orElse(null);
               asset.setCoinId(c);
               assetRepository.save(asset);
               assetList.add(asset);
               portfolio.setAssets(assetList);
           }

           portfolioRepository.save(portfolio);

       } catch (Exception e) {
           System.err.println("Fehler beim Portfolio-Update: " + e.getMessage());
           e.printStackTrace();
       }
   }
}
