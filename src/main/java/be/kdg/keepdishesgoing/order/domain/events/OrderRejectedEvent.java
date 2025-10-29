package be.kdg.keepdishesgoing.order.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId,
        UUID restaurantId,
        String reason,
        LocalDateTime rejectedAt
) implements DomainEvent {
    @Override
    public LocalDateTime eventPit() {
        return rejectedAt;
    }
}
