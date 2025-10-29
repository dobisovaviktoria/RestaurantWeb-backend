package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantPicturesJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantPicturesRepository extends JpaRepository<RestaurantPicturesJpaEntity, UUID> {
    List<RestaurantPicturesJpaEntity> findAllByRestaurantId(UUID restaurantId);
}
