package be.kdg.keepdishesgoing.restaurant.adapter.in;

import be.kdg.keepdishesgoing.restaurant.core.GetOwnerUseCaseImpl;
import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerController {

    private final GetOwnerUseCaseImpl getOwnerUseCase;

    public OwnerController(GetOwnerUseCaseImpl getOwnerUseCase) {
        this.getOwnerUseCase = getOwnerUseCase;
    }

    @GetMapping("/")
    public String profile(@AuthenticationPrincipal OAuth2User principal) {
        Owner owner = getOwnerUseCase.getOrCreateOwner(principal);
        return "Hello, " + owner.getName();
    }
}
