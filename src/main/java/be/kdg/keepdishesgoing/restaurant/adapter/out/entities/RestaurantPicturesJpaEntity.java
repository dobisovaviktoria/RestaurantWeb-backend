package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "restaurant_pictures", schema = "kdg_restaurant")
public class RestaurantPicturesJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private UUID restaurantId;

    public RestaurantPicturesJpaEntity(String url, UUID restaurantId) {
        this.id = UUID.randomUUID();
        this.url = url;
        this.restaurantId = restaurantId;
    }

    public RestaurantPicturesJpaEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
