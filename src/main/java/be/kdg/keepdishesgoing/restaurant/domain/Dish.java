package be.kdg.keepdishesgoing.restaurant.domain;

import java.util.List;
import java.util.UUID;

public class Dish {
    private final UUID id;
    private final String name;
    private final DishType dishType;
    private final List<Dish> subDishes;
    private final String description;
    private final float price;
    private final String imageUrl;

    private Dish(UUID id, String name, DishType dishType, List<Dish> subDishes, String description, float price, String imageUrl) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Dish name cannot be empty");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");

        this.id = id == null ? UUID.randomUUID() : id;
        this.name = name;
        this.dishType = dishType;
        this.subDishes = subDishes == null ? List.of() : List.copyOf(subDishes);
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Dish create(String name, DishType dishType, List<Dish> subDishes, String description, float price, String imageUrl) {
        return new Dish(null, name, dishType, subDishes, description, price, imageUrl);
    }

    public static Dish fromPersistence(UUID id, String name, DishType dishType, List<Dish> subDishes, String description, float price, String imageUrl) {
        return new Dish(id, name, dishType, subDishes, description, price, imageUrl);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public DishType dishType() {
        return dishType;
    }

    public List<Dish> subDishes() {
        return subDishes;
    }

    public String description() {
        return description;
    }

    public float price() {
        return price;
    }

    public String imageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", type=" + dishType +
                ", price=" + price +
                '}';
    }
}