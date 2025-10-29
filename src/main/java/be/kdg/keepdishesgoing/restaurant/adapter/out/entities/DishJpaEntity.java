package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.domain.DishType;
import be.kdg.keepdishesgoing.restaurant.domain.StockStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dishes", schema = "kdg_restaurant")
public class DishJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
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

    private UUID liveDishId;

    public DishJpaEntity() {}

    public DishJpaEntity(UUID id, UUID restaurantId, String name, DishType dishType, String description,
                         float price, String imageUrl, DishStatus status, StockStatus stockStatus, UUID liveDishId) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.dishType = dishType;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.stockStatus = stockStatus;
        this.liveDishId = liveDishId;
    }

    public static DishJpaEntity fromDomain(be.kdg.keepdishesgoing.restaurant.domain.Dish dish) {
        return new DishJpaEntity(dish.id(), dish.restaurantId(), dish.name(), dish.dishType(),
                dish.description(), dish.price(), dish.imageUrl(), dish.status(), dish.stockStatus(), dish.liveDishId());
    }

    public UUID getId() { return id; }
    public UUID getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public DishType getDishType() { return dishType; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public DishStatus getStatus() { return status; }
    public StockStatus getStockStatus() { return stockStatus; }
    public UUID getLiveDishId() { return liveDishId; }

    public void setId(UUID id) { this.id = id; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public void setName(String name) { this.name = name; }
    public void setDishType(DishType dishType) { this.dishType = dishType; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(float price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setStatus(DishStatus status) { this.status = status; }
    public void setStockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; }
    public void setLiveDishId(UUID liveDishId) { this.liveDishId = liveDishId; }
}
