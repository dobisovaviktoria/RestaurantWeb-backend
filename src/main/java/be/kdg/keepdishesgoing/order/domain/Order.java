package be.kdg.keepdishesgoing.order.domain;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.common.domain.Money;
import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.order.domain.events.*;

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
        order.status = OrderStatus.PENDING;

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
        }
        else if (event instanceof OrderAcceptedEvent e) {
            this.status = OrderStatus.ACCEPTED;
        }
        else if (event instanceof OrderRejectedEvent e) {
            this.status = OrderStatus.REJECTED;
        }
        else if (event instanceof OrderReadyForPickupEvent) {
            this.status = OrderStatus.READY_FOR_PICKUP;
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
    }

    public void accept() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only accept pending orders");
        }

        this.status = OrderStatus.ACCEPTED;
    }

    public void reject(String reason) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending orders");
        }
        this.status = OrderStatus.REJECTED;
    }

    public void markReadyForPickup() {
        if (status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Can only mark accepted orders as ready");
        }
        this.status = OrderStatus.READY_FOR_PICKUP;
    }

    public void markPickedUp() {
        if (status != OrderStatus.READY_FOR_PICKUP) {
            throw new IllegalStateException("Order must be ready before pickup");
        }
        this.status = OrderStatus.PICKED_UP;
    }

    public void markDelivered() {
        if (status != OrderStatus.PICKED_UP) {
            throw new IllegalStateException("Order must be picked up before delivery");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void autoDecline() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be auto-declined");
        }
        this.status = OrderStatus.AUTO_DECLINED;
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
    public Money getTotalAmount() { return totalAmount; }
}
