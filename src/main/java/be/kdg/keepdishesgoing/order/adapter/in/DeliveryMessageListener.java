package be.kdg.keepdishesgoing.order.adapter.in;

import be.kdg.keepdishesgoing.order.port.in.MarkOrderPickedUpUseCase;
import be.kdg.keepdishesgoing.order.port.in.MarkOrderDeliveredUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryMessageListener {

    private static final Logger log = LoggerFactory.getLogger(DeliveryMessageListener.class);

    private final MarkOrderPickedUpUseCase markPickedUpUseCase;
    private final MarkOrderDeliveredUseCase markDeliveredUseCase;
    private final ObjectMapper objectMapper;

    public DeliveryMessageListener(
            MarkOrderPickedUpUseCase markPickedUpUseCase,
            MarkOrderDeliveredUseCase markDeliveredUseCase,
            ObjectMapper objectMapper) {
        this.markPickedUpUseCase = markPickedUpUseCase;
        this.markDeliveredUseCase = markDeliveredUseCase;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "kdg.delivery.pickedup")
    public void handleOrderPickedUp(String message) {
        try {
            log.info("Received OrderPickedUp: {}", message);

            OrderPickedUpMessage msg = objectMapper.readValue(message, OrderPickedUpMessage.class);
            UUID orderId = UUID.fromString(msg.orderId());

            markPickedUpUseCase.markPickedUp(orderId);

            log.info("Processed OrderPickedUp for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to process OrderPickedUp message", e);
        }
    }

    @RabbitListener(queues = "kdg.delivery.delivered")
    public void handleOrderDelivered(String message) {
        try {
            log.info("Received OrderDelivered: {}", message);

            OrderDeliveredMessage msg = objectMapper.readValue(message, OrderDeliveredMessage.class);
            UUID orderId = UUID.fromString(msg.orderId());

            markDeliveredUseCase.markDelivered(orderId);

            log.info("Processed OrderDelivered for order: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to process OrderDelivered message", e);
        }
    }

    @RabbitListener(queues = "kdg.delivery.location")
    public void handleCourierLocation(String message) {
        try {
            log.info("Received CourierLocation: {}", message);

            CourierLocationMessage msg = objectMapper.readValue(message, CourierLocationMessage.class);

            log.info("Courier location updated: orderId={}, lat={}, lon={}",
                    msg.orderId(), msg.latitude(), msg.longitude());

        } catch (Exception e) {
            log.error("Failed to process CourierLocation message", e);
        }
    }

    record OrderPickedUpMessage(String orderId, String restaurantId) {}
    record OrderDeliveredMessage(String orderId, String restaurantId) {}
    record CourierLocationMessage(String orderId, double latitude, double longitude) {}
}
