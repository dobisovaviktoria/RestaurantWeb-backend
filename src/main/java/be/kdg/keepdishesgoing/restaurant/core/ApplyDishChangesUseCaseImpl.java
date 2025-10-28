package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DraftDishProjection;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.DraftDishProjectionRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.LiveDishProjectionRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.port.in.ApplyDishChangesUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.UpdateDishPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApplyDishChangesUseCaseImpl implements ApplyDishChangesUseCase {

    private final LoadDishPort loadPort;
    private final UpdateDishPort updatePort;
    private final DraftDishProjectionRepository draftRepo;
    private final LiveDishProjectionRepository liveRepo;

    public ApplyDishChangesUseCaseImpl(LoadDishPort loadPort,
                                       UpdateDishPort updatePort,
                                       DraftDishProjectionRepository draftRepo,
                                       LiveDishProjectionRepository liveRepo) {
        this.loadPort = loadPort;
        this.updatePort = updatePort;
        this.draftRepo = draftRepo;
        this.liveRepo = liveRepo;
    }

    @Override
    public void applyAllPendingChanges(UUID restaurantId) {
        List<DraftDishProjection> drafts = draftRepo.findByRestaurantId(restaurantId);

        long newDishesCount = drafts.stream()
                .filter(draft -> draft.getLiveDishId() == null)
                .count();

        long currentPublished = liveRepo.countByRestaurantIdAndStatus(
                restaurantId,
                DishStatus.PUBLISHED
        );

        if (currentPublished + newDishesCount > 10) {
            throw new IllegalStateException(
                    String.format("Cannot publish. Would result in %d dishes (limit: 10). " +
                                    "Current: %d, New: %d",
                            currentPublished + newDishesCount, currentPublished, newDishesCount)
            );
        }

        for (DraftDishProjection draftProjection : drafts) {
            Dish dish = loadPort.loadById(restaurantId, draftProjection.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Draft not found: " + draftProjection.getId()
                    ));

            dish.publish();
            updatePort.save(dish);
        }
    }
}
