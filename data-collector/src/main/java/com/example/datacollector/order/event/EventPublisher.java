package com.example.datacollector.order.event;

import com.example.datacollector.core.DataField;
import com.example.event.CollectEventRequestV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.datacollector.kafka.KafkaTopics.DATA_COLLECT_REQUEST_TOPIC;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String orderId, DataField source, DataField destination, Object sourceValue) {
        var event = new CollectEventRequestV1(orderId, source, destination, sourceValue);
        kafkaTemplate.send(DATA_COLLECT_REQUEST_TOPIC, orderId, event);
    }
}
