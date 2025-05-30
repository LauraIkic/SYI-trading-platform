package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.dto.response.WalletDto;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.entity.Wallet;
import at.ikic.tradingPlatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "http://localhost:3000")
public class WalletController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<WalletDto> getUserWallet() {
        try {
            User currentUser = authService.getAuthenticatedUser();
            Wallet wallet = currentUser.getWallet();
            
            WalletDto walletDto = new WalletDto();
            walletDto.setBalance(wallet.getBalance());
            walletDto.setAvailableBalance(wallet.getBalance());
            walletDto.setInvestedAmount(BigDecimal.ZERO);
            walletDto.setTotalValue(wallet.getBalance());
            
            return ResponseEntity.ok(walletDto);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Wallet: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}
