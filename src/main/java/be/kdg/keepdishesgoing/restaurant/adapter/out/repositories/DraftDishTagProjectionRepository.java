package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DraftDishTagProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DraftDishTagProjectionRepository extends JpaRepository<DraftDishTagProjection, UUID> {
    List<DraftDishTagProjection> findByDraftDishId(UUID draftDishId);
    void deleteByDraftDishId(UUID draftDishId);
}
