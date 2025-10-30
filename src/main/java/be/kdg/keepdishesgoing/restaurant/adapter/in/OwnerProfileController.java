package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.RestaurantJpaRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import be.kdg.keepdishesgoing.restaurant.port.in.GetOrCreateOwnerUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/owner")
public class OwnerProfileController {

    private final GetOrCreateOwnerUseCase getOrCreateOwnerUseCase;
    private final RestaurantJpaRepository restaurantRepo;

    public OwnerProfileController(
            GetOrCreateOwnerUseCase getOrCreateOwnerUseCase,
            RestaurantJpaRepository restaurantRepo) {
        this.getOrCreateOwnerUseCase = getOrCreateOwnerUseCase;
        this.restaurantRepo = restaurantRepo;
    }

    @GetMapping("/profile")
    public ResponseEntity<OwnerProfileDto> getProfile(@AuthenticationPrincipal Jwt jwt) {
        Owner owner = getOrCreateOwnerUseCase.getOrCreateOwner(jwt);

        RestaurantJpaEntity restaurant = restaurantRepo.findByOwnerId(owner.id())
                .orElse(null);

        OwnerProfileDto dto = new OwnerProfileDto(
                owner.id(),
                owner.email(),
                owner.name(),
                restaurant != null ? restaurant.getId() : null
        );

        return ResponseEntity.ok(dto);
    }

    public record OwnerProfileDto(
            String ownerId,
            String email,
            String name,
            UUID restaurantId
    ) {}
}
