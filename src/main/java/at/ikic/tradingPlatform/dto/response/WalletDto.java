package at.ikic.tradingPlatform.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletDto {
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal investedAmount;
    private BigDecimal totalValue;
}
