package be.kdg.keepdishesgoing.restaurant.adapter.in.response;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;

public class DishDtoMapper {
    public static DishDto fromDomain(Dish dish) {
        return new DishDto(
                dish.id(),
                dish.liveDishId(),
                dish.name(),
                dish.dishType(),
                dish.foodTags(),
                dish.description(),
                dish.price(),
                dish.imageUrl(),
                dish.status(),
                dish.stockStatus()
        );
    }
}
