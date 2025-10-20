package be.kdg.keepdishesgoing.restaurant.adapter.in.request;

import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;

import java.util.List;
import java.util.UUID;

public record DishDraftRequest(
        UUID dishId,
        UUID liveDishId,
        String name,
        DishType dishType,
        List<FoodTag> foodTags,
        String description,
        float price,
        String imageUrl
) {}
