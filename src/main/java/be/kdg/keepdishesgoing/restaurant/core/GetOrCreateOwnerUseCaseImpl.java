package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import be.kdg.keepdishesgoing.restaurant.port.in.GetOrCreateOwnerUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadOwnerPort;
import be.kdg.keepdishesgoing.restaurant.port.out.SaveOwnerPort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetOrCreateOwnerUseCaseImpl implements GetOrCreateOwnerUseCase {

    private final LoadOwnerPort loadOwnerPort;
    private final SaveOwnerPort saveOwnerPort;

    public GetOrCreateOwnerUseCaseImpl(LoadOwnerPort loadOwnerPort, SaveOwnerPort saveOwnerPort) {
        this.loadOwnerPort = loadOwnerPort;
        this.saveOwnerPort = saveOwnerPort;
    }

    @Override
    public Owner getOrCreateOwner(Jwt jwt) {
        String sub = jwt.getSubject();
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("preferred_username");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Email claim missing from JWT token");
        }

        return loadOwnerPort.loadById(sub)
                .orElseGet(() -> {
                    Owner owner = Owner.create(sub, email, name != null ? name : email);
                    return saveOwnerPort.saveOwner(owner);
                });
    }
}
