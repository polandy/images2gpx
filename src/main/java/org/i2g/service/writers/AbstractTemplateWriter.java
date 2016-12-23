package org.i2g.service.writers;


import org.apache.commons.io.FileUtils;
import org.i2g.model.I2GContainer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractTemplateWriter implements FileWriter {
    private static final String PLACEHOLDER_TEMPLATE = "\\{\\{ %s \\}\\}";

    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath){
        try {
            String templateContent = getTemplateAsString(getTemplatePath());
            String content = replacePlaceHolders(templateContent);
            content = insertCoordinates(content, fileLocationMapping);
            writeContentToFile(content, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String insertCoordinates(String content, List<I2GContainer> fileLocationMapping) throws IOException;

    protected abstract String replacePlaceHolders(String templateContent);

    protected abstract String getTemplatePath();

    String getTemplateAsString(String templatePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream templateInputStream = resource.getInputStream();
        File copiedTemplate = File.createTempFile("tmp__template", ".html");
        FileUtils.copyInputStreamToFile(templateInputStream, copiedTemplate);
        return new Scanner(copiedTemplate).useDelimiter("\\Z").next();
    }

    /**
     * @param placeholder
     * @return a String like "{{ VALUE_TO_REPLACE }}"
     */
    String buildPlaceholder(String placeholder) {
        return String.format(PLACEHOLDER_TEMPLATE, placeholder);
    }

    private void writeContentToFile(String content, String outputFilePath) throws IOException {
        File outpFile = new File(outputFilePath + "/googlemapspolyline.html");
        outpFile.createNewFile();
        PrintWriter writer = new PrintWriter(outpFile, "UTF-8");
        writer.println(content);
        writer.close();
        System.out.println("File has been written: " + outpFile.getAbsolutePath());
    }

}
