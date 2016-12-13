package org.i2g.service.writers;

public enum OutputType {
    GPX("gpx");
//    GOOGLE_MAPS("googlemaps");

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
}
