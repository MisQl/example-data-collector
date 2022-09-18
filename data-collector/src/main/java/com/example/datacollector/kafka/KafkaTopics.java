package com.example.datacollector.kafka;

import java.util.Arrays;
import java.util.List;

public class KafkaTopics {

    public static final String DATA_COLLECT_REQUEST_TOPIC = "data-collect.request.v1";
    public static final String DATA_COLLECT_RESULT_TOPIC = "data-collect.result.v1";

    public static List<String> getTopics() {
        return Arrays.asList(
                DATA_COLLECT_REQUEST_TOPIC,
                DATA_COLLECT_RESULT_TOPIC
        );
    }
}
