package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.LiveDishProjectionRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.port.in.PublishDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.UpdateDishPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PublishDishUseCaseImpl implements PublishDishUseCase {

    private final LoadDishPort loadPort;
    private final UpdateDishPort updatePort;
    private final LiveDishProjectionRepository liveRepo;

    public PublishDishUseCaseImpl(LoadDishPort loadPort,
                                  UpdateDishPort updatePort,
                                  LiveDishProjectionRepository liveRepo) {
        this.loadPort = loadPort;
        this.updatePort = updatePort;
        this.liveRepo = liveRepo;
    }

    @Override
    public Dish publishDish(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

        // max 10 for live
        if (dish.liveDishId() == null) {
            long publishedCount = liveRepo.countByRestaurantIdAndStatus(
                    restaurantId,
                    DishStatus.PUBLISHED
            );

            if (publishedCount >= 10) {
                throw new IllegalStateException(
                        "Cannot publish more than 10 dishes. Current: " + publishedCount
                );
            }
        }

        dish.publish();
        return updatePort.save(dish);
    }

    @Override
    public Dish unpublishDish(UUID restaurantId, UUID dishId) {
        Dish dish = loadPort.loadById(restaurantId, dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

        dish.unpublish();
        return updatePort.save(dish);
    }
}
