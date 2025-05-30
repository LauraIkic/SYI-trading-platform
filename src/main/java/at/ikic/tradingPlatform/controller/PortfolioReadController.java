package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.dto.response.PortfolioItemDto;
import at.ikic.tradingPlatform.entity.Portfolio;
import at.ikic.tradingPlatform.entity.PortfolioItem;
import at.ikic.tradingPlatform.repository.PortfolioItemRepository;
import at.ikic.tradingPlatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PortfolioReadController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PortfolioItemRepository portfolioItemRepository;

    @GetMapping("/portfolio")
    public ResponseEntity<Portfolio> read() {
        Portfolio portfolio = authService.getAuthenticatedUser().getPortfolio();
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/portfolio/items")
    public ResponseEntity<List<PortfolioItemDto>> getPortfolioItems() {
        try {
            Portfolio portfolio = authService.getAuthenticatedUser().getPortfolio();
            List<PortfolioItem> items = portfolioItemRepository.findByPortfolio(portfolio);

            // Entity zu DTO konvertieren
            List<PortfolioItemDto> dtos = items.stream()
                    .map(item -> new PortfolioItemDto(
                            item.getId(),
                            item.getCoinId(),
                            item.getCoinName(),
                            item.getCoinSymbol(),
                            item.getQuantity(),
                            item.getAveragePrice(),
                            item.getCurrentPrice(),
                            item.getImageUrl()  // HINZUGEFÜGT: Image-URL weiterleiten
                    ))
                    .toList();

            System.out.println("Portfolio Items gefunden: " + dtos.size());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Portfolio Items: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
