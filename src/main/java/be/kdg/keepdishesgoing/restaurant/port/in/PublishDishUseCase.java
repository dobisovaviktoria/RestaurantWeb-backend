package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;

import java.util.UUID;

public interface PublishDishUseCase {
    Dish publishDish(UUID restaurantId, UUID dishId);
    Dish unpublishDish(UUID restaurantId, UUID dishId);
}
