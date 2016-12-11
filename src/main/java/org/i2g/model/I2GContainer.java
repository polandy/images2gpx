package org.i2g.model;

import com.drew.lang.GeoLocation;

import java.io.File;

/**
 * Container object which contains all data which are used for the writers
 */
public class I2GContainer {

    private File imagefile;
    private GeoLocation location;

    public I2GContainer(File imagefile, GeoLocation location) {
        this.imagefile = imagefile;
        this.location = location;
    }

    public File getImagefile() {
        return imagefile;
    }

    public void setImagefile(File imagefile) {
        this.imagefile = imagefile;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }
}
