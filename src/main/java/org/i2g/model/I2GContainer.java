package org.i2g.model;

import com.drew.lang.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Container object which contains all data which are used for the writers
 */
@AllArgsConstructor
@Data
public class I2GContainer {
    private File imagefile;
    private LocalDateTime captureDate;
    private GeoLocation location;
    private Integer gpsAltitude;
}
