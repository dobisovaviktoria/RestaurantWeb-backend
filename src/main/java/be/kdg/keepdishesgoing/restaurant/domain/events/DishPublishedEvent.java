package be.kdg.keepdishesgoing.restaurant.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record DishPublishedEvent(LocalDateTime eventPit, UUID restaurantId, UUID draftId, UUID liveDishId) implements DomainEvent {
    public DishPublishedEvent(UUID restaurantId, UUID draftId, UUID liveDishId) {
        this(LocalDateTime.now(), restaurantId, draftId, liveDishId);
    }

    @Override
    public LocalDateTime eventPit() { return eventPit; }
}
