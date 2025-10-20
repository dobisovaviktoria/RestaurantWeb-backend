package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.port.in.ApplyDishChangesUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishDraftPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplyDishChangesUseCaseImpl implements ApplyDishChangesUseCase {

    private final DishDraftPort dishDraftPort;

    public ApplyDishChangesUseCaseImpl(DishDraftPort dishDraftPort) {
        this.dishDraftPort = dishDraftPort;
    }

    @Override
    public void applyAllPendingChanges(UUID restaurantId) {
        dishDraftPort.applyDraftChanges(restaurantId);
    }
}
