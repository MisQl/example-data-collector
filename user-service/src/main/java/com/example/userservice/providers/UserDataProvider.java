package com.example.userservice.providers;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.core.DataProvider;
import com.example.datacollector.core.SupportedMapping;
import com.example.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.datacollector.core.DataField.*;

@Component
@RequiredArgsConstructor
public class UserDataProvider implements DataProvider {

    private static final Set<DataField> SUPPORTED_FIELDS = Set.of(USER_FIRSTNAME, USER_LASTNAME, USER_PARENT_ID);

    private final UserRepository userRepository;

    @Override
    public Set<SupportedMapping> supports() {
        return Collections.singleton(new SupportedMapping(USER_ID, SUPPORTED_FIELDS));
    }

    @Override
    public Set<Data> get(Data source, Set<DataField> outputModel) {
        var dataField = source.getDataField();
        var value = source.getValue();
        if (USER_ID.equals(dataField) && SUPPORTED_FIELDS.containsAll(outputModel) && value instanceof Number number) {
            var user = userRepository.findById(number.longValue()).orElseThrow(() -> new RuntimeException("User not found: " + value));
            var outputData = new HashSet<Data>();
            if (outputModel.contains(USER_FIRSTNAME)) {
                outputData.add(new Data(USER_FIRSTNAME, user.getFirstname()));
            }
            if (outputModel.contains(USER_LASTNAME)) {
                outputData.add(new Data(USER_LASTNAME, user.getLastname()));
            }
            if (outputModel.contains(USER_PARENT_ID)) {
                outputData.add(new Data(USER_PARENT_ID, user.getParentId()));
            }
            return outputData;
        }

        throw new RuntimeException("Data cannot be collected, outputModel: " + outputModel + " source: " + source);
    }
}
