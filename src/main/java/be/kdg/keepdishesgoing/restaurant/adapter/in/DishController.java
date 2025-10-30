package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.in.request.DishDraftRequest;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.DishDto;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.DishDtoMapper;
import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.port.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurants/{restaurantId}/dishes")
public class DishController {

    private final EditDishUseCase editDishUseCase;
    private final PublishDishUseCase publishDishUseCase;
    private final StockDishUseCase stockDishUseCase;
    private final ApplyDishChangesUseCase applyDishChangesUseCase;

    public DishController(EditDishUseCase editDishUseCase,
                          PublishDishUseCase publishDishUseCase,
                          StockDishUseCase stockDishUseCase,
                          ApplyDishChangesUseCase applyDishChangesUseCase) {
        this.editDishUseCase = editDishUseCase;
        this.publishDishUseCase = publishDishUseCase;
        this.stockDishUseCase = stockDishUseCase;
        this.applyDishChangesUseCase = applyDishChangesUseCase;
    }

    @PostMapping("/draft")
    public ResponseEntity<DishDto> createOrUpdateDraft(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody DishDraftRequest request) {

        DishDraftCommand command = new DishDraftCommand(
                restaurantId,
                request.dishId(),
                request.liveDishId(),
                request.name(),
                request.dishType(),
                request.foodTags(),
                request.description(),
                request.price(),
                request.imageUrl()
        );

        Dish dish = editDishUseCase.createOrUpdateDraft(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DishDtoMapper.fromDomain(dish));
    }

    @PostMapping("/{dishId}/publish")
    public ResponseEntity<Void> publishDish(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID dishId) {

        publishDishUseCase.publishDish(restaurantId, dishId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/unpublish")
    public ResponseEntity<Void> unpublishDish(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID dishId) {

        publishDishUseCase.unpublishDish(restaurantId, dishId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/out-of-stock")
    public ResponseEntity<Void> markOutOfStock(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID dishId) {

        stockDishUseCase.markOutOfStock(restaurantId, dishId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dishId}/in-stock")
    public ResponseEntity<Void> markInStock(
            @PathVariable UUID restaurantId,
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID dishId) {

        stockDishUseCase.markBackInStock(restaurantId, dishId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/apply-all")
    public ResponseEntity<Void> applyAllDrafts(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID restaurantId) {

        applyDishChangesUseCase.applyAllPendingChanges(restaurantId);
        return ResponseEntity.ok().build();
    }
}
