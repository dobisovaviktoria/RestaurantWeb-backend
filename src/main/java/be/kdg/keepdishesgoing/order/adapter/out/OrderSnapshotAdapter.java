package be.kdg.keepdishesgoing.order.adapter.out;

import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderSnapshotRepository;
import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.out.OrderSnapshotPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderSnapshotAdapter implements OrderSnapshotPort {
    private final OrderSnapshotRepository snapshotRepo;
    private final ObjectMapper objectMapper;

    public OrderSnapshotAdapter(OrderSnapshotRepository snapshotRepo, ObjectMapper objectMapper) {
        this.snapshotRepo = snapshotRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void saveSnapshot(Order order) {
        try {
            String snapshotData = objectMapper.writeValueAsString(order);
            OrderSnapshotJpa snapshot = new OrderSnapshotJpa(
                    order.getId(),
                    snapshotData,
                    order.getVersion(),
                    LocalDateTime.now()
            );
            snapshotRepo.save(snapshot);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save snapshot", e);
        }
    }

    @Override
    public Optional<Order> loadSnapshot(UUID orderId) {
        return snapshotRepo.findById(orderId)
                .map(jpa -> {
                    try {
                        return objectMapper.readValue(jpa.getSnapshotData(), Order.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load snapshot", e);
                    }
                });
    }
}
