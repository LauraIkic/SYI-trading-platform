/* filepath: /Users/aylin/FH Projekte/SYSI/SYI-Gruppenprojekt/SYI-trading-platform/src/main/java/at/ikic/tradingPlatform/dto/response/PortfolioItemDto.java */
package at.ikic.tradingPlatform.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItemDto {
    private UUID id;
    private String coinId;
    private String coinName;
    private String coinSymbol;
    private Double quantity;
    private Double averagePrice;
    private Double currentPrice;
    private String imageUrl;
}
