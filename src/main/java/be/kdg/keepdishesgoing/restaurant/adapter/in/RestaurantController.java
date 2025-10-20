package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.adapter.in.request.CreateRestaurantRequest;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.RestaurantDto;
import be.kdg.keepdishesgoing.restaurant.adapter.in.response.RestaurantDtoMapper;
import be.kdg.keepdishesgoing.restaurant.domain.Address;
import be.kdg.keepdishesgoing.restaurant.domain.OpeningHours;
import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import be.kdg.keepdishesgoing.restaurant.domain.exceptions.OwnerAlreadyHasRestaurantException;
import be.kdg.keepdishesgoing.restaurant.port.in.CreateRestaurantCommand;
import be.kdg.keepdishesgoing.restaurant.port.in.CreateRestaurantUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;

@RestController
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
    }

    @PostMapping("/restaurant")
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
}
