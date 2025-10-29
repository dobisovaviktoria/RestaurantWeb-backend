package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadDishPort {
    Optional<Dish> loadById(UUID restaurantId, UUID dishId);
    List<Dish> loadDrafts(UUID restaurantId);
    List<Dish> loadPublished(UUID restaurantId);
}
