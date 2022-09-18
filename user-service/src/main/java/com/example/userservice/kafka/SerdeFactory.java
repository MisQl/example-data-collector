package com.example.userservice.kafka;

import org.springframework.kafka.support.serializer.JsonSerde;

public class SerdeFactory {

    public static <T> JsonSerde<T> Json(Class<T> c) {
        var valueSerde = new JsonSerde<T>();
        valueSerde.deserializer().addTrustedPackages("*");
        return valueSerde;
    }
}
