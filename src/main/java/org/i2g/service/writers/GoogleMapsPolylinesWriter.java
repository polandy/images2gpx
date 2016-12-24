package org.i2g.service.writers;


import com.drew.lang.GeoLocation;
import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service("googleMapsPolylinesWriter")
public class GoogleMapsPolylinesWriter extends AbstractTemplateWriter {

    private static final String TEMPLATE_PATH = "gmaps-templates/polylines.html";

    private static final String COORDINATE_TEMPLATE = "{lat: %s, lng: %s}";
    private static final String PLACEHOLDER_TITLE = "TITLE";
    private static final String PLACEHOLDER_API_KEY = "API_KEY";
    private static final String PLACEHOLDER_COORDINATES = "COORDINATES";

    @Override
    protected String insertCoordinates(String content, List<I2GContainer> fileLocationMapping) throws IOException {
        StringBuffer coordinates = new StringBuffer();
        Iterator<I2GContainer> iterator = fileLocationMapping.iterator();
        while (iterator.hasNext()) {
            I2GContainer next = iterator.next();
            GeoLocation location = next.getLocation();
            coordinates.append(String.format(COORDINATE_TEMPLATE, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
            coordinates.append(iterator.hasNext() ? ",\n\t" : "");
        }
        return content.replaceAll(buildPlaceholder(PLACEHOLDER_COORDINATES), coordinates.toString());
    }

    @Override
    protected String replacePlaceHolders(String templateContent) {
        return templateContent
                .replaceAll(buildPlaceholder(PLACEHOLDER_TITLE), PROJECT_NAME)
                .replaceAll(buildPlaceholder(PLACEHOLDER_API_KEY), "");
    }

    @Override
    protected String getTemplatePath() {
        return TEMPLATE_PATH;
    }
}
