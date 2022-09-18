package com.example.userservice.providers;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.SimpleStep;
import com.example.dataprovider.DataProvider;
import com.example.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.datacollector.core.DataField.USER_FIRSTNAME;
import static com.example.datacollector.core.DataField.USER_ID;

@Component
@RequiredArgsConstructor
public class UserFirstnameProvider implements DataProvider {

    private final UserRepository userRepository;

    @Override
    public SimpleStep supports() {
        return new SimpleStep(USER_ID, USER_FIRSTNAME);
    }

    @Override
    public Optional<Data> get(Data source) {
        var dataField = source.getDataField();
        var value = source.getValue();
        if (!dataField.equals(USER_ID)) {
            return Optional.empty();
        }
        if (value instanceof Number number) {
            return userRepository.findById(number.longValue()).map(user -> new Data(USER_FIRSTNAME, user.getFirstname()));
        }
        return Optional.empty();
    }
}
