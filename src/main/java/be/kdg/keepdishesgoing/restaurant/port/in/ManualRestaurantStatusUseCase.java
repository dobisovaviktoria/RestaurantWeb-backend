package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.exceptions.RestaurantNotFoundException;

public interface ManualRestaurantStatusUseCase {
    void changeStatus(ChangeRestaurantStatusCommand command) throws RestaurantNotFoundException;
}
