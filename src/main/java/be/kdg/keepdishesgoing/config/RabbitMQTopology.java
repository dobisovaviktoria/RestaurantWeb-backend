package be.kdg.keepdishesgoing.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    @Bean
    public TopicExchange kdgExchange() {
        return new TopicExchange("kdg.events");
    }

    @Bean
    public Queue restaurantOrderReadyQueue() {
        return new Queue("restaurant.order.ready", true);
    }

    @Bean
    public Queue restaurantOrderAcceptedQueue() {
        return new Queue("restaurant.order.accepted", true);
    }

    @Bean
    public Queue deliveryPickedUpQueue() {
        return new Queue("kdg.delivery.pickedup", true);
    }

    @Bean
    public Queue deliveryLocationQueue() {
        return new Queue("kdg.delivery.location", true);
    }

    @Bean
    public Queue deliveryDeliveredQueue() {
        return new Queue("kdg.delivery.delivered", true);
    }

    @Bean
    public Binding bindRestaurantOrderReady(Queue restaurantOrderReadyQueue, TopicExchange kdgExchange) {
        return BindingBuilder.bind(restaurantOrderReadyQueue)
                .to(kdgExchange)
                .with("restaurant.*.order.ready.v1");
    }

    @Bean
    public Binding bindRestaurantOrderAccepted(Queue restaurantOrderAcceptedQueue, TopicExchange kdgExchange) {
        return BindingBuilder.bind(restaurantOrderAcceptedQueue)
                .to(kdgExchange)
                .with("restaurant.*.order.accepted.v1");
    }

    @Bean
    public Binding bindDeliveryPickedUp(Queue deliveryPickedUpQueue, TopicExchange kdgExchange) {
        return BindingBuilder.bind(deliveryPickedUpQueue)
                .to(kdgExchange)
                .with("delivery.*.order.pickedup.v1");
    }

    @Bean
    public Binding bindDeliveryLocation(Queue deliveryLocationQueue, TopicExchange kdgExchange) {
        return BindingBuilder.bind(deliveryLocationQueue)
                .to(kdgExchange)
                .with("delivery.*.order.location.v1");
    }

    @Bean
    public Binding bindDeliveryDelivered(Queue deliveryDeliveredQueue, TopicExchange kdgExchange) {
        return BindingBuilder.bind(deliveryDeliveredQueue)
                .to(kdgExchange)
                .with("delivery.*.order.delivered.v1");
    }
}