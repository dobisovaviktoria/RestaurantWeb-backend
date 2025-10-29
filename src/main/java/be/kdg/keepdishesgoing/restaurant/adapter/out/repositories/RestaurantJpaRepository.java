package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {
    Optional<RestaurantJpaEntity> findByName(String name);

    @Query("SELECT r FROM RestaurantJpaEntity r WHERE r.ownerId = :ownerId")
    Optional<RestaurantJpaEntity> findByOwnerId(String ownerId);
}