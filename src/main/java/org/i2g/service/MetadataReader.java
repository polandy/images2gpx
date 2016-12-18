package org.i2g.service;

import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import org.i2g.model.I2GContainer;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface MetadataReader {

    List<I2GContainer> getI2GContainers(List<File> imageFiles);

    GeoLocation getGeolocation(Metadata metadata);

    LocalDateTime getCaptureDate(Metadata metadata);
}
