package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.MarkOrderPickedUpUseCase;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MarkOrderPickedUpUseCaseImpl implements MarkOrderPickedUpUseCase {

    private final OrderEventStorePort eventStore;

    public MarkOrderPickedUpUseCaseImpl(OrderEventStorePort eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void markPickedUp(UUID orderId) {
        Order order = eventStore.loadEvents(orderId)
                .map(Order::fromEvents)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.markPickedUp();
        eventStore.save(order);
    }
}
