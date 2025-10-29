package be.kdg.keepdishesgoing.order.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAcceptedEvent(
        UUID orderId,
        UUID restaurantId,
        LocalDateTime acceptedAt
) implements DomainEvent {
    @Override
    public LocalDateTime eventPit() {
        return acceptedAt;
    }
}
