package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import be.kdg.keepdishesgoing.restaurant.domain.exceptions.OwnerAlreadyHasRestaurantException;

public interface CreateRestaurantUseCase {
    Restaurant createRestaurant(CreateRestaurantCommand command)
            throws OwnerAlreadyHasRestaurantException;
}