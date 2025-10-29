package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DishJpaEntity;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DishJpaRepository extends JpaRepository<DishJpaEntity, UUID> {
    List<DishJpaEntity> findByRestaurantIdAndStatus(UUID restaurantId, DishStatus status);
}
