package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;
import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import be.kdg.keepdishesgoing.order.port.out.OrderQueryPort;
import be.kdg.keepdishesgoing.order.port.out.OrderSnapshotPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutoDeclineOrderScheduler {
    private static final Logger log = LoggerFactory.getLogger(AutoDeclineOrderScheduler.class);

    private final OrderQueryPort orderQueryPort;
    private final OrderEventStorePort eventStore;
    private final OrderSnapshotPort snapshotPort;

    public AutoDeclineOrderScheduler(
            OrderQueryPort orderQueryPort,
            OrderEventStorePort eventStore,
            OrderSnapshotPort snapshotPort) {
        this.orderQueryPort = orderQueryPort;
        this.eventStore = eventStore;
        this.snapshotPort = snapshotPort;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkPendingOrders() {
        log.debug("Checking for orders to auto-decline...");
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<OrderProjection> pendingOrders = orderQueryPort
                .findPendingOrdersOlderThan(fiveMinutesAgo);

        if (pendingOrders.isEmpty()) {
            return;
        }

        log.info("Found {} orders to auto-decline", pendingOrders.size());

        for (OrderProjection projection : pendingOrders) {
            try {
                Order order = snapshotPort.loadSnapshot(projection.getOrderId())
                        .map(snapshot -> {
                            return eventStore.loadEvents(projection.getOrderId())
                                    .map(events -> {
                                        events.forEach(snapshot::applyEvent);
                                        return snapshot;
                                    })
                                    .orElse(snapshot);
                        })
                        .or(() -> eventStore.loadEvents(projection.getOrderId())
                                .map(Order::fromEvents))
                        .orElse(null);

                if (order != null && order.getStatus() == OrderStatus.PENDING) {
                    log.info("Auto-declining order: {}", order.getId());
                    order.autoDecline();
                    eventStore.save(order);
                }
            } catch (Exception e) {
                log.error("Failed to auto-decline order: {}", projection.getOrderId(), e);
            }
        }
    }
}
