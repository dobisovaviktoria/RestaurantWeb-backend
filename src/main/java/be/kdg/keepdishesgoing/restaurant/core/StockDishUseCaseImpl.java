package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.port.in.StockDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishWritePort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class StockDishUseCaseImpl implements StockDishUseCase {
    private final LoadDishPort loadPort;
    private final DishWritePort writePort;

    public StockDishUseCaseImpl(LoadDishPort loadPort, DishWritePort writePort) {
        this.loadPort = loadPort;
        this.writePort = writePort;
    }

    @Override
    public Dish markOutOfStock(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.markOutOfStock();
        return writePort.save(dish);
    }

    @Override
    public Dish markBackInStock(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.markInStock();
        return writePort.save(dish);
    }
}
