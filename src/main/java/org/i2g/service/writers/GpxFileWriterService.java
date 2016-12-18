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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
            JAXBElement<GpxType> gpxElement = new ObjectFactory().createGpx(getGpxType(fileLocationMapping));

        Marshaller jaxbMarshaller;
        File file = new File(outputFilePath);
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(gpxElement, file);
            validateXml(file);
            System.out.println("XML has been written successfully: " + file.getAbsoluteFile());
        } catch (JAXBException e) {
            // could not create marshaller / unmarshaller, invalid xml
            System.out.println("Invalid xml, could not generate gpx file");
            file.delete();
            e.printStackTrace();
        } catch (IOException e) {
            // could not read resource (schema not found)
            e.printStackTrace();
        } catch (SAXException e) {
            // could not initialize Schema (schema corrupt)
            e.printStackTrace();
        }
    }

    private void validateXml(File xmlFile) throws JAXBException, IOException, SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        File xsdFile = resourceLoader.getResource("classpath:/xsd/gpx.xsd").getFile();
        Schema gpxSchema =  sf.newSchema(xsdFile);
        unmarshaller.setEventHandler(new GpxValidationEventHandler());
        unmarshaller.setSchema(gpxSchema);
        unmarshaller.unmarshal(xmlFile);
    }

    private GpxType getGpxType(List<I2GContainer> fileLocationMapping) {

        GpxType gpxType = new GpxType();
        gpxType.setMetadata(getMetadataType());
        gpxType.setCreator(PROJECT_NAME);
        gpxType.setVersion(GPX_SCHEMA_VERSION);

        TrkType track = new TrkType();
        TrksegType trackSegment = new TrksegType();
        track.getTrkseg().add(trackSegment);
        gpxType.getTrk().add(track);

        trackSegment.getTrkpt().addAll(getWaypointTypes(fileLocationMapping));
        return gpxType;
    }

    protected List<WptType> getWaypointTypes(List<I2GContainer> fileLocationMapping) {
        List<WptType> trackPoints = new ArrayList<>();
        fileLocationMapping.stream()
                .forEach(container ->
                        trackPoints.add(getWaypointType(container))
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

    protected WptType getWaypointType(I2GContainer container) {
        WptType waypoint = new WptType();
        waypoint.setLat(BigDecimal.valueOf(container.getLocation().getLatitude()));
        waypoint.setLon(BigDecimal.valueOf(container.getLocation().getLongitude()));
        waypoint = addCaptureDateToWaypoint(waypoint , container);
        return waypoint;
    }

    private WptType addCaptureDateToWaypoint(WptType waypoint, I2GContainer container) {
        try {
            XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(container.getCaptureDate().toString());
            waypoint.setTime(cal);
        } catch (DatatypeConfigurationException e) {
            // TODO log the error
        }
        return waypoint;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
