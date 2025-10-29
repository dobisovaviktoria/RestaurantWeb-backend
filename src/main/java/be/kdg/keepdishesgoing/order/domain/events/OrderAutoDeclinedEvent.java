package be.kdg.keepdishesgoing.order.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAutoDeclinedEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime declinedAt
) implements DomainEvent {
    @Override
    public LocalDateTime eventPit() {
        return declinedAt;
    }
}
