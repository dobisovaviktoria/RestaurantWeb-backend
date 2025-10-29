package be.kdg.keepdishesgoing.order.domain.events;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.common.domain.Money;
import be.kdg.keepdishesgoing.order.domain.OrderLine;
import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID orderId,
        UUID restaurantId,
        String customerName,
        String customerEmail,
        Address deliveryAddress,
        List<OrderLine> orderLines,
        Money totalAmount,
        LocalDateTime placedAt
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return placedAt;
    }
}
