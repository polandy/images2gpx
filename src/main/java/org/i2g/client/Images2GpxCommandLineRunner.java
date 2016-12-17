package org.i2g.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.i2g.model.I2GContainer;
import org.i2g.service.FileReader;
import org.i2g.service.MetadataReader;
import org.i2g.service.writers.FileWriter;
import org.i2g.service.writers.OutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class Images2GpxCommandLineRunner implements CommandLineRunner {

    @Parameter(names = {"-i", "--inputDirectory"}, description = "Directory containing your images", required = true)
    private
    String inputDirectory;

    @Parameter(names = {"-o", "--outputDirectory"}, description = "Output directory")
    private
    String outputDirectory;


    @Autowired
    private FileReader fileReaderService;

    @Autowired
    private MetadataReader metadataReaderService;

    @Autowired
    private Map<OutputType, FileWriter> writerRegistry;

    public Images2GpxCommandLineRunner(Map<OutputType, FileWriter> writerRegistry) {
        this.writerRegistry = writerRegistry;
    }

    private Images2GpxCommandLineRunner() {}

    public void run(String[] args) {
        Images2GpxCommandLineRunner argsContainer = new Images2GpxCommandLineRunner();
        new JCommander(argsContainer, args);

        argsContainer.outputDirectory = StringUtils.isEmpty(argsContainer.outputDirectory) ? System.getProperty("user.dir") : argsContainer.outputDirectory;
        String outputFilePath = String.format("%s/%s", argsContainer.outputDirectory, "output.gpx");

        System.out.println(String.format("Processing all files in directory \"%s\"", argsContainer.inputDirectory));
        System.out.println(String.format("Writing to %s", outputFilePath));

        // read files and metadata
        List<File> allImageFiles = fileReaderService.readFiles(argsContainer.inputDirectory, false);
        // System.out.println(String.format("%s Files :", allImageFiles.size()));
        // allImageFiles.forEach(f -> System.out.println(String.format("\t-%s", f.getAbsoluteFile())));
        List<I2GContainer> containers = metadataReaderService.getI2GContainers(allImageFiles);
        System.out.println(containers);
        // containers.forEach(img -> System.out.println(String.format("%s -> (%s, %s)", img.getImagefile().getName(), img.getLocation().getLatitude(), img.getLocation().getLongitude())));

        // write coordinates to file
        FileWriter writer = writerRegistry.get(OutputType.GPX);
        writer.write(containers, outputFilePath);
    }
}
