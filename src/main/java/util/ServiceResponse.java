package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServiceResponse<T> {

    private T response;
    private final List<String> errorMessages = new ArrayList<>();

    public Optional<T> getResponseObject() {
        return Optional.ofNullable(response);
    }

    public void setResponseObject(T response) {
        this.response = response;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void addErrorMessages(String ... messages) {
        errorMessages.addAll(Arrays.asList(messages));
    }

    public boolean hasErrorMessages() {
        return !errorMessages.isEmpty();
    }
}
