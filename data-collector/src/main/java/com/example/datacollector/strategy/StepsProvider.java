package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;

import java.util.List;
import java.util.Set;

public interface StepsProvider {

    List<Step> calculateSteps(Set<DataField> source, Set<DataField> destination);
}
