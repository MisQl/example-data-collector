package com.example.userservice.providers;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataProvider;
import com.example.event.CollectEventRequestV1;
import com.example.event.CollectEventResponseV1;
import com.example.userservice.kafka.SerdeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.userservice.kafka.KafkaTopics.DATA_COLLECT_REQUEST_TOPIC;
import static com.example.userservice.kafka.KafkaTopics.DATA_COLLECT_RESULT_TOPIC;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserProviderEventStream {

    private final List<DataProvider> dataProviders;

    @Autowired
    void addressStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((key, event) -> dataProviders.stream().anyMatch(dataProvider -> dataProvider.supports(event)))
                .peek((key, event) -> log.info("---> PROCESSING: " + event))
                .mapValues((k, v) -> collectData(v))
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }

    private CollectEventResponseV1 collectData(CollectEventRequestV1 event) {
        var dataProvider = dataProviders.stream()
                .filter(dp -> dp.supports(event))
                .findFirst().orElseThrow(() -> new RuntimeException("No supported data provider"));

        var inputData = new Data(event.source(), event.sourceValue());
        var outputModel = event.destination();
        var outputData = dataProvider.get(inputData, outputModel).stream()
                .map(data -> new CollectEventResponseV1.DestinationData(data.getDataField(), data.getValue()))
                .collect(Collectors.toSet());

        return event.response(outputData);
    }
}
