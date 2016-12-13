package org.i2g.service.writers;


import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

@Service("gpxFileWriter")
public class GpxFileWriterService implements FileWriter {

    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath) {
        DocumentBuilderFactory docFactory;
        DocumentBuilder docBuilder;
        System.out.println(String.format("TODO: Write XML to '%s' :-)", outputFilePath));
    }
}
