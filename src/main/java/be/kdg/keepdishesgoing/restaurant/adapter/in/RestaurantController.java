package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.in.request.CreateRestaurantRequest;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.RestaurantDto;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.RestaurantDtoMapper;
import be.kdg.keepdishesgoing.restaurant.domain.Address;
import be.kdg.keepdishesgoing.restaurant.domain.OpeningHours;
import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import be.kdg.keepdishesgoing.restaurant.domain.RestaurantStatus;
import be.kdg.keepdishesgoing.restaurant.domain.exceptions.OwnerAlreadyHasRestaurantException;
import be.kdg.keepdishesgoing.restaurant.domain.exceptions.RestaurantNotFoundException;
import be.kdg.keepdishesgoing.restaurant.port.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final UpdateOpeningHoursUseCase updateOpeningHoursUseCase;
    private final ManualRestaurantStatusUseCase manualRestaurantStatusUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase, UpdateOpeningHoursUseCase updateOpeningHoursUseCase, ManualRestaurantStatusUseCase manualRestaurantStatusUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateOpeningHoursUseCase = updateOpeningHoursUseCase;
        this.manualRestaurantStatusUseCase = manualRestaurantStatusUseCase;
    }

    @PostMapping()
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody CreateRestaurantRequest request) throws OwnerAlreadyHasRestaurantException {
        Address address = new Address(
                request.address().street(),
                request.address().number(),
                request.address().city(),
                request.address().zipcode(),
                request.address().country()
        );

        List<OpeningHours> openingHours = request.openingHours().stream()
                .map(ohr -> OpeningHours.create(
                        ohr.dayOfWeek(),
                        ohr.openingTime(),
                        ohr.closingTime()
                ))
                .toList();

        CreateRestaurantCommand command = new CreateRestaurantCommand(
                request.ownerId(),
                request.name(),
                address,
                request.email(),
                request.pictures(),
                request.cuisineType(),
                request.preparationTime(),
                openingHours
        );

        Restaurant restaurant = createRestaurantUseCase.createRestaurant(command);
        RestaurantDto dto = RestaurantDtoMapper.fromDomain(restaurant);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{restaurantId}/opening-hours")
    public ResponseEntity<Void> updateOpeningHours(
            @PathVariable UUID restaurantId,
            @RequestBody List<OpeningHours> openingHours) throws RestaurantNotFoundException {
        updateOpeningHoursUseCase.updateOpeningHours(
                new UpdateOpeningHoursCommand(restaurantId, openingHours));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{restaurantId}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID restaurantId,
            @RequestParam RestaurantStatus status) throws RestaurantNotFoundException {
        manualRestaurantStatusUseCase.changeStatus(
                new ChangeRestaurantStatusCommand(restaurantId, status));
        return ResponseEntity.noContent().build();
    }
}
