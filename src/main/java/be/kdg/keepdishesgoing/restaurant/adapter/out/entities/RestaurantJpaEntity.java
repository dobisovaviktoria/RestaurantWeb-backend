package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import be.kdg.keepdishesgoing.restaurant.domain.CuisineType;
import be.kdg.keepdishesgoing.restaurant.domain.RestaurantStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "restaurants", schema = "kdg_restaurant")
public class RestaurantJpaEntity {

    @Id
    private UUID id;

    private String name;
    private String email;
    private float preparationTime;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @Column(nullable = false, unique = true)
    private String ownerId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    public RestaurantJpaEntity(UUID id, String name, String email, float preparationTime, CuisineType cuisineType, String ownerId,  RestaurantStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preparationTime = preparationTime;
        this.cuisineType = cuisineType;
        this.ownerId = ownerId;
        this.status = status;
    }

    public RestaurantJpaEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(float preparationTime) {
        this.preparationTime = preparationTime;
    }

    public CuisineType getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(CuisineType cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }
}
