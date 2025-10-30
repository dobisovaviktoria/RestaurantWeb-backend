package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@CrossOrigin(origins = "http://localhost:5173")
public class OwnerProfileController {
    private static final Logger logger = LoggerFactory.getLogger(OwnerProfileController.class);
    private final RestaurantLoadPort restaurantLoadPort;

    public OwnerProfileController(RestaurantLoadPort restaurantLoadPort) {
        this.restaurantLoadPort = restaurantLoadPort;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt,
                                        @RequestHeader("Authorization") String authHeader) {
        logger.info("=== PROFILE REQUEST ===");
        logger.info("Authorization Header: {}", authHeader);

        if (jwt == null) {
            logger.error("❌ No JWT token provided");
            return ResponseEntity.status(401).body("No JWT token");
        }

        logger.info("✅ JWT Subject: {}", jwt.getSubject());
        logger.info("✅ JWT Claims: {}", jwt.getClaims());

        String ownerId = jwt.getSubject();
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("given_name");

        logger.info("Owner ID: {}, Email: {}, Name: {}", ownerId, email, name);

        String restaurantId = restaurantLoadPort.findByOwnerId(ownerId)
                .map(restaurant -> restaurant.id().toString())
                .orElse(null);

        OwnerProfileDto dto = new OwnerProfileDto(ownerId, email, name, restaurantId);
        logger.info("✅ Returning profile: {}", dto);

        return ResponseEntity.ok(dto);
    }

    public record OwnerProfileDto(String ownerId, String email, String name, String restaurantId) {}
}
