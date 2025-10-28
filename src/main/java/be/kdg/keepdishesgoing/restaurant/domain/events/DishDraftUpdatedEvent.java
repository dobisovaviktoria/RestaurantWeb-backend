package be.kdg.keepdishesgoing.restaurant.domain.events;

import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DishDraftUpdatedEvent(LocalDateTime eventPit, UUID draftId,
                                    String name, DishType dishType,
                                    List<FoodTag> foodTags, String description,
                                    float price, String imageUrl) implements DomainEvent {
    public DishDraftUpdatedEvent(UUID draftId, String name, DishType dishType,
                                 List<FoodTag> foodTags, String description, float price, String imageUrl) {
        this(LocalDateTime.now(), draftId, name, dishType, foodTags, description, price, imageUrl);
    }

    @Override
    public LocalDateTime eventPit() { return eventPit; }
}
