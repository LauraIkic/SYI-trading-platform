package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.entity.Portfolio;
import at.ikic.tradingPlatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PortfolioReadController {

    @Autowired
    private AuthService authService;

    @GetMapping("/portfolio")
    public ResponseEntity<Portfolio> read (){
        return ResponseEntity.ok(authService.getAuthenticatedUser().getPortfolio());
    }
}
