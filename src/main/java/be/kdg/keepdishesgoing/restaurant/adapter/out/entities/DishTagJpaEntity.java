package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dish_tags", schema = "kdg_restaurant")
public class DishTagJpaEntity {

    @Id
    private UUID id;

    private UUID dishId;

    @Enumerated(EnumType.STRING)
    private FoodTag tag;

    public DishTagJpaEntity() {}

    public DishTagJpaEntity(UUID id, UUID dishId, FoodTag tag) {
        this.id = id;
        this.dishId = dishId;
        this.tag = tag;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDishId() { return dishId; }
    public void setDishId(UUID dishId) { this.dishId = dishId; }
    public FoodTag getTag() { return tag; }
    public void setTag(FoodTag tag) { this.tag = tag; }
}
