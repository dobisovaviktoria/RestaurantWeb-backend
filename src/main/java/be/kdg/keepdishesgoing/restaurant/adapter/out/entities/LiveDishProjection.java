package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.StockStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "live_dishes", schema = "kdg_restaurant")
public class LiveDishProjection {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    private String name;

    @Enumerated(EnumType.STRING)
    private DishType dishType;

    private String description;
    private float price;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DishStatus status;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    public LiveDishProjection() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
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
    public DishStatus getStatus() { return status; }
    public void setStatus(DishStatus status) { this.status = status; }
    public StockStatus getStockStatus() { return stockStatus; }
    public void setStockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; }
}
