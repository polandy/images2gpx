package org.i2g.service;

import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;

public interface MetadataReader {
    GeoLocation getGeolocation(Metadata metadata);
}
