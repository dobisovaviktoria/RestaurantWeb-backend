package be.kdg.keepdishesgoing.order.domain;

import be.kdg.keepdishesgoing.common.domain.Money;

import java.util.UUID;

public record OrderLine(
        UUID dishId,
        String dishName,
        Money price,
        int quantity
) {
    public OrderLine {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public Money getLineTotal() {
        return new Money(price.amount().multiply(java.math.BigDecimal.valueOf(quantity)));
    }
}
