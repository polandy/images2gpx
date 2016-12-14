package org.i2g.out.writers;


import org.i2g.model.I2GContainer;
import org.i2g.service.writers.FileWriter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("gpxFileWriter")
public class GpxFileWriterService implements FileWriter {

    @Override
    public void write(List<I2GContainer> fileLocationMapping, String outputfilePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = createRootElementWithAttributes(doc);
            doc.appendChild(rootElement);

            Element metadata = createMetadateElement(doc);
            rootElement.appendChild(metadata);

            // build track
            Element track = createTrackElement(doc);
            rootElement.appendChild(track);

            // add all cordinates to one track segment
            Element trackSegment = doc.createElement("trkseg");
            track.appendChild(trackSegment);

            // write coordinates
            for (I2GContainer container: fileLocationMapping) {
//      <trkpt lat="50.716779660433531" lon="6.445616148412228">
//        <ele>166.44999999999999</ele>
//        <time>2012-12-03T09:08:42Z</time>
//      </trkpt>
                Element trkpt = doc.createElement("trkpt");
                trkpt.setAttribute("lat", String.valueOf(container.getLocation().getLatitude()));
                trkpt.setAttribute("lon", String.valueOf(container.getLocation().getLongitude()));
//                Element time = doc.createElement("time");
//                time.setTextContent(container.getCaptureDate()); // TODO
//                trkpt.appendChild(time);
                trackSegment.appendChild(trkpt);
            }

            // write the content into xml file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputfilePath));
//            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);



        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    private Element createTrackElement(Document doc) {
        Element track = doc.createElement("trk");
        Element track_name = doc.createElement("name");
        Element track_extensions = doc.createElement("extensions");
        Element track_extensions_extension = doc.createElement("TrackExtension");
        Element track_extensions_extension_displayColor = doc.createElementNS("gpxx", "DisplayColor");
        track_extensions_extension_displayColor.setTextContent("red");
        track.appendChild(track_name);
        track.appendChild(track_extensions);
        track_extensions.appendChild(track_extensions_extension);
        track_extensions_extension.appendChild(track_extensions_extension_displayColor);
        return track;
    }

    /*  <metadata>
    <link href="http://www.garmin.com">
      <text>Garmin International</text>
    </link>
    <time>2012-12-03T18:05:03Z</time>
    <bounds maxlat="50.718614039942622" maxlon="6.465599527582526" minlat="50.702664786949754" minlon="6.442962186411023" />
  </metadata>*/
    private Element createMetadateElement(Document doc) {
        Element metadata = doc.createElement("metadata");
        Element metadata_link = doc.createElement("link");
        metadata_link.setAttribute("href", GITHUB_URL);
        Element metadata_link_text = doc.createElement("text");
        metadata_link_text.setNodeValue("Images2Gpx");
        Element metadata_time = doc.createElement("time");
        metadata_time.setNodeValue(getIso8601TimeFormat(LocalDateTime.now()));
//        Element metadata_bounds = doc.createElement("bounds");
//        metadata_bounds.setNodeValue("TODO");

        metadata_link.appendChild(metadata_link_text);

        metadata.appendChild(metadata_link);
        metadata.appendChild(metadata_time);
//        metadata.appendChild(metadata_bounds);

        return metadata;
    }

    private Element createRootElementWithAttributes(Document doc) {
        Element rootElement = doc.createElement("gpx");
        rootElement.setAttribute("creator", "Image2Gpx");
        rootElement.setAttribute("version", "0.1");
        rootElement.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/ActivityExtension/v1 http://www8.garmin.com/xmlschemas/ActivityExtensionv1.xsd http://www.garmin.com/xmlschemas/AdventuresExtensions/v1 http://www8.garmin.com/xmlschemas/AdventuresExtensionv1.xsd http://www.garmin.com/xmlschemas/PressureExtension/v1 http://www.garmin.com/xmlschemas/PressureExtensionv1.xsd");
        rootElement.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
        rootElement.setAttribute("xmlns:gpxtrx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
        rootElement.setAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
        rootElement.setAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
        rootElement.setAttribute("xmlns:trp", "http://www.garmin.com/xmlschemas/TripExtensions/v1");
        rootElement.setAttribute("xmlns:adv", "http://www.garmin.com/xmlschemas/AdventuresExtensions/v1");
        rootElement.setAttribute("xmlns:prs", "http://www.garmin.com/xmlschemas/PressureExtension/v1");
        return rootElement;
    }

    String getIso8601TimeFormat(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
        return date.format(formatter);
    }
}
