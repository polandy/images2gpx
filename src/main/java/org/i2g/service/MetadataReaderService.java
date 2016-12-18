package org.i2g.service;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.apache.commons.lang3.ObjectUtils;
import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MetadataReaderService implements MetadataReader {

    @Override
    public List<I2GContainer> getI2GContainers(List<File> imageFiles) {
        List<I2GContainer> photoLocations = new ArrayList<>();
        for (File file : imageFiles) {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                GeoLocation geoLocation = getGeolocation(metadata);
                LocalDateTime captureDate = getCaptureDate(metadata);
                // Integer gpsAltitude = getGpsAltitude(metadata);

                if (ObjectUtils.allNotNull(geoLocation, captureDate)) {
                    photoLocations.add(new I2GContainer(file, geoLocation, captureDate));
                }
            } catch (ImageProcessingException e) {
                System.out.println(String.format("Could not read img %s\n Reason:%s", file.getAbsolutePath(), e.getMessage()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photoLocations;
    }

    @Override
    public LocalDateTime getCaptureDate(Metadata metadata) {
        return metadata.getDirectoriesOfType(ExifSubIFDDirectory.class).stream()
                .map(exifSubIFDDirectories -> exifSubIFDDirectories.getDateOriginal())
                .filter(Objects::nonNull)
                .findFirst()
                .map(captureDate -> toLocalDateTime(captureDate))
                .orElse(null);
    }

    private LocalDateTime toLocalDateTime(Date captureDate) {
        return captureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public GeoLocation getGeolocation(Metadata metadata) {
        return Optional.ofNullable(metadata.getDirectoriesOfType(GpsDirectory.class))
                .map(gpsDirs -> nonZeroGeoLocation(gpsDirs))
                .orElse(null);
    }

    private GeoLocation nonZeroGeoLocation(Collection<GpsDirectory> gpsDirectories) {
        return gpsDirectories.stream()
                .map(gpsDir -> gpsDir.getGeoLocation())
                .filter(Objects::nonNull)
                .filter(geoLoc -> !geoLoc.isZero())
                .findFirst().orElse(null);
    }

}
