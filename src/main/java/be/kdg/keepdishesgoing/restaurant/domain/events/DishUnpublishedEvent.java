package be.kdg.keepdishesgoing.restaurant.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record DishUnpublishedEvent(LocalDateTime eventPit, UUID restaurantId, UUID liveDishId) implements DomainEvent {
    public DishUnpublishedEvent(UUID restaurantId, UUID liveDishId) {
        this(LocalDateTime.now(), restaurantId, liveDishId);
    }

    @Override
    public LocalDateTime eventPit() { return eventPit; }
}
