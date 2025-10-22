package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.exceptions.RestaurantNotFoundException;
import be.kdg.keepdishesgoing.restaurant.port.in.ChangeRestaurantStatusCommand;
import be.kdg.keepdishesgoing.restaurant.port.in.ManualRestaurantStatusUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantUpdatePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ManualRestaurantStatusUseCaseImpl implements ManualRestaurantStatusUseCase {

    private final RestaurantLoadPort loadPort;
    private final RestaurantUpdatePort updatePort;

    public ManualRestaurantStatusUseCaseImpl(RestaurantLoadPort loadPort, RestaurantUpdatePort updatePort) {
        this.loadPort = loadPort;
        this.updatePort = updatePort;
    }

    @Override
    public void changeStatus(ChangeRestaurantStatusCommand command) throws RestaurantNotFoundException {
        var restaurant = loadPort.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + command.restaurantId()));

        restaurant.setStatus(command.status());
        updatePort.save(restaurant);
    }
}
