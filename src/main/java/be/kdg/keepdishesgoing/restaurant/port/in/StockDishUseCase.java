package be.kdg.keepdishesgoing.restaurant.port.in;

import java.util.UUID;

public interface StockDishUseCase {
    void markOutOfStock(UUID restaurantId, UUID dishId);
    void markBackInStock(UUID restaurantId, UUID dishId);
}
