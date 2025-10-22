package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.exceptions.RestaurantNotFoundException;
import be.kdg.keepdishesgoing.restaurant.port.in.UpdateOpeningHoursCommand;
import be.kdg.keepdishesgoing.restaurant.port.in.UpdateOpeningHoursUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantUpdatePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UpdateOpeningHoursUseCaseImpl implements UpdateOpeningHoursUseCase {

    private final RestaurantLoadPort loadPort;
    private final RestaurantUpdatePort updatePort;

    public UpdateOpeningHoursUseCaseImpl(RestaurantLoadPort loadPort, RestaurantUpdatePort updatePort) {
        this.loadPort = loadPort;
        this.updatePort = updatePort;
    }

    @Override
    public void updateOpeningHours(UpdateOpeningHoursCommand command) throws RestaurantNotFoundException {
        var restaurant = loadPort.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + command.restaurantId()));

        restaurant.updateOpeningHours(command.newOpeningHours());
        updatePort.save(restaurant);
    }
}
