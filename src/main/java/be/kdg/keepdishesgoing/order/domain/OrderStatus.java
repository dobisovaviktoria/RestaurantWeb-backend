package be.kdg.keepdishesgoing.order.domain;

public enum OrderStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    AUTO_DECLINED,
    READY_FOR_PICKUP,
    PICKED_UP,
    DELIVERED
}
