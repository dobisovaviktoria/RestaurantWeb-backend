package be.kdg.keepdishesgoing.restaurant.domain;

import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.restaurant.domain.events.*;
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
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Dish(UUID id, UUID restaurantId, String name, DishType dishType,
                 List<FoodTag> foodTags, String description, float price,
                 String imageUrl, DishStatus status, StockStatus stockStatus,
                 UUID liveDishId) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.dishType = dishType;
        this.foodTags = foodTags == null ? List.of() : List.copyOf(foodTags);
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.stockStatus = stockStatus;
        this.liveDishId = liveDishId;
    }

    public static Dish createNewDraft(UUID restaurantId, String name, DishType dishType,
                                      List<FoodTag> tags, String description,
                                      float price, String imageUrl) {
        validateDishData(restaurantId, name, price);

        UUID draftId = UUID.randomUUID();
        Dish dish = new Dish(draftId, restaurantId, name, dishType, tags,
                description, price, imageUrl,
                DishStatus.DRAFT, StockStatus.IN_STOCK, null);

        dish.domainEvents.add(new DishDraftCreatedEvent(
                restaurantId, draftId, null, name, dishType, tags,
                description, price, imageUrl
        ));

        return dish;
    }

    public static Dish createDraftFromPublished(Dish publishedDish, String name,
                                                DishType dishType, List<FoodTag> tags,
                                                String description, float price,
                                                String imageUrl) {
        if (publishedDish.status != DishStatus.PUBLISHED) {
            throw new IllegalStateException("Can only create draft from published dish");
        }

        UUID draftId = UUID.randomUUID();
        Dish draft = new Dish(draftId, publishedDish.restaurantId, name, dishType,
                tags, description, price, imageUrl,
                DishStatus.DRAFT, StockStatus.IN_STOCK,
                publishedDish.id);

        draft.domainEvents.add(new DishDraftCreatedEvent(
                publishedDish.restaurantId, draftId, publishedDish.id,
                name, dishType, tags, description, price, imageUrl
        ));

        return draft;
    }

    public static Dish fromPersistence(UUID id, UUID restaurantId, String name,
                                       DishType dishType, List<FoodTag> foodTags,
                                       String description, float price, String imageUrl,
                                       DishStatus status, StockStatus stockStatus,
                                       UUID liveDishId) {
        return new Dish(id, restaurantId, name, dishType, foodTags,
                description, price, imageUrl, status, stockStatus, liveDishId);
    }

    public void updateDraft(String name, DishType dishType, List<FoodTag> tags,
                            String description, float price, String imageUrl) {
        if (this.status != DishStatus.DRAFT) {
            throw new IllegalStateException("Can only update drafts");
        }

        validateDishData(this.restaurantId, name, price);

        this.name = name;
        this.dishType = dishType;
        this.foodTags = tags == null ? List.of() : List.copyOf(tags);
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;

        this.domainEvents.add(new DishDraftUpdatedEvent(
                this.id, name, dishType, this.foodTags, description, price, imageUrl
        ));
    }

    public void publish() {
        if (this.status == DishStatus.PUBLISHED) {
            throw new IllegalStateException("Dish is already published");
        }
        UUID targetLiveId = this.liveDishId != null ? this.liveDishId : this.id;

        this.status = DishStatus.PUBLISHED;
        this.domainEvents.add(new DishPublishedEvent(
                this.restaurantId, this.id, targetLiveId
        ));
    }

    public void unpublish() {
        if (this.status != DishStatus.PUBLISHED) {
            throw new IllegalStateException("Can only unpublish published dishes");
        }

        this.status = DishStatus.UNPUBLISHED;
        UUID liveId = this.liveDishId != null ? this.liveDishId : this.id;

        this.domainEvents.add(new DishUnpublishedEvent(this.restaurantId, liveId));
    }

    public void markOutOfStock() {
        if (this.status != DishStatus.PUBLISHED) {
            throw new IllegalStateException("Only published dishes can be out of stock");
        }

        if (this.stockStatus == StockStatus.OUT_OF_STOCK) {
            return;
        }

        this.stockStatus = StockStatus.OUT_OF_STOCK;
        UUID liveId = this.liveDishId != null ? this.liveDishId : this.id;

        this.domainEvents.add(new DishMarkedOutOfStockEvent(this.restaurantId, liveId));
    }

    public void markInStock() {
        if (this.status != DishStatus.PUBLISHED) {
            throw new IllegalStateException("Only published dishes can be marked in stock");
        }

        if (this.stockStatus == StockStatus.IN_STOCK) {
            return;
        }

        this.stockStatus = StockStatus.IN_STOCK;
        UUID liveId = this.liveDishId != null ? this.liveDishId : this.id;

        this.domainEvents.add(new DishMarkedInStockEvent(this.restaurantId, liveId));
    }

    private static void validateDishData(UUID restaurantId, String name, float price) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dish name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public UUID id() { return id; }
    public UUID restaurantId() { return restaurantId; }
    public String name() { return name; }
    public DishType dishType() { return dishType; }
    public List<FoodTag> foodTags() { return List.copyOf(foodTags); }
    public String description() { return description; }
    public float price() { return price; }
    public String imageUrl() { return imageUrl; }
    public DishStatus status() { return status; }
    public StockStatus stockStatus() { return stockStatus; }
    public UUID liveDishId() { return liveDishId; }
}