package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DraftDishProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DraftDishProjectionRepository extends JpaRepository<DraftDishProjection, UUID> {
    List<DraftDishProjection> findByRestaurantId(UUID restaurantId);
}
