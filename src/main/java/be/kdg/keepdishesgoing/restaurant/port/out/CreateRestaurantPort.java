package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;

public interface CreateRestaurantPort {
    Restaurant create(Restaurant restaurant);
}
