package com.example.userservice.providers;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
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

import static com.example.userservice.kafka.KafkaTopics.DATA_COLLECT_REQUEST_TOPIC;
import static com.example.userservice.kafka.KafkaTopics.DATA_COLLECT_RESULT_TOPIC;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserProviderEventStream {

    private final UserFirstnameProvider userFirstnameProvider;
    private final UserLastnameProvider userLastnameProvider;
    private final UserParentIdProvider userParentIdProvider;

    @Autowired
    void userFirstnameStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((k, v) -> DataField.USER_ID.equals(v.source()))
                .filter((k, v) -> DataField.USER_FIRSTNAME.equals(v.destination()))
                .peek((k, v) -> log.info("---> PROCESSING: " + v))
                .mapValues((k, v) -> {
                    var data = new Data(v.source(), v.sourceValue());
                    return userFirstnameProvider.get(data).map(d -> new CollectEventResponseV1(v.orderId(), v.source(), v.destination(), v.sourceValue(), d.getValue()))
                            .orElseThrow(() -> new RuntimeException("Missing " + v.destination() + " for " + data.getDataField() + ": " + data.getValue()));
                })
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }

    @Autowired
    void userLastnameStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((k, v) -> DataField.USER_ID.equals(v.source()))
                .filter((k, v) -> DataField.USER_LASTNAME.equals(v.destination()))
                .peek((k, v) -> log.info("---> PROCESSING: " + v))
                .mapValues((k, v) -> {
                    var data = new Data(v.source(), v.sourceValue());
                    return userLastnameProvider.get(data).map(d -> new CollectEventResponseV1(v.orderId(), v.source(), v.destination(), v.sourceValue(), d.getValue()))
                            .orElseThrow(() -> new RuntimeException("Missing " + v.destination() + " for " + data.getDataField() + ": " + data.getValue()));
                })
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }

    @Autowired
    void setUserParentIdStream(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(DATA_COLLECT_REQUEST_TOPIC, Consumed.with(Serdes.String(), SerdeFactory.Json(CollectEventRequestV1.class)))
                .filter((k, v) -> DataField.USER_ID.equals(v.source()))
                .filter((k, v) -> DataField.USER_PARENT_ID.equals(v.destination()))
                .peek((k, v) -> log.info("---> PROCESSING: " + v))
                .mapValues((k, v) -> {
                    var data = new Data(v.source(), v.sourceValue());
                    return userParentIdProvider.get(data).map(d -> new CollectEventResponseV1(v.orderId(), v.source(), v.destination(), v.sourceValue(), d.getValue()))
                            .orElseThrow(() -> new RuntimeException("Missing " + v.destination() + " for " + data.getDataField() + ": " + data.getValue()));
                })
                .to(DATA_COLLECT_RESULT_TOPIC, Produced.with(Serdes.String(), SerdeFactory.Json(CollectEventResponseV1.class)));
    }
}
