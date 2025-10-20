package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.port.in.EditDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.in.DishDraftCommand;
import be.kdg.keepdishesgoing.restaurant.port.out.CreateDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import org.springframework.stereotype.Service;

@Service
public class EditDishUseCaseImpl implements EditDishUseCase {

    private final CreateDishPort createDishPort;
    private final LoadDishPort loadDishPort;

    public EditDishUseCaseImpl(CreateDishPort createDishPort, LoadDishPort loadDishPort) {
        this.createDishPort = createDishPort;
        this.loadDishPort = loadDishPort;
    }

    @Override
    public Dish createOrUpdateDraft(DishDraftCommand command) {
        Dish dish;

        if (command.dishId() != null) {
            Dish existing = loadDishPort.loadById(command.restaurantId(), command.dishId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

            if (existing.status() == DishStatus.PUBLISHED) {
                dish = Dish.createDraftFromPublished(
                        existing.id(),
                        command.restaurantId(),
                        command.name(),
                        command.dishType(),
                        command.foodTags(),
                        command.description(),
                        command.price(),
                        command.imageUrl()
                );
            } else {
                dish = Dish.fromPersistence(
                        existing.id(),
                        command.restaurantId(),
                        command.name(),
                        command.dishType(),
                        command.foodTags(),
                        command.description(),
                        command.price(),
                        command.imageUrl(),
                        existing.status(),
                        existing.stockStatus(),
                        existing.liveDishId()
                );
            }
        } else {
            dish = Dish.create(
                    command.restaurantId(),
                    command.name(),
                    command.dishType(),
                    command.foodTags(),
                    command.description(),
                    command.price(),
                    command.imageUrl(),
                    command.liveDishId()
            );
        }

        return createDishPort.saveDraft(dish);
    }
}
