package be.kdg.keepdishesgoing.order.adapter.out;

import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderEventRepository;
import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.domain.events.*;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderEventStoreAdapter implements OrderEventStorePort {

    private final OrderEventRepository eventRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OrderEventStoreAdapter(OrderEventRepository eventRepository,
                                  ObjectMapper objectMapper,
                                  ApplicationEventPublisher eventPublisher) {
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void save(Order order) {
        List<DomainEvent> newEvents = order.getDomainEvents();

        int currentSequence = eventRepository.findMaxSequenceByAggregateId(order.getId())
                .orElse(-1);

        for (DomainEvent event : newEvents) {
            currentSequence++;

            String eventType = event.getClass().getSimpleName();
            String eventData = serializeEvent(event);

            OrderEventJpa entity = new OrderEventJpa(
                    order.getId(),
                    order.getRestaurantId(),
                    eventType,
                    eventData,
                    currentSequence,
                    event.eventPit()
            );

            eventRepository.save(entity);

            eventPublisher.publishEvent(event);
        }

        order.clearDomainEvents();
    }

    @Override
    public Optional<List<DomainEvent>> loadEvents(UUID orderId) {
        List<OrderEventJpa> entities = eventRepository
                .findByOrderIdOrderBySequenceNumberAsc(orderId);

        if (entities.isEmpty()) {
            return Optional.empty();
        }

        List<DomainEvent> events = entities.stream()
                .map(this::deserializeEvent)
                .collect(Collectors.toList());

        return Optional.of(events);
    }

    private String serializeEvent(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    private DomainEvent deserializeEvent(OrderEventJpa entity) {
        try {
            Class<?> eventClass = Class.forName(
                    "be.kdg.keepdishesgoing.order.domain.events." + entity.getEventType()
            );
            return (DomainEvent) objectMapper.readValue(entity.getEventData(), eventClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}
