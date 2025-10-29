package be.kdg.keepdishesgoing.order.adapter.out;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_events", schema = "kdg_orders")
public class OrderEventJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String eventData;

    @Column(nullable = false)
    private int sequenceNumber;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public OrderEventJpa() {}

    public OrderEventJpa(UUID aggregateId, UUID restaurantId, String eventType,
                            String eventData, int sequenceNumber, LocalDateTime timestamp) {
        this.orderId = aggregateId;
        this.restaurantId = restaurantId;
        this.eventType = eventType;
        this.eventData = eventData;
        this.sequenceNumber = sequenceNumber;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public UUID getRestaurantId() { return restaurantId; }
    public String getEventType() { return eventType; }
    public String getEventData() { return eventData; }
    public int getSequenceNumber() { return sequenceNumber; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
