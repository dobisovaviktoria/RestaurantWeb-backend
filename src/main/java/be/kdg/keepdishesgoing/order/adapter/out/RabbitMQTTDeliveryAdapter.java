package be.kdg.keepdishesgoing.order.adapter.out;

import be.kdg.keepdishesgoing.order.port.out.DeliveryMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQTTDeliveryAdapter implements DeliveryMessagePort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQTTDeliveryAdapter.class);

    private static final String EXCHANGE_NAME = "kdg.events";

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQTTDeliveryAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderAccepted(UUID orderId, UUID restaurantId) {
        String routingKey = String.format("restaurant.%s.order.accepted.v1", restaurantId);

        OrderAcceptedMessage message = new OrderAcceptedMessage(
                orderId.toString(),
                restaurantId.toString()
        );

        sendMessage(routingKey, message, "OrderAccepted");
    }

    @Override
    public void publishOrderReady(UUID orderId, UUID restaurantId) {
        String routingKey = String.format("restaurant.%s.order.ready.v1", restaurantId);

        OrderReadyMessage message = new OrderReadyMessage(
                orderId.toString(),
                restaurantId.toString()
        );

        sendMessage(routingKey, message, "OrderReady");
    }

    private void sendMessage(String routingKey, Object message, String messageType) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, message);
            log.info("Published {} message: routingKey={}, message={}", messageType, routingKey, message);
        } catch (Exception e) {
            log.error("Failed to publish {} message (routingKey={}): {}", messageType, routingKey, e.getMessage(), e);
            throw new RuntimeException("Failed to publish " + messageType + " message", e);
        }
    }

    record OrderAcceptedMessage(String orderId, String restaurantId) {}
    record OrderReadyMessage(String orderId, String restaurantId) {}
}
