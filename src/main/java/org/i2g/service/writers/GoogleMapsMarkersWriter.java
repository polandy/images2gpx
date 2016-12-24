package org.i2g.service.writers;


import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service("googleMapsMarkersWriter")
public class GoogleMapsMarkersWriter extends AbstractTemplateWriter {


    private static final String TEMPLATE_PATH = "gmaps-templates/markers.html";

    private static final String PLACEHOLDER_TITLE = "TITLE";
    private static final String PLACEHOLDER_MARKERS = "MARKERS";
    private static final String PLACEHOLDER_LATITUDE = "LAT";
    private static final String PLACEHOLDER_LONGITUDE = "LONG";
    private static final String PLACEHOLDER_LOCATION = "LOCATION";

    @Override
    protected String replacePlaceHolders(String templateContent) {
        return templateContent.replaceAll(PLACEHOLDER_TITLE, PROJECT_NAME);
    }

    @Override
    protected String insertCoordinates(String content, List<I2GContainer> fileLocationMapping) throws IOException {
        StringBuilder markers = new StringBuilder();
        for (I2GContainer container : fileLocationMapping) {
            markers.append(buildMarker(container));
        }
        return content.replaceAll(buildPlaceholder(PLACEHOLDER_MARKERS), markers.toString());
    }

    private String buildMarker(I2GContainer container) throws IOException {
        String templateAsString = getTemplateAsString("gmaps-templates/markers_marker-template");
        return templateAsString
                .replaceAll(buildPlaceholder(PLACEHOLDER_LATITUDE), String.valueOf(container.getLocation().getLatitude()))
                .replaceAll(buildPlaceholder(PLACEHOLDER_LONGITUDE), String.valueOf(container.getLocation().getLongitude()))
                .replaceAll(buildPlaceholder(PLACEHOLDER_TITLE), container.getImagefile().getAbsolutePath())
                .replaceAll(buildPlaceholder(PLACEHOLDER_LOCATION), container.getImagefile().getAbsolutePath());
    }


    @Override
    protected String getTemplatePath() {
        return TEMPLATE_PATH;
    }

    @Override
    protected String getFilename() {
        return "images2gpx-google-maps-with-markers.html";
    }
}
