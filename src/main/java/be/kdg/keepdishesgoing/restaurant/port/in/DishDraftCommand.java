package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

public record DishDraftCommand(
        UUID restaurantId,
        UUID dishId,
        UUID liveDishId,
        String name,
        DishType dishType,
        List<FoodTag> foodTags,
        String description,
        float price,
        String imageUrl
) {
    public DishDraftCommand {
        Assert.notNull(restaurantId, "Restaurant ID must not be null");
        Assert.hasText(name, "Dish name is required");
        Assert.isTrue(price >= 0, "Price must be non-negative");
    }
}
