package org.i2g;

import org.i2g.client.Images2GpxCommandLineRunner;
import org.i2g.service.FileReader;
import org.i2g.service.MetadataReader;
import org.i2g.service.writers.FileWriter;
import org.i2g.service.writers.OutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    @Qualifier("gpxFileWriter")
    private FileWriter gpxFileWriterService;

    @Autowired
    @Qualifier("googleMapsMarkersWriter")
    private FileWriter googleMapsMarkersWriter;

    @Autowired
    @Qualifier("googleMapsPolylinesWriter")
    private FileWriter googleMapsPolylinesWriter;

    @Autowired
    @Qualifier("fileReaderService")
    private FileReader fileReaderService;

    @Autowired
    private MetadataReader metadataReaderService;

    @Bean
    public CommandLineRunner getImages2GpxCommandLineRunner() {
        return new Images2GpxCommandLineRunner(fileReaderService, metadataReaderService, getWriterRegistry());
    }

    @Bean
    public HashMap<OutputType, FileWriter> getWriterRegistry() {
        HashMap<OutputType, FileWriter> writers = new HashMap<>();
        writers.put(OutputType.GPX, gpxFileWriterService);
        writers.put(OutputType.GM_MARKERS, googleMapsMarkersWriter);
        writers.put(OutputType.GM_POLYLINES, googleMapsPolylinesWriter);
        return writers;
    }
}
