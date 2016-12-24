package org.i2g.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.i2g.client.argument.converter.FileConverter;
import org.i2g.client.argument.converter.OutputTypeConverter;
import org.i2g.client.argument.validator.OutputDirectoryValidator;
import org.i2g.client.argument.validator.OutputTypeValidator;
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

    @Parameter(names = {"-i", "--inputDirectory"},
            converter = FileConverter.class,
            description = "Directory containing your images", required = true)
    private File inputDirectory;

    @Parameter(names = {"-o", "--outputDirectory"},
            validateWith = OutputDirectoryValidator.class,
            converter = FileConverter.class,
            description = "Output directory")
    private File outputDirectory = new File(System.getProperty("user.dir"));

    @Parameter(names = {"-t", "outputType"},
            validateWith = OutputTypeValidator.class,
            converter = OutputTypeConverter.class,
            description = "Default: gpx, possible values:\n" +
                    "\tgpx\ta gpx file")
    private OutputType outputType = OutputType.GPX;

    @Parameter(names = {"-r", "--recursive"},
            description = "Read the inputDirectory recursively"
    )
    private boolean recursive = false;

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

        String outputFilePath = String.format("%s", argsContainer.outputDirectory);

        System.out.println(String.format("Processing all files in directory \"%s\"", argsContainer.inputDirectory));
        System.out.println(String.format("Writing to %s", outputFilePath));

        List<File> allImageFiles = fileReaderService.readFiles(argsContainer.inputDirectory, argsContainer.recursive);
        List<I2GContainer> containers = metadataReaderService.getI2GContainers(allImageFiles);
        System.out.println(containers);

        // write coordinates to file
        FileWriter writer = writerRegistry.get(argsContainer.outputType);
        writer.write(containers, outputFilePath);
        System.exit(0);
    }
}
