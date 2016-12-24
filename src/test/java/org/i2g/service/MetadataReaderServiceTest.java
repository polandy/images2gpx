package org.i2g.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import org.hamcrest.CoreMatchers;
import org.i2g.model.I2GContainer;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class MetadataReaderServiceTest {

    MetadataReader service = new MetadataReaderService();

    FileReader fileReaderService = new FileReaderService();
    private static final Double TOLERANCE = 0.01;

    private static final String IMAGE_WITH_GPS_DATA_PATH = "/images/image_with_gps_data.jpg";
    private static final String IMAGE_WITHOUT_GPS_DATA_PATH = "/images/image_without_gps_data.jpg";
    private static final String IMAGE_WITH_GPS_ALTITUDE_PATH = "/images/image_with_gps_altitude.jpg";
    private static final String IMAGE_WITHOUT_GPS_ALTITUDE_PATH = "/images/image_without_gps_altitude.jpg";
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
    public void shouldReadCorrectGpsAltitude() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITH_GPS_ALTITUDE_PATH);
        // when
        Integer gpsAltitude = service.getGpsAltitude(metadata);
        // then
        Assert.assertThat(gpsAltitude, is(630));
    }

    @Test
    public void shouldHandleImageWithoutGpsAltitude() throws ImageProcessingException, IOException {
        // given
        Metadata metadata = readMetadata(IMAGE_WITHOUT_GPS_ALTITUDE_PATH);
        // when
        Integer gpsAltitude = service.getGpsAltitude(metadata);
        // then
        Assert.assertThat(gpsAltitude, is(CoreMatchers.nullValue()));
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

    @Test
    public void shouldOrderImagesByCaptureDate() {
        // given
        int filesInDirectory = 5;
        File inputDirectory = new File(this.getClass().getResource("/test-ordered-by-date").getFile());
        List<File> files = fileReaderService.readFiles(inputDirectory, false);
        List<I2GContainer> i2GContainers = service.getI2GContainers(files);
        List<I2GContainer> sortedContainers = new ArrayList<>();
        sortedContainers.addAll(i2GContainers);

        // when
        Collections.sort(sortedContainers);

        // then
        Assert.assertThat(i2GContainers.size(), is(filesInDirectory));
        Assert.assertThat(i2GContainers, is(sortedContainers));
    }

    @Test
    public void shouldOrderImagesByCaptureDateRecursive() {
        // given
        int filesInDirectory = 5;
        int filesInSubDir = 2;
        File inputDirectory = new File(this.getClass().getResource("/test-ordered-by-date").getFile());
        List<File> files = fileReaderService.readFiles(inputDirectory, true);
        List<I2GContainer> i2GContainers = service.getI2GContainers(files);
        List<I2GContainer> sortedContainers = new ArrayList<>();
        sortedContainers.addAll(i2GContainers);

        // when
        Collections.sort(sortedContainers);

        // then
        Assert.assertThat(i2GContainers.size(), is(filesInDirectory + filesInSubDir));
        Assert.assertThat(i2GContainers, is(sortedContainers));
    }

    private Metadata readMetadata(String filepath) throws ImageProcessingException, IOException {
        File imageFile = new File(this.getClass().getResource(filepath).getFile());
        return ImageMetadataReader.readMetadata(imageFile);
    }


}
