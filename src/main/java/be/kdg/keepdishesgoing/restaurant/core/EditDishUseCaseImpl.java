package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.domain.DishStatus;
import be.kdg.keepdishesgoing.restaurant.port.in.DishDraftCommand;
import be.kdg.keepdishesgoing.restaurant.port.in.EditDishUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import be.kdg.keepdishesgoing.restaurant.port.out.UpdateDishPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditDishUseCaseImpl implements EditDishUseCase {

    private final UpdateDishPort updatePort;
    private final LoadDishPort loadPort;

    public EditDishUseCaseImpl(UpdateDishPort updatePort, LoadDishPort loadPort) {
        this.updatePort = updatePort;
        this.loadPort = loadPort;
    }

    @Override
    public Dish createOrUpdateDraft(DishDraftCommand command) {
        Dish dish;

        if (command.dishId() != null) {
            dish = loadPort.loadById(command.restaurantId(), command.dishId())
                    .orElseThrow(() -> new IllegalArgumentException("Draft not found"));

            if (dish.status() != DishStatus.DRAFT) {
                throw new IllegalStateException("Can only edit drafts. Use liveDishId to create draft from published.");
            }

            dish.updateDraft(
                    command.name(),
                    command.dishType(),
                    command.foodTags(),
                    command.description(),
                    command.price(),
                    command.imageUrl()
            );

        } else if (command.liveDishId() != null) {
            Dish published = loadPort.loadById(command.restaurantId(), command.liveDishId())
                    .orElseThrow(() -> new IllegalArgumentException("Published dish not found"));

            dish = Dish.createDraftFromPublished(
                    published,
                    command.name(),
                    command.dishType(),
                    command.foodTags(),
                    command.description(),
                    command.price(),
                    command.imageUrl()
            );

        } else {
            dish = Dish.createNewDraft(
                    command.restaurantId(),
                    command.name(),
                    command.dishType(),
                    command.foodTags(),
                    command.description(),
                    command.price(),
                    command.imageUrl()
            );
        }

        return updatePort.save(dish);
    }
}