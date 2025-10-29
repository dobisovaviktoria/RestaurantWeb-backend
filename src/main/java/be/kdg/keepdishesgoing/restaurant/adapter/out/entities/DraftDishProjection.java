package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "draft_dishes", schema = "kdg_restaurant")
public class DraftDishProjection {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    private UUID liveDishId;

    private String name;

    @Enumerated(EnumType.STRING)
    private DishType dishType;

    private String description;
    private float price;
    private String imageUrl;

    public DraftDishProjection() {}

    public DraftDishProjection(UUID id, UUID restaurantId, UUID liveDishId, String name, DishType dishType,
                               String description, float price, String imageUrl) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.liveDishId = liveDishId;
        this.name = name;
        this.dishType = dishType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public UUID getLiveDishId() { return liveDishId; }
    public void setLiveDishId(UUID liveDishId) { this.liveDishId = liveDishId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DishType getDishType() { return dishType; }
    public void setDishType(DishType dishType) { this.dishType = dishType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
