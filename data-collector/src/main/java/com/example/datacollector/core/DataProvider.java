package com.example.datacollector.core;

import java.util.Set;

public interface DataProvider {

    Set<SupportedMapping> supports();

    Set<Data> get(Data source, Set<DataField> outputModel);
}
