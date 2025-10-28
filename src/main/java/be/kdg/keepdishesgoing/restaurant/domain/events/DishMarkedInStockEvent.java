package be.kdg.keepdishesgoing.restaurant.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record DishMarkedInStockEvent(LocalDateTime eventPit, UUID restaurantId, UUID dishId) implements DomainEvent {
    public DishMarkedInStockEvent(UUID restaurantId, UUID dishId) {
        this(LocalDateTime.now(), restaurantId, dishId);
    }

    @Override
    public LocalDateTime eventPit() { return eventPit; }
}
