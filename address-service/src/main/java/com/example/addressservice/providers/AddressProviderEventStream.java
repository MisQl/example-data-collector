package com.example.addressservice.providers;

import com.example.addressservice.kafka.SerdeFactory;
import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.event.CollectEventRequestV1;
import com.example.event.CollectEventResponseV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static com.example.addressservice.kafka.KafkaTopics.DATA_COLLECT_REQUEST_TOPIC;
import static com.example.addressservice.kafka.KafkaTopics.DATA_COLLECT_RESULT_TOPIC;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AddressProviderEventStream {

    private final AddressCityProvider addressCityProvider;
    private final AddressStreetProvider addressStreetProvider;

    @Autowired
    void addressCityStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((k, v) -> DataField.ADDRESS_ID.equals(v.source()))
                .filter((k, v) -> DataField.ADDRESS_CITY.equals(v.destination()))
                .peek((k, v) -> log.info("---> PROCESSING: " + v))
                .mapValues((k, v) -> {
                    var data = new Data(v.source(), v.sourceValue());
                    return addressCityProvider.get(data).map(d -> new CollectEventResponseV1(v.orderId(), v.source(), v.destination(), v.sourceValue(), d.getValue()))
                            .orElseThrow(() -> new RuntimeException("Missing " + v.destination() + " for " + data.getDataField() + ": " + data.getValue()));
                })
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }

    @Autowired
    void addressStreetStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((k, v) -> DataField.ADDRESS_ID.equals(v.source()))
                .filter((k, v) -> DataField.ADDRESS_STREET.equals(v.destination()))
                .peek((k, v) -> log.info("---> PROCESSING: " + v))
                .mapValues((k, v) -> {
                    var data = new Data(v.source(), v.sourceValue());
                    return addressStreetProvider.get(data).map(d -> new CollectEventResponseV1(v.orderId(), v.source(), v.destination(), v.sourceValue(), d.getValue()))
                            .orElseThrow(() -> new RuntimeException("Missing " + v.destination() + " for " + data.getDataField() + ": " + data.getValue()));
                })
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }
}
