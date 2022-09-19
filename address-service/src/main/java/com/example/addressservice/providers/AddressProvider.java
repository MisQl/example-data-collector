package com.example.addressservice.providers;

import com.example.addressservice.address.AddressRepository;
import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.core.DataProvider;
import com.example.datacollector.core.SupportedMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.datacollector.core.DataField.*;

@Component
@RequiredArgsConstructor
public class AddressProvider implements DataProvider {

    public static final Set<DataField> SUPPORTED_FIELDS = Set.of(ADDRESS_CITY, ADDRESS_STREET);

    private final AddressRepository addressRepository;

    @Override
    public Set<SupportedMapping> supports() {
        return Collections.singleton(new SupportedMapping(ADDRESS_ID, Set.of(ADDRESS_CITY, ADDRESS_STREET)));
    }

    @Override
    public Set<Data> get(Data source, Set<DataField> outputModel) {
        var dataField = source.getDataField();
        var value = source.getValue();
        if (ADDRESS_ID.equals(dataField) && SUPPORTED_FIELDS.containsAll(outputModel) && value instanceof Number number) {
            var address = addressRepository.findById(number.longValue()).orElseThrow(() -> new RuntimeException("Address not found: " + value));
            var outputData = new HashSet<Data>();
            if (outputModel.contains(ADDRESS_CITY)) {
                outputData.add(new Data(ADDRESS_CITY, address.getCity()));
            }
            if (outputModel.contains(ADDRESS_STREET)) {
                outputData.add(new Data(ADDRESS_STREET, address.getStreet()));
            }
            return outputData;
        }

        throw new RuntimeException("Data cannot be collected, outputModel: " + outputModel + " source: " + source);
    }
}
