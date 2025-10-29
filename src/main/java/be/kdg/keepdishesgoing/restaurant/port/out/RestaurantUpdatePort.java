package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;

public interface RestaurantUpdatePort {
    void save(Restaurant restaurant);
}
