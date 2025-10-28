package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.port.in.StockDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.UpdateDishPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class StockDishUseCaseImpl implements StockDishUseCase {

    private final LoadDishPort loadPort;
    private final UpdateDishPort updatePort;

    public StockDishUseCaseImpl(LoadDishPort loadPort, UpdateDishPort updatePort) {
        this.loadPort = loadPort;
        this.updatePort = updatePort;
    }

    @Override
    public Dish markOutOfStock(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

        dish.markOutOfStock();
        return updatePort.save(dish);
    }

    @Override
    public Dish markBackInStock(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

        dish.markInStock();
        return updatePort.save(dish);
    }
}
