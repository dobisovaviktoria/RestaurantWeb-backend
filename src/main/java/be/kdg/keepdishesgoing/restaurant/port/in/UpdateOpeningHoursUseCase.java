package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.exceptions.RestaurantNotFoundException;

public interface UpdateOpeningHoursUseCase {
    void updateOpeningHours(UpdateOpeningHoursCommand command) throws RestaurantNotFoundException;
}
