package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.entity.Wallet;
import at.ikic.tradingPlatform.enums.WalletTransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private AuthService authService;

   public void transferMoney(BigDecimal money, WalletTransactionType type){
       Wallet wallet = authService.getAuthenticatedUser().getWallet();

       if (WalletTransactionType.DEPOSIT == type) {
           wallet.setBalance(wallet.getBalance().add(money));
       } else {
           wallet.setBalance(wallet.getBalance().subtract(money));
       }
   }

   public Boolean balanceSufficient(BigDecimal money) {
       Wallet wallet = authService.getAuthenticatedUser().getWallet();

       return wallet.getBalance().compareTo(money) >= 0;
   }
}
