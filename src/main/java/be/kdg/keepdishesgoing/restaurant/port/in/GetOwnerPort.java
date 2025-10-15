package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface GetOwnerPort {
    Owner getOrCreateOwner(OAuth2User user);
}
