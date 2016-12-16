package org.i2g.service.writers;


import org.i2g.jaxb.types.*;
import org.i2g.model.I2GContainer;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("gpxFileWriter")
public class GpxFileWriterService implements FileWriter {

    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpxType.class);

            JAXBElement<GpxType> gpxElement = new ObjectFactory().createGpx(getGpxType(fileLocationMapping));


            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(outputFilePath);

//            jaxbMarshaller.marshal(gpxType, file);
            jaxbMarshaller.marshal(gpxElement, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private GpxType getGpxType(List<I2GContainer> fileLocationMapping) {

        GpxType gpxType = new GpxType();
        gpxType.setMetadata(getMetadataType());
        gpxType.setCreator("Images2Gpx");

        TrkType track = new TrkType();
        TrksegType trackSegment = new TrksegType();
        track.getTrkseg().add(trackSegment);
        gpxType.getTrk().add(track);

        trackSegment.getTrkpt().addAll(getWaypointTypes(fileLocationMapping));
        return gpxType;
    }

    private List<WptType> getWaypointTypes(List<I2GContainer> fileLocationMapping) {
        List<WptType> trackPoints = new ArrayList<>();
        fileLocationMapping.stream().map(I2GContainer::getLocation)
                .forEach(location ->
                        trackPoints.add(getWaypointType(location.getLatitude(), location.getLongitude()))
                );
        return trackPoints;
    }

    private MetadataType getMetadataType() {
        MetadataType metadataType = new MetadataType();
        metadataType.setAuthor(getPersonType());
        metadataType.setDesc(GITHUB_URL);
        // metadataType.setName();
        return metadataType;
    }

    private PersonType getPersonType() {
        PersonType personType = new PersonType();
        personType.setName(System.getProperty("user.name"));
        return personType;
    }

    private WptType getWaypointType(double latitude, double longitude) {
        WptType waypoint = new WptType();
        waypoint.setLat(BigDecimal.valueOf(latitude));
        waypoint.setLon(BigDecimal.valueOf(longitude));
        return waypoint;
    }
}
