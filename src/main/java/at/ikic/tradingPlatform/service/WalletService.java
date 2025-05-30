package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.entity.Order;
import at.ikic.tradingPlatform.entity.Wallet;
import at.ikic.tradingPlatform.entity.Coin;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.entity.Portfolio;
import at.ikic.tradingPlatform.entity.PortfolioItem;
import at.ikic.tradingPlatform.enums.WalletTransactionType;
import at.ikic.tradingPlatform.repository.CoinRepository;
import at.ikic.tradingPlatform.repository.UserRepository;
import at.ikic.tradingPlatform.repository.WalletRepository;
import at.ikic.tradingPlatform.repository.PortfolioItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private AuthService authService;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PortfolioItemRepository portfolioItemRepository;

    public void transferMoney(BigDecimal money, WalletTransactionType type) {
        Wallet wallet = authService.getAuthenticatedUser().getWallet();

        if (WalletTransactionType.DEPOSIT == type) {
            wallet.setBalance(wallet.getBalance().add(money));
        } else {
            wallet.setBalance(wallet.getBalance().subtract(money));
        }
        walletRepository.save(wallet);
    }

    public Boolean balanceSufficient(BigDecimal money) {
        Wallet wallet = authService.getAuthenticatedUser().getWallet();
        return wallet.getBalance().compareTo(money) >= 0;
    }

    public void updateWalletAfterOrder(Order order) {
        try {
            System.out.println("Update Wallet nach Order: " + order.getId());
            
            UUID userId = order.getUserId();
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + userId));
            
            Wallet wallet = user.getWallet();
            Portfolio portfolio = user.getPortfolio();
            System.out.println("Gefundenes Wallet für User: " + userId + ", Balance: " + wallet.getBalance());
            
            Coin coin = coinRepository.findById(order.getCoinId()).orElse(null);
            if (coin == null) {
                System.err.println("Coin nicht gefunden: " + order.getCoinId());
                return;
            }
            
            BigDecimal orderValue = BigDecimal.valueOf(order.getQuantity() * order.getPrice());
            
            if ("BUY".equals(order.getType().toString())) {
                
                // 1. WALLET UPDATE
                wallet.setBalance(wallet.getBalance().subtract(orderValue));
                walletRepository.save(wallet);
                System.out.println("Wallet-Balance reduziert um: $" + orderValue);
                
                // 2. PORTFOLIO UPDATE - HINZUGEFÜGT!
                updatePortfolioAfterPurchase(portfolio, coin, order.getQuantity(), order.getPrice());
                
                System.out.println("Neue Balance: $" + wallet.getBalance());
            }
            
            System.out.println("Wallet und Portfolio erfolgreich aktualisiert für Order: " + order.getId());
            
        } catch (Exception e) {
            System.err.println("Fehler beim Wallet-Update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NEUE METHODE: Portfolio nach Kauf aktualisieren
    private void updatePortfolioAfterPurchase(Portfolio portfolio, Coin coin, Double quantity, Double price) {
        try {
            // Prüfe ob Coin bereits im Portfolio existiert
            Optional<PortfolioItem> existingItem = portfolioItemRepository
                .findByPortfolioAndCoinId(portfolio, coin.getId());
            
            if (existingItem.isPresent()) {
                // Coin existiert bereits - erhöhe Quantity
                PortfolioItem item = existingItem.get();
                Double newQuantity = item.getQuantity() + quantity;
                Double newAveragePrice = ((item.getQuantity() * item.getAveragePrice()) + (quantity * price)) / newQuantity;
                
                item.setQuantity(newQuantity);
                item.setAveragePrice(newAveragePrice);
                item.setCurrentPrice(coin.getCurrentPrice()); // Aktueller Preis vom Coin
                item.setImageUrl(coin.getImage());            // HINZUGEFÜGT: Image-URL
                
                portfolioItemRepository.save(item);
                System.out.println("Portfolio Item aktualisiert: " + coin.getSymbol() + " - Neue Quantity: " + newQuantity);
                
            } else {
                // Neuer Coin - erstelle PortfolioItem
                PortfolioItem newItem = new PortfolioItem();
                newItem.setPortfolio(portfolio);
                newItem.setCoinId(coin.getId());
                newItem.setCoinName(coin.getName());
                newItem.setCoinSymbol(coin.getSymbol());
                newItem.setQuantity(quantity);
                newItem.setAveragePrice(price);
                newItem.setCurrentPrice(coin.getCurrentPrice());
                newItem.setImageUrl(coin.getImage());        // HINZUGEFÜGT: Image-URL
                
                portfolioItemRepository.save(newItem);
                System.out.println("Neues Portfolio Item erstellt: " + coin.getSymbol() + " - Quantity: " + quantity);
            }
            
        } catch (Exception e) {
            System.err.println("Fehler beim Portfolio-Update: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
