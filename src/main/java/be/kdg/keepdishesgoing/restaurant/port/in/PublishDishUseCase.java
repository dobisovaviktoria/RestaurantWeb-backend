package be.kdg.keepdishesgoing.restaurant.port.in;

import java.util.UUID;

public interface PublishDishUseCase {
    void publishDish(UUID restaurantId, UUID dishId);
    void unpublishDish(UUID restaurantId, UUID dishId);
}
