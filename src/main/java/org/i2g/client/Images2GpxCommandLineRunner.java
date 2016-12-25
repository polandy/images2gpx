package org.i2g.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.i2g.client.argument.converter.FileConverter;
import org.i2g.client.argument.converter.OutputTypeConverter;
import org.i2g.client.argument.validator.OutputDirectoryValidator;
import org.i2g.client.argument.validator.OutputTypeValidator;
import org.i2g.model.I2GContainer;
import org.i2g.service.FileReader;
import org.i2g.service.MetadataReader;
import org.i2g.service.writers.FileWriter;
import org.i2g.service.writers.OutputType;
import org.i2g.service.writers.WriterContext;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Images2GpxCommandLineRunner implements CommandLineRunner {

    @Parameter(names = {"-i", "--inputDirectory"},
            converter = FileConverter.class,
            description = "Directory containing your images", required = true)
    private File inputDirectory;

    @Parameter(names = {"-o", "--outputDirectory"},
            validateWith = OutputDirectoryValidator.class,
            converter = FileConverter.class,
            description = "Output Directory")
    private File outputDirectory = new File(System.getProperty("user.dir"));

    @Parameter(names = {"-t", "outputType"},
            validateWith = OutputTypeValidator.class,
            converter = OutputTypeConverter.class,
            description = "supported types [gpx, google-maps-markers, google-maps-polylines]")
    private OutputType outputType = OutputType.GPX;

    @Parameter(names = {"-r", "--recursive"},
            description = "Read the inputDirectory recursively")
    private boolean recursive = false;

    @Parameter(names = {"-k", "--apikey"},
            description = "Google Maps API Key. Only used for OutputType google-maps-polyline"
    )
    private String apiKey;

    @Parameter(names = {"?", "-h", "--help"}, help = true)
    boolean help = false;

    @NonNull
    private FileReader fileReaderService;

    @NonNull
    private MetadataReader metadataReaderService;

    @NonNull
    private Map<OutputType, FileWriter> writerRegistry;


    public void run(String[] args) {
        Images2GpxCommandLineRunner jcContext = new Images2GpxCommandLineRunner();
        JCommander jCommander = new JCommander(jcContext, args);
        ifHelpPrintAndExit(jcContext.help, jCommander);

        WriterContext wc = createWriterContext(jcContext);

        System.out.println(String.format("Processing all files in directory \"%s\"", wc.getInputDirectory()));
        System.out.println(String.format("Writing to %s", wc.getOutputDirectory()));

        List<File> allImageFiles = fileReaderService.readFiles(wc.getInputDirectory(), wc.getRecursive());
        List<I2GContainer> containers = metadataReaderService.getI2GContainers(allImageFiles);
        System.out.println(containers);

        // write coordinates to file
        FileWriter writer = writerRegistry.get(jcContext.outputType);
        writer.write(containers, wc);
        System.exit(0);
    }

    private void ifHelpPrintAndExit(boolean help, JCommander jCommander) {
        if (help) {
            jCommander.usage();
            System.exit(0);
        }
    }

    private WriterContext createWriterContext(Images2GpxCommandLineRunner args) {
        if (args.outputType == OutputType.GM_POLYLINES && StringUtils.isEmpty(args.apiKey)) {
            System.out.println("for google-maps-polyline the attribute 'apikey' is required!");
            System.out.println("You can create a new API Key here: https://console.developers.google.com/apis/credentials");
        }

        return WriterContext.builder()
                .inputDirectory(args.inputDirectory)
                .outputDirectory(args.outputDirectory)
                .recursive(args.recursive)
                .apiKey(args.apiKey)
                .build();
    }
}
