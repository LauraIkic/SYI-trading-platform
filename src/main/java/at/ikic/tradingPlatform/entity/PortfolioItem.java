/* filepath: /Users/aylin/FH Projekte/SYSI/SYI-Gruppenprojekt/SYI-trading-platform/src/main/java/at/ikic/tradingPlatform/entity/PortfolioItem.java */
package at.ikic.tradingPlatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

import at.ikic.tradingPlatform.entity.Portfolio;

@Entity
@Table(name = "portfolio_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "coin_id", nullable = false)
    private String coinId;

    @Column(name = "coin_name")
    private String coinName;

    @Column(name = "coin_symbol")
    private String coinSymbol;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "average_price", nullable = false)
    private Double averagePrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "image_url")
    private String imageUrl;  
}
