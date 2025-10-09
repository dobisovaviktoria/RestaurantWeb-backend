package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.adapter.out.OwnerJpaEntity;
import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import be.kdg.keepdishesgoing.restaurant.port.in.GetOwnerPort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadOwnerPort;
import be.kdg.keepdishesgoing.restaurant.port.out.SaveOwnerPort;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOwnerUseCaseImpl implements GetOwnerPort {

    private final LoadOwnerPort loadOwnerPort;
    private final SaveOwnerPort saveOwnerPort;

    public GetOwnerUseCaseImpl(LoadOwnerPort loadOwnerPort,  SaveOwnerPort saveOwnerPort) {
        this.loadOwnerPort = loadOwnerPort;
        this.saveOwnerPort = saveOwnerPort;
    }

    @Override
    public Owner getOrCreateOwner(OAuth2User user) {
        String email = user.getAttribute("email");
        return loadOwnerPort.loadByEmail(email)
                .orElseGet(() -> {
                    OwnerJpaEntity owner = new OwnerJpaEntity();
                    owner.setId(UUID.randomUUID().toString());
                    owner.setEmail(email);
                    owner.setName(user.getAttribute("name"));
                    return saveOwnerPort.saveOwner(owner);
                });
    }
}
