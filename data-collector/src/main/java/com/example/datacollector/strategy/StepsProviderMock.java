package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.example.datacollector.core.DataField.*;

@Component
class StepsProviderMock implements StepsProvider {

    @Override
    public List<DataField> calculateSteps(Set<DataField> source, DataField destination) {
        if (List.of(USER_FIRSTNAME, USER_LASTNAME).contains(destination) && source.contains(USER_ID)) {
            return List.of(USER_ID, destination);
        }
        if (List.of(ADDRESS_STREET, ADDRESS_CITY).contains(destination) && source.contains(ADDRESS_ID)) {
            return List.of(ADDRESS_ID, destination);
        }
        if (List.of(USER_PARENT_ID).contains(destination) && source.contains(USER_ID)) {
            return List.of(USER_ID, USER_PARENT_ID);
        }
        if (List.of(USER_PARENT_FIRSTNAME, USER_PARENT_LASTNAME).contains(destination) && source.contains(USER_ID)) {
            return List.of(USER_ID, USER_PARENT_ID, destination);
        }
        throw new IllegalArgumentException("The source data is insufficient to obtain: " + destination);
    }
}

/*
    USER_ID -> USER_FIRSTNAME
    USER_ID -> USER_LASTNAME
    USER_ID -> USER_PARENT_ID

    ADDRESS_ID -> ADDRESS_STREET
    ADDRESS_ID -> ADDRESS_CITY

    USER_PARENT_ID -> USER_PARENT_FIRSTNAME
    USER_PARENT_ID -> USER_PARENT_LASTNAME
 */