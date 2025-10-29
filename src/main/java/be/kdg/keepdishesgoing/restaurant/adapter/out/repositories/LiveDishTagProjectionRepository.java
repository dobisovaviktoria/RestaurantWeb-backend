package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.LiveDishTagProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiveDishTagProjectionRepository extends JpaRepository<LiveDishTagProjection, UUID> {
    List<LiveDishTagProjection> findByLiveDishId(UUID liveDishId);
    void deleteByLiveDishId(UUID liveDishId);
}
