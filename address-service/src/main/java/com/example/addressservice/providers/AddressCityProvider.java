package com.example.addressservice.providers;

import com.example.addressservice.address.AddressRepository;
import com.example.datacollector.core.Data;
import com.example.datacollector.core.SimpleStep;
import com.example.dataprovider.DataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.datacollector.core.DataField.ADDRESS_CITY;
import static com.example.datacollector.core.DataField.ADDRESS_ID;

@Component
@RequiredArgsConstructor
public class AddressCityProvider implements DataProvider {

    private final AddressRepository addressRepository;

    @Override
    public SimpleStep supports() {
        return new SimpleStep(ADDRESS_ID, ADDRESS_CITY);
    }

    @Override
    public Optional<Data> get(Data source) {
        var dataField = source.getDataField();
        var value = source.getValue();
        if (!dataField.equals(ADDRESS_ID)) {
            return Optional.empty();
        }
        if (value instanceof Number number) {
            return addressRepository.findById(number.longValue()).map(user -> new Data(ADDRESS_CITY, user.getCity()));
        }
        return Optional.empty();
    }
}
