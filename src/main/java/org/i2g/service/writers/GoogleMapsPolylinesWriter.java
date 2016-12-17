package org.i2g.service.writers;


import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("googleMapsPolylinesWriter")
public class GoogleMapsPolylinesWriter implements FileWriter {
    private static final String PLACEHOLDER_TEMPLATE = "{{ %s }}";
    private static final String PLACEHOLDER_TITLE = "TITLE";
    private static final String PLACEHOLDER_API_KEY = "API_KEY";
    private static final String PLACEHOLDER_COORDINATES = "COORDINATES";


    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath) {
        System.out.println("outofsda");
    }
}
