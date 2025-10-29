package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "draft_dish_tags", schema = "kdg_restaurant")
public class DraftDishTagProjection {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID draftDishId;

    @Column(nullable = false)
    private String tag;

    public DraftDishTagProjection() {}

    public DraftDishTagProjection(UUID id, UUID draftDishId, String tag) {
        this.id = id;
        this.draftDishId = draftDishId;
        this.tag = tag;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDraftDishId() { return draftDishId; }
    public void setDraftDishId(UUID draftDishId) { this.draftDishId = draftDishId; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
