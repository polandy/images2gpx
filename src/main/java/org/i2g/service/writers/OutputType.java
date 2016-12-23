package org.i2g.service.writers;

import java.util.Arrays;
import java.util.List;

public enum OutputType {
    GPX("gpx"),
    GOOGLE_MAPS_MARKERS("google-maps-markers");

    String value;

    OutputType(String value) {
        this.value = value;
    }

    String getAllTypesForDescription() {
        return OutputType.values().toString();
    }

    public String getValue() {
        return this.value;
    }

    public static OutputType getByValue(String value) throws IllegalArgumentException {
        List<OutputType> types = Arrays.asList(values());
        return types.stream()
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
