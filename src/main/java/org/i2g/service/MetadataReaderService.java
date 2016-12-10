package org.i2g.service;


import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MetadataReaderService implements MetadataReader{

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
