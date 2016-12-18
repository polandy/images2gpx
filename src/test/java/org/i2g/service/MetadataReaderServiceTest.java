package org.i2g.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class MetadataReaderServiceTest {

    MetadataReaderService service = new MetadataReaderService();
    private static final Double TOLERANCE = 0.01;

    private static final String IMAGE_WITH_GPS_DATA_PATH = "/images/image_with_gps_data.jpg";
    private static final String IMAGE_WITHOUT_GPS_DATA_PATH = "/images/image_without_gps_data.jpg";
    private static final String IMAGE_WITH_CAPTURE_DATE_PATH = "/images/image_with_capture_date.jpg";
    private static final String IMAGE_WITHOUT_CAPTURE_DATE_PATH = "/images/image_without_capture_date.jpg";


    @Test
    public void shouldReadCorrectGeolocation() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITH_GPS_DATA_PATH);
        // when
        GeoLocation geolocation = service.getGeolocation(metadata);
        // then
        Assert.assertEquals(22.3, geolocation.getLatitude(), TOLERANCE);
        Assert.assertEquals(103.89, geolocation.getLongitude(), TOLERANCE);
    }

    @Test
    public void shouldHandleImageWithoutGeolocation() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITHOUT_GPS_DATA_PATH);
        // when
        GeoLocation geolocation = service.getGeolocation(metadata);
        // then
        Assert.assertNull(geolocation);
    }

    @Test
    public void shouldReadCorrectCaptureDate() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITH_CAPTURE_DATE_PATH);
        LocalDateTime expectedCaptureDate = LocalDateTime.of(2016, 10, 18, 18, 28, 34);
        // when
        LocalDateTime captureDate = service.getCaptureDate(metadata);
        // then
        Assert.assertThat(captureDate, is(expectedCaptureDate));
    }

    @Test
    public void shouldHandleImageWithoutCaptureDate() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITHOUT_CAPTURE_DATE_PATH);
        // when
        LocalDateTime captureDate = service.getCaptureDate(metadata);
        // then
        Assert.assertThat(captureDate, is(CoreMatchers.nullValue()));
    }

    private Metadata readMetadata(String filepath) throws ImageProcessingException, IOException {
        File imageFile = new File(this.getClass().getResource(filepath).getFile());
        return ImageMetadataReader.readMetadata(imageFile);
    }


}
