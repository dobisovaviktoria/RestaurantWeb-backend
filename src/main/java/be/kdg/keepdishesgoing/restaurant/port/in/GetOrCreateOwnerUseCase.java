package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import org.springframework.security.oauth2.jwt.Jwt;

public interface GetOrCreateOwnerUseCase {
    Owner getOrCreateOwner(Jwt jwt);
}
