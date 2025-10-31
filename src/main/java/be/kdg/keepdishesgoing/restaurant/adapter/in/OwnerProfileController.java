package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@CrossOrigin(origins = "http://localhost:5173")
public class OwnerProfileController {
    private final RestaurantLoadPort restaurantLoadPort;

    public OwnerProfileController(RestaurantLoadPort restaurantLoadPort) {
        this.restaurantLoadPort = restaurantLoadPort;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt,
                                        @RequestHeader("Authorization") String authHeader) {

        if (jwt == null) {
            return ResponseEntity.status(401).body("No JWT token");
        }

        String ownerId = jwt.getSubject();
        String email = jwt.getClaim("contactEmail");
        String name = jwt.getClaim("given_name");

        String restaurantId = restaurantLoadPort.findByOwnerId(ownerId)
                .map(restaurant -> restaurant.id().toString())
                .orElse(null);

        OwnerProfileDto dto = new OwnerProfileDto(ownerId, email, name, restaurantId);

        return ResponseEntity.ok(dto);
    }

    public record OwnerProfileDto(String ownerId, String email, String name, String restaurantId) {}
}
