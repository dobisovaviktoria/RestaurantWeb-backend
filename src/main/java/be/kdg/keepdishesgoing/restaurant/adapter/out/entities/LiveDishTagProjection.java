package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "live_dish_tags", schema = "kdg_restaurant")
public class LiveDishTagProjection {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID liveDishId;

    @Column(nullable = false)
    private String tag;

    public LiveDishTagProjection() {}

    public LiveDishTagProjection(UUID id, UUID liveDishId, String tag) {
        this.id = id;
        this.liveDishId = liveDishId;
        this.tag = tag;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getLiveDishId() { return liveDishId; }
    public void setLiveDishId(UUID liveDishId) { this.liveDishId = liveDishId; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
