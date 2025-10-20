package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;

public interface EditDishUseCase {
    Dish createOrUpdateDraft(DishDraftCommand command);
}
