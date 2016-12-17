package org.i2g.service.writers;


import org.i2g.jaxb.types.*;
import org.i2g.model.I2GContainer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("gpxFileWriter")
public class GpxFileWriterService implements FileWriter, ResourceLoaderAware {

    private JAXBContext jaxbContext;

    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        try {
            jaxbContext = JAXBContext.newInstance(GpxType.class);
        } catch (JAXBException e) {
            System.out.println("ERROR: could nod initialize the JAXB Context. Terminate program.");
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputFilePath) {
        try {
            JAXBElement<GpxType> gpxElement = new ObjectFactory().createGpx(getGpxType(fileLocationMapping));

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(outputFilePath);

            jaxbMarshaller.marshal(gpxElement, file);

            validateXml(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateXml(File xmlFile) throws JAXBException, IOException, SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        File xsdFile = resourceLoader.getResource("classpath:/xsd/gpx.xsd").getFile();
        Schema gpxSchema =  sf.newSchema(xsdFile);
        unmarshaller.setSchema(gpxSchema);
        unmarshaller.setEventHandler(new GpxValidationEventHandler());
        unmarshaller.unmarshal(xmlFile);
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

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
