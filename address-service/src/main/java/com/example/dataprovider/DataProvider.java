package com.example.dataprovider;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.SimpleStep;

import java.util.Optional;

public interface DataProvider {

    SimpleStep supports();

    Optional<Data> get(Data source);
}
