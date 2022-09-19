package com.example.datacollector.order.event;

import com.example.datacollector.core.Data;
import com.example.datacollector.order.OrderCollector;
import com.example.event.CollectEventResponseV1;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.example.datacollector.kafka.KafkaTopics.DATA_COLLECT_RESULT_TOPIC;

@Component
@RequiredArgsConstructor
public class EventListener {

    private final OrderCollector orderCollector;

    @KafkaListener(topics = DATA_COLLECT_RESULT_TOPIC)
    public void handle(ConsumerRecord<String, CollectEventResponseV1> record) {
        System.out.println("---> RESULT: " + record.value());
        var event = record.value();
        var orderId = event.orderId();
        var destinationData = event.destinationData().stream()
                .map(d -> new Data(d.destination(), d.destinationValue()))
                .collect(Collectors.toSet());
        orderCollector.updateOrder(orderId, destinationData);
    }
}
