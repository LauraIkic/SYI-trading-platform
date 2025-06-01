package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.entity.*;
import at.ikic.tradingPlatform.enums.TransactionType;
import at.ikic.tradingPlatform.enums.WalletTransactionType;
import at.ikic.tradingPlatform.repository.*;
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
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;


    public void transferMoney(BigDecimal money, WalletTransactionType type){
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


    public void updateWalletAfterOrder(Order order, User user) {
        try {
            System.out.println("Update Wallet nach Order: " + order.getId());
            System.out.println("User wallet update" + user.getId());

            Wallet wallet = user.getWallet();
            System.out.println("User wallet " + wallet.getId());
            System.out.println("Gefundenes Wallet für User: " + user.getId() + ", Balance: " + wallet.getBalance());

            Coin coin = coinRepository.findById(order.getCoinId()).orElse(null);
            if (coin == null) {
                System.err.println("Coin nicht gefunden: " + order.getCoinId());
                return;
            }

            BigDecimal orderValue = BigDecimal.valueOf(order.getQuantity() * order.getPrice());

            if (TransactionType.BUY == order.getType()) {
                wallet.setBalance(wallet.getBalance().subtract(orderValue));
                walletRepository.save(wallet);
            }

        } catch (Exception e) {
            System.err.println("Fehler beim Wallet-Update: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
