import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import org.i2g.service.MetadataReaderService;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MetadataReaderServiceTest {

    MetadataReaderService service = new MetadataReaderService();
    private static final Double TOLERANCE = 0.01;

    public static final String IMAGE_WITH_GPS_DATA_PATH = "/images/image_with_gps_data.jpg";
    public static final String IMAGE_WITHOUT_GPS_DATA_PATH = "/images/image_without_gps_data.jpg";

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

    private Metadata readMetadata(String filepath) throws ImageProcessingException, IOException {
        File imageFile = new File(this.getClass().getResource(filepath).getFile());
        return ImageMetadataReader.readMetadata(imageFile);
    }


}
