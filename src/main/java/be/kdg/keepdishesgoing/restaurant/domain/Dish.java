package be.kdg.keepdishesgoing.restaurant.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dish {
    private final UUID id;
    private final UUID restaurantId;
    private String name;
    private DishType dishType;
    private List<FoodTag> foodTags;
    private String description;
    private float price;
    private String imageUrl;
    private DishStatus status;
    private StockStatus stockStatus;
    private UUID liveDishId;

    private Dish(UUID id, UUID restaurantId, String name, DishType dishType, List<FoodTag> foodTags, String description,
                 float price, String imageUrl, DishStatus status, StockStatus stockStatus, UUID liveDishId) {
        if (restaurantId == null) throw new IllegalArgumentException("Restaurant ID cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Dish name cannot be empty");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");

        this.id = id == null ? UUID.randomUUID() : id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.dishType = dishType;
        this.foodTags = foodTags == null ? List.of() : List.copyOf(foodTags);
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status == null ? DishStatus.DRAFT : status;
        this.stockStatus = stockStatus == null ? StockStatus.IN_STOCK : stockStatus;
        this.liveDishId = liveDishId;
    }

    public static Dish createNewDraft(UUID restaurantId, String name, DishType dishType, List<FoodTag> foodTags,
                                      String description, float price, String imageUrl) {
        return new Dish(null, restaurantId, name, dishType, foodTags, description, price, imageUrl,
                DishStatus.DRAFT, StockStatus.IN_STOCK, null);
    }

    public static Dish createDraftFromPublished(Dish liveDish, String name, DishType dishType, List<FoodTag> foodTags,
                                                String description, float price, String imageUrl) {
        return new Dish(UUID.randomUUID(), liveDish.restaurantId, name, dishType, foodTags, description, price,
                imageUrl, DishStatus.DRAFT, StockStatus.IN_STOCK, liveDish.id);
    }

    public static Dish fromPersistence(UUID id, UUID restaurantId, String name, DishType dishType, List<FoodTag> foodTags,
                                       String description, float price, String imageUrl, DishStatus status,
                                       StockStatus stockStatus, UUID liveDishId) {
        return new Dish(id, restaurantId, name, dishType, foodTags, description, price, imageUrl,
                status, stockStatus, liveDishId);
    }

    public void applyDraftTo(Dish liveDish) {
        if (status != DishStatus.DRAFT) throw new IllegalStateException("Only drafts can be applied");

        liveDish.name = this.name;
        liveDish.description = this.description;
        liveDish.dishType = this.dishType;
        liveDish.foodTags = new ArrayList<>(this.foodTags);
        liveDish.price = this.price;
        liveDish.imageUrl = this.imageUrl;
        liveDish.status = DishStatus.PUBLISHED;
    }

    public void publish() { if (status != DishStatus.PUBLISHED) status = DishStatus.PUBLISHED; }
    public void unpublish() { if (status != DishStatus.UNPUBLISHED) status = DishStatus.UNPUBLISHED; }
    public void markOutOfStock() { stockStatus = StockStatus.OUT_OF_STOCK; }
    public void markInStock() { stockStatus = StockStatus.IN_STOCK; }

    public UUID id() { return id; }
    public UUID restaurantId() { return restaurantId; }
    public String name() { return name; }
    public DishType dishType() { return dishType; }
    public List<FoodTag> foodTags() { return foodTags; }
    public String description() { return description; }
    public float price() { return price; }
    public String imageUrl() { return imageUrl; }
    public DishStatus status() { return status; }
    public StockStatus stockStatus() { return stockStatus; }
    public UUID liveDishId() { return liveDishId; }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", type=" + dishType +
                ", price=" + price +
                ", status=" + status +
                ", stock=" + stockStatus +
                ", restaurantId=" + restaurantId +
                '}';
    }
}