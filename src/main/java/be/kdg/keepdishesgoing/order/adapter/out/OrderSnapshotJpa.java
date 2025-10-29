package be.kdg.keepdishesgoing.order.adapter.out;

import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_snapshots", schema = "kdg_orders")
public class OrderSnapshotJpa {

    @Id
    private UUID aggregateId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String snapshotData;

    @Column(nullable = false)
    private int lastEventSequence;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public OrderSnapshotJpa() {}

    public OrderSnapshotJpa(UUID aggregateId, String snapshotData,
                               int lastEventSequence, LocalDateTime createdAt) {
        this.aggregateId = aggregateId;
        this.snapshotData = snapshotData;
        this.lastEventSequence = lastEventSequence;
        this.createdAt = createdAt;
    }

    public UUID getAggregateId() { return aggregateId; }
    public String getSnapshotData() { return snapshotData; }
    public int getLastEventSequence() { return lastEventSequence; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
