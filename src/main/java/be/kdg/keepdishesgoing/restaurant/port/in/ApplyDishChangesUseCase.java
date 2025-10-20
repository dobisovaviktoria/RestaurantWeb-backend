package be.kdg.keepdishesgoing.restaurant.port.in;

import java.util.UUID;

public interface ApplyDishChangesUseCase {
    void applyAllPendingChanges(UUID restaurantId);
}
