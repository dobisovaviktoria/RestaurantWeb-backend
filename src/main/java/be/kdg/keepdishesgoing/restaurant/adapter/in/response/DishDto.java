package be.kdg.keepdishesgoing.restaurant.adapter.in.response;

import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.domain.StockStatus;
import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;

import java.util.List;
import java.util.UUID;

public record DishDto(
        UUID id,
        UUID liveDishId,
        String name,
        DishType dishType,
        List<FoodTag> foodTags,
        String description,
        float price,
        String imageUrl,
        DishStatus status,
        StockStatus stockStatus
) {}
