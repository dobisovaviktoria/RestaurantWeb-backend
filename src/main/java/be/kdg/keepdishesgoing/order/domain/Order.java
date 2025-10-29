package be.kdg.keepdishesgoing.order.domain;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.common.domain.Money;
import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.order.domain.events.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID restaurantId;
    private String customerName;
    private String customerEmail;
    private Address deliveryAddress;
    private List<OrderLine> orderLines;
    private Money totalAmount;
    private OrderStatus status;
    private LocalDateTime placedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime readyAt;
    private String rejectionReason;

    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private int version = 0;

    private Order() {
        this.orderLines = new ArrayList<>();
    }

    public static Order placeOrder(
            UUID restaurantId,
            String customerName,
            String customerEmail,
            Address deliveryAddress,
            List<OrderLine> orderLines
    ) {
        validateOrderPlacement(customerName, customerEmail, orderLines);

        Order order = new Order();
        order.id = UUID.randomUUID();
        order.restaurantId = restaurantId;
        order.customerName = customerName;
        order.customerEmail = customerEmail;
        order.deliveryAddress = deliveryAddress;
        order.orderLines = new ArrayList<>(orderLines);
        order.totalAmount = calculateTotal(orderLines);
        order.status = OrderStatus.PENDING;
        order.placedAt = LocalDateTime.now();

        order.domainEvents.add(new OrderPlacedEvent(
                order.id,
                order.restaurantId,
                order.customerName,
                order.customerEmail,
                order.deliveryAddress,
                order.orderLines,
                order.totalAmount,
                order.placedAt
        ));

        return order;
    }

    public static Order fromEvents(List<DomainEvent> events) {
        Order order = new Order();
        events.forEach(order::apply);
        return order;
    }

    private void apply(DomainEvent event) {
        if (event instanceof OrderPlacedEvent e) {
            this.id = e.orderId();
            this.restaurantId = e.restaurantId();
            this.customerName = e.customerName();
            this.customerEmail = e.customerEmail();
            this.deliveryAddress = e.deliveryAddress();
            this.orderLines = new ArrayList<>(e.orderLines());
            this.totalAmount = e.totalAmount();
            this.status = OrderStatus.PENDING;
            this.placedAt = e.placedAt();
        }
        else if (event instanceof OrderAcceptedEvent e) {
            this.status = OrderStatus.ACCEPTED;
            this.acceptedAt = e.acceptedAt();
        }
        else if (event instanceof OrderRejectedEvent e) {
            this.status = OrderStatus.REJECTED;
            this.rejectionReason = e.reason();
        }
        else if (event instanceof OrderReadyForPickupEvent) {
            this.status = OrderStatus.READY_FOR_PICKUP;
            this.readyAt = LocalDateTime.now();
        }
        else if (event instanceof OrderPickedUpEvent) {
            this.status = OrderStatus.PICKED_UP;
        }
        else if (event instanceof OrderDeliveredEvent) {
            this.status = OrderStatus.DELIVERED;
        }
        else if (event instanceof OrderAutoDeclinedEvent) {
            this.status = OrderStatus.AUTO_DECLINED;
        }

        version++;
    }

    public void accept() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only accept pending orders");
        }

        this.status = OrderStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();

        domainEvents.add(new OrderAcceptedEvent(
                this.id,
                this.restaurantId,
                this.acceptedAt
        ));
    }

    public void reject(String reason) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending orders");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }

        this.status = OrderStatus.REJECTED;
        this.rejectionReason = reason;

        domainEvents.add(new OrderRejectedEvent(
                this.id,
                this.restaurantId,
                reason,
                LocalDateTime.now()
        ));
    }

    public void markReadyForPickup() {
        if (status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Can only mark accepted orders as ready");
        }

        this.status = OrderStatus.READY_FOR_PICKUP;
        this.readyAt = LocalDateTime.now();

        domainEvents.add(new OrderReadyForPickupEvent(
                this.id,
                this.restaurantId,
                this.readyAt
        ));
    }

    public void markPickedUp() {
        if (status != OrderStatus.READY_FOR_PICKUP) {
            throw new IllegalStateException("Order must be ready before pickup");
        }

        this.status = OrderStatus.PICKED_UP;

        domainEvents.add(new OrderPickedUpEvent(
                this.id,
                this.restaurantId,
                LocalDateTime.now()
        ));
    }

    public void markDelivered() {
        if (status != OrderStatus.PICKED_UP) {
            throw new IllegalStateException("Order must be picked up before delivery");
        }

        this.status = OrderStatus.DELIVERED;

        domainEvents.add(new OrderDeliveredEvent(
                this.id,
                this.restaurantId,
                LocalDateTime.now()
        ));
    }

    public void autoDecline() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be auto-declined");
        }

        this.status = OrderStatus.AUTO_DECLINED;

        domainEvents.add(new OrderAutoDeclinedEvent(
                this.id,
                this.restaurantId,
                LocalDateTime.now()
        ));
    }

    public boolean isTimedOut() {
        if (status != OrderStatus.PENDING) {
            return false;
        }
        return placedAt.plusMinutes(5).isBefore(LocalDateTime.now());
    }

    private static Money calculateTotal(List<OrderLine> orderLines) {
        return orderLines.stream()
                .map(line -> line.price().multiply(BigDecimal.valueOf(line.quantity())))
                .reduce(new Money(BigDecimal.ZERO), Money::add);
    }

    private static void validateOrderPlacement(
            String customerName,
            String customerEmail,
            List<OrderLine> orderLines
    ) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customerEmail == null || !customerEmail.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    public UUID getId() { return id; }
    public UUID getRestaurantId() { return restaurantId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public Money getTotalAmount() { return totalAmount; }
    public List<DomainEvent> getDomainEvents() { return List.copyOf(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }
    public int getVersion() { return version; }
}