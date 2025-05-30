package at.ikic.tradingPlatform.dto.request;

import at.ikic.tradingPlatform.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderCreateDto {
    @NotNull(message = "Transaction type can not be null")
    private TransactionType type;

    @NotNull(message = "Coin Id can not be null")
    private String coinId;

    @NotNull(message = "Quantity can not be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private double quantity;
}
