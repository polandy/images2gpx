package org.i2g.service.writers;


import org.i2g.model.I2GContainer;

import java.util.List;

public interface FileWriter {
    public static final String GITHUB_URL = "https://github.com/polandy/images2gpx";

    void write(List<I2GContainer> fileLocationMapping, String outputFilePath);
}
