package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.port.in.StockDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishStockPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StockDishUseCaseImpl implements StockDishUseCase {

    private final DishStockPort dishStockPort;

    public StockDishUseCaseImpl(DishStockPort dishStockPort) {
        this.dishStockPort = dishStockPort;
    }

    @Override
    public void markOutOfStock(UUID restaurantId, UUID dishId) {
        dishStockPort.markOutOfStock(dishId);
    }

    @Override
    public void markBackInStock(UUID restaurantId, UUID dishId) {
        dishStockPort.markBackInStock(dishId);
    }
}
