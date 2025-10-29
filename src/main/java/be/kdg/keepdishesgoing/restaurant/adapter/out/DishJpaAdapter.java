package be.kdg.keepdishesgoing.restaurant.adapter.out;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DishJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DishTagJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.DishJpaRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.DishTagJpaRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.UpdateDishPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DishJpaAdapter implements UpdateDishPort, LoadDishPort {

    private final DishJpaRepository dishRepo;
    private final DishTagJpaRepository tagRepo;
    private final ApplicationEventPublisher eventPublisher;

    public DishJpaAdapter(DishJpaRepository dishRepo,
                          DishTagJpaRepository tagRepo,
                          ApplicationEventPublisher eventPublisher) {
        this.dishRepo = dishRepo;
        this.tagRepo = tagRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Dish save(Dish dish) {
        DishJpaEntity entity = DishJpaEntity.fromDomain(dish);
        dishRepo.save(entity);

        tagRepo.deleteByDishId(dish.id());
        List<DishTagJpaEntity> tags = dish.foodTags().stream()
                .map(tag -> new DishTagJpaEntity(UUID.randomUUID(), dish.id(), tag))
                .toList();
        if (!tags.isEmpty()) {
            tagRepo.saveAll(tags);
        }

        dish.getDomainEvents().forEach(eventPublisher::publishEvent);
        dish.clearDomainEvents();

        return dish;
    }

    @Override
    public Optional<Dish> loadById(UUID restaurantId, UUID dishId) {
        return dishRepo.findById(dishId)
                .map(this::toDomain);
    }

    @Override
    public List<Dish> loadDrafts(UUID restaurantId) {
        return dishRepo.findByRestaurantIdAndStatus(restaurantId, DishStatus.DRAFT)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Dish> loadPublished(UUID restaurantId) {
        return dishRepo.findByRestaurantIdAndStatus(restaurantId, DishStatus.PUBLISHED)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Dish toDomain(DishJpaEntity entity) {
        List<FoodTag> tags = tagRepo.findByDishId(entity.getId())
                .stream()
                .map(DishTagJpaEntity::getTag)
                .toList();

        return Dish.fromPersistence(
                entity.getId(),
                entity.getRestaurantId(),
                entity.getName(),
                entity.getDishType(),
                tags,
                entity.getDescription(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getStatus(),
                entity.getStockStatus(),
                entity.getLiveDishId()
        );
    }
}