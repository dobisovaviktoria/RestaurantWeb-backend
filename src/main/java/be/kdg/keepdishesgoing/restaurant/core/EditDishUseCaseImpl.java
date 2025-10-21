package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.port.in.EditDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.in.DishDraftCommand;
import be.kdg.keepdishesgoing.restaurant.port.out.DishWritePort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EditDishUseCaseImpl implements EditDishUseCase {
    private final DishWritePort writePort;
    private final LoadDishPort loadPort;

    public EditDishUseCaseImpl(DishWritePort writePort, LoadDishPort loadPort) {
        this.writePort = writePort;
        this.loadPort = loadPort;
    }

    @Override
    public Dish createOrUpdateDraft(DishDraftCommand command) {
        Dish dish;
        if (command.dishId() != null) {
            Dish existing = loadPort.loadById(command.restaurantId(), command.dishId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

            if (existing.status() == DishStatus.PUBLISHED) {
                dish = Dish.createDraftFromPublished(existing, command.name(), command.dishType(),
                        command.foodTags(), command.description(), command.price(), command.imageUrl());
            } else {
                dish = Dish.fromPersistence(existing.id(), existing.restaurantId(), command.name(), command.dishType(),
                        command.foodTags(), command.description(), command.price(), command.imageUrl(),
                        existing.status(), existing.stockStatus(), existing.liveDishId());
            }
        } else {
            dish = Dish.createNewDraft(command.restaurantId(), command.name(), command.dishType(),
                    command.foodTags(), command.description(), command.price(), command.imageUrl());
        }
        return writePort.saveDraft(dish);
    }
}
