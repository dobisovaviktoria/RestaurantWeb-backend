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
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:5173")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final UpdateOpeningHoursUseCase updateOpeningHoursUseCase;
    private final ManualRestaurantStatusUseCase manualRestaurantStatusUseCase;
    private final RestaurantLoadPort restaurantLoadPort;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase, UpdateOpeningHoursUseCase updateOpeningHoursUseCase, ManualRestaurantStatusUseCase manualRestaurantStatusUseCase, RestaurantLoadPort restaurantLoadPort) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateOpeningHoursUseCase = updateOpeningHoursUseCase;
        this.manualRestaurantStatusUseCase = manualRestaurantStatusUseCase;
        this.restaurantLoadPort = restaurantLoadPort;
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
                request.contactEmail(),
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

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable UUID restaurantId) throws RestaurantNotFoundException {
        return restaurantLoadPort.findById(restaurantId)
                .map(RestaurantDtoMapper::fromDomain)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + restaurantId));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        List<RestaurantDto> restaurants = restaurantLoadPort.findAll().stream()
                .map(RestaurantDtoMapper::fromDomain)
                .toList();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/owners/{ownerId}")
    public ResponseEntity<RestaurantDto> getByOwnerId(@PathVariable String ownerId) {
        return restaurantLoadPort.findByOwnerId(ownerId)
                .map(RestaurantDtoMapper::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
