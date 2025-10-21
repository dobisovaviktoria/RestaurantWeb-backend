package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;

import java.util.UUID;

public interface StockDishUseCase {
    Dish markOutOfStock(UUID restaurantId, UUID dishId);
    Dish markBackInStock(UUID restaurantId, UUID dishId);
}
