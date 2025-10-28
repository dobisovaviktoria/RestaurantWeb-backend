package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.*;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.*;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.domain.FoodTag;
import be.kdg.keepdishesgoing.restaurant.domain.StockStatus;
import be.kdg.keepdishesgoing.restaurant.domain.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class DishProjectionListener {

    private static final Logger log = LoggerFactory.getLogger(DishProjectionListener.class);

    private final DraftDishProjectionRepository draftRepo;
    private final LiveDishProjectionRepository liveRepo;
    private final DraftDishTagProjectionRepository draftTagRepo;
    private final LiveDishTagProjectionRepository liveTagRepo;

    public DishProjectionListener(DraftDishProjectionRepository draftRepo,
                                  LiveDishProjectionRepository liveRepo,
                                  DraftDishTagProjectionRepository draftTagRepo,
                                  LiveDishTagProjectionRepository liveTagRepo) {
        this.draftRepo = draftRepo;
        this.liveRepo = liveRepo;
        this.draftTagRepo = draftTagRepo;
        this.liveTagRepo = liveTagRepo;
    }

    @EventListener
    @Transactional
    public void on(DishDraftCreatedEvent event) {
        log.info("Projecting draft created: {}", event.draftId());

        DraftDishProjection draft = new DraftDishProjection(
                event.draftId(),
                event.restaurantId(),
                event.liveDishId(),
                event.name(),
                event.dishType(),
                event.description(),
                event.price(),
                event.imageUrl()
        );
        draftRepo.save(draft);
        saveDraftTags(event.draftId(), event.foodTags());
    }

    @EventListener
    @Transactional
    public void on(DishDraftUpdatedEvent event) {
        log.info("Projecting draft updated: {}", event.draftId());

        draftRepo.findById(event.draftId()).ifPresent(draft -> {
            draft.setName(event.name());
            draft.setDishType(event.dishType());
            draft.setDescription(event.description());
            draft.setPrice(event.price());
            draft.setImageUrl(event.imageUrl());
            draftRepo.save(draft);
            saveDraftTags(event.draftId(), event.foodTags());
        });
    }

    @EventListener
    @Transactional
    public void on(DishPublishedEvent event) {
        log.info("Projecting dish published: draft={}, live={}",
                event.draftId(), event.liveDishId());

        draftRepo.findById(event.draftId()).ifPresent(draft -> {
            LiveDishProjection live = liveRepo.findById(event.liveDishId())
                    .orElse(new LiveDishProjection());

            live.setId(event.liveDishId());
            live.setRestaurantId(draft.getRestaurantId());
            live.setName(draft.getName());
            live.setDishType(draft.getDishType());
            live.setDescription(draft.getDescription());
            live.setPrice(draft.getPrice());
            live.setImageUrl(draft.getImageUrl());
            live.setStatus(DishStatus.PUBLISHED);
            live.setStockStatus(StockStatus.IN_STOCK);
            liveRepo.save(live);

            List<String> tags = draftTagRepo.findByDraftDishId(draft.getId())
                    .stream()
                    .map(DraftDishTagProjection::getTag)
                    .toList();
            saveLiveTags(event.liveDishId(), tags);

            draftTagRepo.deleteByDraftDishId(draft.getId());
            draftRepo.deleteById(draft.getId());
        });
    }

    @EventListener
    @Transactional
    public void on(DishUnpublishedEvent event) {
        log.info("Projecting dish unpublished: {}", event.liveDishId());

        liveTagRepo.deleteByLiveDishId(event.liveDishId());
        liveRepo.deleteById(event.liveDishId());
    }

    @EventListener
    @Transactional
    public void on(DishMarkedOutOfStockEvent event) {
        log.info("Projecting out of stock: {}", event.dishId());

        liveRepo.findById(event.dishId()).ifPresent(live -> {
            live.setStockStatus(StockStatus.OUT_OF_STOCK);
            liveRepo.save(live);
        });
    }

    @EventListener
    @Transactional
    public void on(DishMarkedInStockEvent event) {
        log.info("Projecting in stock: {}", event.dishId());

        liveRepo.findById(event.dishId()).ifPresent(live -> {
            live.setStockStatus(StockStatus.IN_STOCK);
            liveRepo.save(live);
        });
    }

    private void saveDraftTags(UUID draftId, List<FoodTag> tags) {
        draftTagRepo.deleteByDraftDishId(draftId);
        if (tags != null && !tags.isEmpty()) {
            List<DraftDishTagProjection> tagEntities = tags.stream()
                    .map(tag -> new DraftDishTagProjection(UUID.randomUUID(), draftId, tag.name()))
                    .toList();
            draftTagRepo.saveAll(tagEntities);
        }
    }

    private void saveLiveTags(UUID liveId, List<String> tags) {
        liveTagRepo.deleteByLiveDishId(liveId);
        if (tags != null && !tags.isEmpty()) {
            List<LiveDishTagProjection> tagEntities = tags.stream()
                    .map(tag -> new LiveDishTagProjection(UUID.randomUUID(), liveId, tag))
                    .toList();
            liveTagRepo.saveAll(tagEntities);
        }
    }
}
