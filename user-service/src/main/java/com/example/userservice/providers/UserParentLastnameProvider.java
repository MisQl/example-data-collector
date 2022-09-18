package com.example.userservice.providers;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.SimpleStep;
import com.example.dataprovider.DataProvider;
import com.example.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.datacollector.core.DataField.USER_PARENT_ID;
import static com.example.datacollector.core.DataField.USER_PARENT_LASTNAME;

@Component
@RequiredArgsConstructor
public class UserParentLastnameProvider implements DataProvider {

    private final UserRepository userRepository;

    @Override
    public SimpleStep supports() {
        return new SimpleStep(USER_PARENT_ID, USER_PARENT_LASTNAME);
    }

    @Override
    public Optional<Data> get(Data source) {
        var dataField = source.getDataField();
        var value = source.getValue();
        if (!dataField.equals(USER_PARENT_ID)) {
            return Optional.empty();
        }
        if (value instanceof Number number) {
            return userRepository.findById(number.longValue()).map(user -> new Data(USER_PARENT_LASTNAME, user.getLastname()));
        }
        return Optional.empty();
    }
}
