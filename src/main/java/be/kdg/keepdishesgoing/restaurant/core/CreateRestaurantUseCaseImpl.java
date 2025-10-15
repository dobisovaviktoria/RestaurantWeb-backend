package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import be.kdg.keepdishesgoing.restaurant.domain.exceptions.OwnerAlreadyHasRestaurantException;
import be.kdg.keepdishesgoing.restaurant.port.in.CreateRestaurantCommand;
import be.kdg.keepdishesgoing.restaurant.port.in.CreateRestaurantUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.CreateRestaurantPort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadRestaurantPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final Logger logger = LoggerFactory.getLogger(CreateRestaurantUseCaseImpl.class);
    private final CreateRestaurantPort createRestaurantPort;
    private final LoadRestaurantPort loadRestaurantPort;

    public CreateRestaurantUseCaseImpl(CreateRestaurantPort createRestaurantPort,  LoadRestaurantPort loadRestaurantPort) {
        this.createRestaurantPort = createRestaurantPort;
        this.loadRestaurantPort = loadRestaurantPort;
    }

    @Override
    @Transactional
    public Restaurant createRestaurant(CreateRestaurantCommand command) throws OwnerAlreadyHasRestaurantException{
        if (loadRestaurantPort.existsByOwnerId(command.ownerId())) {
            throw new OwnerAlreadyHasRestaurantException("Owner already has a restaurant");
        }
        Restaurant restaurant = new Restaurant(command.name(), command.address(), command.email(),
                command.pictures(), command.cuisineType(), command.preparationTime(), command.openingHours(),
                command.ownerId());
        logger.info("Creating restaurant with name: " + command.name());
        return createRestaurantPort.create(restaurant);
    }
}
