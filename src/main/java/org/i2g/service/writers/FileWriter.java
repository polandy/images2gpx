package org.i2g.service.writers;


import org.i2g.model.I2GContainer;

import java.util.List;

public interface FileWriter {
    String GITHUB_URL = "https://github.com/polandy/images2gpx";
    String GPX_SCHEMA_VERSION = "1.1";
    String PROJECT_NAME = "Images2Gpx";


    void write(List<I2GContainer> fileLocationMapping, String outputFilePath);
}
