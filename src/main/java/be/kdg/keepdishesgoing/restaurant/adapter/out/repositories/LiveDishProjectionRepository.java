package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.LiveDishProjection;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiveDishProjectionRepository extends JpaRepository<LiveDishProjection, UUID> {
    List<LiveDishProjection> findByRestaurantIdAndStatus(UUID restaurantId, DishStatus status);
    long countByRestaurantIdAndStatus(UUID restaurantId, DishStatus status);
}
