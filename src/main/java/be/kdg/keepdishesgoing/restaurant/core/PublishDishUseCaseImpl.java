package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.port.in.PublishDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishWritePort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class PublishDishUseCaseImpl implements PublishDishUseCase {
    private final LoadDishPort loadPort;
    private final DishWritePort writePort;

    public PublishDishUseCaseImpl(LoadDishPort loadPort, DishWritePort writePort) {
        this.loadPort = loadPort;
        this.writePort = writePort;
    }

    @Override
    public Dish publishDish(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.publish();
        return writePort.save(dish);
    }

    @Override
    public Dish unpublishDish(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.unpublish();
        return writePort.save(dish);
    }
}
