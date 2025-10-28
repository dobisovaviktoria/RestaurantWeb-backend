package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.DraftDishProjection;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.LiveDishProjection;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.DraftDishProjectionRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.LiveDishProjectionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants/{restaurantId}")
public class DishProjectionController {

    private final DraftDishProjectionRepository draftRepo;
    private final LiveDishProjectionRepository liveRepo;

    public DishProjectionController(DraftDishProjectionRepository draftRepo,
                               LiveDishProjectionRepository liveRepo) {
        this.draftRepo = draftRepo;
        this.liveRepo = liveRepo;
    }

    @GetMapping("/dishes/drafts")
    public ResponseEntity<List<DraftDishProjection>> getDrafts(
            @PathVariable UUID restaurantId) {

        List<DraftDishProjection> drafts = draftRepo.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(drafts);
    }

    @GetMapping("/dishes/drafts/count")
    public ResponseEntity<Long> countDrafts(@PathVariable UUID restaurantId) {
        long count = draftRepo.findByRestaurantId(restaurantId).size();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<LiveDishProjection>> getPublishedMenu(
            @PathVariable UUID restaurantId) {

        List<LiveDishProjection> menu = liveRepo.findByRestaurantIdAndStatus(
                restaurantId,
                be.kdg.keepdishesgoing.restaurant.domain.DishStatus.PUBLISHED
        );
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/dishes/published/count")
    public ResponseEntity<Long> countPublished(@PathVariable UUID restaurantId) {
        long count = liveRepo.countByRestaurantIdAndStatus(
                restaurantId,
                be.kdg.keepdishesgoing.restaurant.domain.DishStatus.PUBLISHED
        );
        return ResponseEntity.ok(count);
    }
}
