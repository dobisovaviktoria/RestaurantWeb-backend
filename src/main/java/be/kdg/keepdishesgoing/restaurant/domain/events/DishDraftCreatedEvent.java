package be.kdg.keepdishesgoing.restaurant.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DishDraftCreatedEvent(
        LocalDateTime eventPit,
        UUID restaurantId,
        UUID draftId,
        UUID liveDishId,
        String name,
        DishType dishType,
        List<FoodTag> foodTags,
        String description,
        float price,
        String imageUrl
) implements DomainEvent {
    public DishDraftCreatedEvent {
    }

    public DishDraftCreatedEvent(UUID restaurantId, UUID draftId, UUID liveDishId, String name, DishType dishType,
                                 List<FoodTag> foodTags, String description, float price, String imageUrl) {
        this(LocalDateTime.now(), restaurantId, draftId, liveDishId, name, dishType, foodTags, description, price, imageUrl);
    }

    @Override
    public LocalDateTime eventPit() { return eventPit; }
}
