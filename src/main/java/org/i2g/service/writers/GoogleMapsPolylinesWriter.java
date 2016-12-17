package org.i2g.service.writers;


import com.drew.lang.GeoLocation;
import org.apache.commons.io.FileUtils;
import org.i2g.model.I2GContainer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@Service("googleMapsPolylinesWriter")
public class GoogleMapsPolylinesWriter implements FileWriter {
    private static final String PLACEHOLDER_TEMPLATE = "\\{\\{ %s \\}\\}";
    private static final String COORDINATE_TEMPLATE = "{lat: %s, lng: %s}";
    private static final String PLACEHOLDER_TITLE = "TITLE";
    private static final String PLACEHOLDER_API_KEY = "API_KEY";
    private static final String PLACEHOLDER_COORDINATES = "COORDINATES";


    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath) {
        try {
            File template = getTemplate();
            String coordinates = getCoordinates(fileLocationMapping);
            String templateContent= new Scanner(template).useDelimiter("\\Z").next();
            String content = templateContent
                    .replaceAll(getPlaceholder(PLACEHOLDER_TITLE), PROJECT_NAME)
                    .replaceAll(getPlaceholder(PLACEHOLDER_API_KEY), "AIzaSyCBkTBsj05_eMoBmKxb5yNl7Tzfgppz8t4")
                    .replaceAll(getPlaceholder(PLACEHOLDER_COORDINATES), coordinates);
            writeContentToFile(content, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeContentToFile(String content, String outputFilePath) throws IOException {
        File outpFile = new File(outputFilePath + "/googlemapspolyline.html");
        outpFile.createNewFile();
        PrintWriter writer = new PrintWriter(outpFile, "UTF-8");
        writer.println(content);
        writer.close();
        System.out.println("Google Maps Polylines html page generated: " + outpFile.getAbsolutePath());
    }

    private String getCoordinates(List<I2GContainer> fileLocationMapping) {
        StringBuffer coordinates = new StringBuffer();
        Iterator<I2GContainer> iterator = fileLocationMapping.iterator();
        while (iterator.hasNext()) {
            I2GContainer next = iterator.next();
            GeoLocation location = next.getLocation();
            coordinates.append(String.format(COORDINATE_TEMPLATE, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
            coordinates.append(iterator.hasNext() ? ",\n\t" : "");
        }
        return coordinates.toString();
    }

    private File getTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("gmaps-templates/polylines.html");
        InputStream templateInputStream = resource.getInputStream();
        File copiedTemplate = File.createTempFile("tmp__template", ".html");
        FileUtils.copyInputStreamToFile(templateInputStream, copiedTemplate);
        return copiedTemplate;
    }

    private String getPlaceholder(String placeholder) {
        return String.format(PLACEHOLDER_TEMPLATE, placeholder);
    }
}
