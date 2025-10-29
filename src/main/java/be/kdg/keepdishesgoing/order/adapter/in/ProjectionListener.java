package be.kdg.keepdishesgoing.order.adapter.in;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;
import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderProjectionRepository;
import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import be.kdg.keepdishesgoing.order.domain.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProjectionListener {

    private static final Logger log = LoggerFactory.getLogger(ProjectionListener.class);

    private final OrderProjectionRepository projectionRepo;

    public ProjectionListener(OrderProjectionRepository projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    @EventListener
    @Transactional
    public void on(OrderPlacedEvent event) {
        log.info("Projecting OrderPlaced: {}", event.orderId());

        OrderProjection projection = new OrderProjection();
        projection.setOrderId(event.orderId());
        projection.setRestaurantId(event.restaurantId());
        projection.setCustomerName(event.customerName());
        projection.setCustomerEmail(event.customerEmail());
        projection.setDeliveryStreet(event.deliveryAddress().street());
        projection.setDeliveryCity(event.deliveryAddress().city());
        projection.setTotalAmount(event.totalAmount().amount());
        projection.setStatus(OrderStatus.PENDING);
        projection.setPlacedAt(event.placedAt());

        projectionRepo.save(projection);
    }

    @EventListener
    @Transactional
    public void on(OrderAcceptedEvent event) {
        log.info("Projecting OrderAccepted: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.ACCEPTED);
            projection.setAcceptedAt(event.acceptedAt());
            projectionRepo.save(projection);
        });
    }

    @EventListener
    @Transactional
    public void on(OrderRejectedEvent event) {
        log.info("Projecting OrderRejected: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.REJECTED);
            projection.setRejectionReason(event.reason());
            projectionRepo.save(projection);
        });
    }

    @EventListener
    @Transactional
    public void on(OrderReadyForPickupEvent event) {
        log.info("Projecting OrderReady: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.READY_FOR_PICKUP);
            projection.setReadyAt(event.readyAt());
            projectionRepo.save(projection);
        });
    }

    @EventListener
    @Transactional
    public void on(OrderPickedUpEvent event) {
        log.info("Projecting OrderPickedUp: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.PICKED_UP);
            projection.setPickedUpAt(event.pickedUpAt());
            projectionRepo.save(projection);
        });
    }

    @EventListener
    @Transactional
    public void on(OrderDeliveredEvent event) {
        log.info("Projecting OrderDelivered: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.DELIVERED);
            projection.setDeliveredAt(event.deliveredAt());
            projectionRepo.save(projection);
        });
    }

    @EventListener
    @Transactional
    public void on(OrderAutoDeclinedEvent event) {
        log.info("Projecting OrderAutoDeclined: {}", event.orderId());

        projectionRepo.findById(event.orderId()).ifPresent(projection -> {
            projection.setStatus(OrderStatus.AUTO_DECLINED);
            projectionRepo.save(projection);
        });
    }
}
