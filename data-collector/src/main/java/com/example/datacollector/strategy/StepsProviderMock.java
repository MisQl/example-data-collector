package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.datacollector.core.DataField.*;

@Component
class StepsProviderMock implements StepsProvider {

    @Override
    public List<Step> calculateSteps(Set<DataField> source, Set<DataField> destination) {
        var steps = new ArrayList<Step>();
        if (source.contains(USER_ID)) {
            var stepDestination = new HashSet<DataField>();
            if (destination.contains(USER_FIRSTNAME)) {
                stepDestination.add(USER_FIRSTNAME);
            }
            if (destination.contains(USER_LASTNAME)) {
                stepDestination.add(USER_LASTNAME);
            }
            if (destination.contains(USER_PARENT_ID)) {
                stepDestination.add(USER_PARENT_ID);
            }
            if (destination.contains(USER_PARENT_FIRSTNAME) || destination.contains(USER_PARENT_LASTNAME)) {
                stepDestination.add(USER_PARENT_ID);
                var additionalStepDestination = new HashSet<DataField>();
                if (destination.contains(USER_PARENT_FIRSTNAME)) {
                    additionalStepDestination.add(USER_PARENT_FIRSTNAME);
                }
                if (destination.contains(USER_PARENT_LASTNAME)) {
                    additionalStepDestination.add(USER_PARENT_LASTNAME);
                }
                steps.add(new Step(USER_PARENT_ID, additionalStepDestination));
            }
            steps.add(new Step(USER_ID, stepDestination));
        }
        if (source.contains(ADDRESS_ID)) {
            var stepDestination = destination.stream()
                    .filter(d -> Set.of(ADDRESS_STREET, ADDRESS_CITY).contains(d))
                    .collect(Collectors.toSet());
            steps.add(new Step(ADDRESS_ID, stepDestination));
        }
        return steps;
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