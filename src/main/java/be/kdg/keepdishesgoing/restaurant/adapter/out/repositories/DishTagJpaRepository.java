package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DishTagJpaEntity;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DishTagJpaRepository extends JpaRepository<DishTagJpaEntity, UUID> {
    List<DishTagJpaEntity> findByDishId(UUID dishId);
    List<DishTagJpaEntity> findByDishIdInAndTag(List<UUID> dishIds, FoodTag tag);
    void deleteByDishId(UUID dishId);
}
