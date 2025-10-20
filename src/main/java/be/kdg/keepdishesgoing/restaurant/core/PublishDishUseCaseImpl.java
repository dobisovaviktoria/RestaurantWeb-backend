package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.port.in.PublishDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishPublishingPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PublishDishUseCaseImpl implements PublishDishUseCase {

    private final DishPublishingPort dishPublishingPort;

    public PublishDishUseCaseImpl(DishPublishingPort dishPublishingPort) {
        this.dishPublishingPort = dishPublishingPort;
    }

    @Override
    public void publishDish(UUID restaurantId, UUID dishId) {
        dishPublishingPort.publishDish(dishId);
    }

    @Override
    public void unpublishDish(UUID restaurantId, UUID dishId) {
        dishPublishingPort.unpublishDish(dishId);
    }
}
