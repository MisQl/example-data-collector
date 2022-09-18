package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;

import java.util.List;
import java.util.Set;

public interface StepsProvider {

    List<DataField> calculateSteps(Set<DataField> source, DataField destination);
}
