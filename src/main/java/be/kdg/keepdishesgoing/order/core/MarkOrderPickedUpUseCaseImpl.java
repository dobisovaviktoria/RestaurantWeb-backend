package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.MarkOrderPickedUpUseCase;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import be.kdg.keepdishesgoing.order.port.out.OrderSnapshotPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MarkOrderPickedUpUseCaseImpl implements MarkOrderPickedUpUseCase {
    private final OrderEventStorePort eventStore;
    private final OrderSnapshotPort snapshotPort;

    public MarkOrderPickedUpUseCaseImpl(
            OrderEventStorePort eventStore,
            OrderSnapshotPort snapshotPort) {
        this.eventStore = eventStore;
        this.snapshotPort = snapshotPort;
    }

    @Override
    public void markPickedUp(UUID orderId) {
        Order order = snapshotPort.loadSnapshot(orderId)
                .map(snapshot -> {
                    return eventStore.loadEvents(orderId)
                            .map(events -> {
                                events.forEach(snapshot::applyEvent);
                                return snapshot;
                            })
                            .orElse(snapshot);
                })
                .or(() -> eventStore.loadEvents(orderId)
                        .map(Order::fromEvents))
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.markPickedUp();
        eventStore.save(order);
    }
}
