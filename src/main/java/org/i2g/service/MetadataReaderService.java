package org.i2g.service;


import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import java.util.Collection;

public class MetadataReaderService {

    private static MetadataReaderService instance;

    private MetadataReaderService() {}

    public static MetadataReaderService getInstance() {
        if (instance == null) {
            instance = new MetadataReaderService();
        }
        return instance;
    }

    public GeoLocation getGeolocation(Metadata metadata) {
        GeoLocation geoLocation = null;
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        if (gpsDirectories == null)
            return null;

        for (GpsDirectory gpsDirectory : gpsDirectories) {
            geoLocation = gpsDirectory.getGeoLocation();

            if (geoLocation != null && !geoLocation.isZero()) {
                return geoLocation;
            }
        }
        return geoLocation;
    }

}
