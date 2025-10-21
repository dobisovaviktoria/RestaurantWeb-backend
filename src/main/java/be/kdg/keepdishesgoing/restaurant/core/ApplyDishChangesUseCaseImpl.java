package be.kdg.keepdishesgoing.restaurant.core;

import be.kdg.keepdishesgoing.restaurant.domain.Dish;
import be.kdg.keepdishesgoing.restaurant.port.in.ApplyDishChangesUseCase;
import be.kdg.keepdishesgoing.restaurant.port.out.DishWritePort;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadDishPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApplyDishChangesUseCaseImpl implements ApplyDishChangesUseCase {
    private final LoadDishPort loadPort;
    private final DishWritePort writePort;

    public ApplyDishChangesUseCaseImpl(LoadDishPort loadPort, DishWritePort writePort) {
        this.loadPort = loadPort;
        this.writePort = writePort;
    }

    @Override
    public void applyAllPendingChanges(UUID restaurantId) {
        List<Dish> drafts = loadPort.loadDrafts(restaurantId);

        for (Dish draft : drafts) {
            if (draft.liveDishId() != null) {
                Dish live = loadPort.loadById(restaurantId, draft.liveDishId())
                        .orElseThrow(() -> new IllegalStateException("Live dish not found"));
                draft.applyDraftTo(live);
                writePort.save(live);
                writePort.deleteDraft(draft.id());
            } else {
                draft.publish();
                writePort.save(draft);
            }
        }
    }
}
