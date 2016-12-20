package org.i2g.service.writers;


import com.drew.lang.GeoLocation;
import org.i2g.jaxb.types.WptType;
import org.i2g.model.I2GContainer;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class GpxFileWriterServiceTest  {

    GpxFileWriterService service = new GpxFileWriterService();

    @Test
    public void shouldWriteValidXml() {
        // TODO
    }

    @Test
    public void shouldProcessCorruptJpegImages() {
        // TODO
    }

    @Test
    public void shouldWriteCaptureDate() throws DatatypeConfigurationException {
        LocalDateTime now = LocalDateTime.now();
        XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(now.toString());

        GeoLocation loc = new GeoLocation(1d,2d);
        I2GContainer c = new I2GContainer(null, now, loc, null);
        WptType waypoint = service.getWaypointType(c);

        Assert.assertThat(waypoint.getTime(), is(cal));

    }

}
