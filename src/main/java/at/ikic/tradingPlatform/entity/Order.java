package at.ikic.tradingPlatform.entity;

import at.ikic.tradingPlatform.enums.TransactionStatus;
import at.ikic.tradingPlatform.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name="orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private String coinId;

    private TransactionType type;

    private double quantity;

    private double price;

    private TransactionStatus status;

    @Version
    private Long version;

    private UUID referenceId;
}
