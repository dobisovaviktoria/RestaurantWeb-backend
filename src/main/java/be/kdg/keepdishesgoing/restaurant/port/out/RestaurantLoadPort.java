package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantLoadPort {
    Optional<Restaurant> findById(UUID id);
    List<Restaurant> findAll();
    Optional<Restaurant> findByOwnerId(String ownerId);
}
