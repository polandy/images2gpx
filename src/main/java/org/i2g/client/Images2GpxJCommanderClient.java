package org.i2g.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.i2g.service.FileReaderService;

import java.io.File;
import java.util.List;

public class Images2GpxJCommanderClient {

    @Parameter(names = {"-i", "--imageDirectory"}, description = "Directory containing your images", required = true)
    private
    String imageDirectory;

    @Parameter(names = {"-o", "--outputDirectory"}, description = "Output directory")
    private
    String outputDirectory;

    private String filepath;

    private FileReaderService fileReaderService;

    public static void main(String[] args) {
        Images2GpxJCommanderClient c = new Images2GpxJCommanderClient();
        new JCommander(c,args);
        c.run();
    }

    private void run() {
        this.outputDirectory = StringUtils.isEmpty(outputDirectory) ? System.getProperty("user.dir") : outputDirectory;
        this.filepath = createAndGetOutputFilePath();

        System.out.println(String.format("Processing all files in directory \"%s\"", this.imageDirectory));
        System.out.println(String.format("Writing to %s", this.filepath));

        // read files and metadata
        List<File> allImageFiles = fileReaderService.readFiles(this.imageDirectory, false);
        System.out.println(String.format("%s Files :", allImageFiles.size()));
        allImageFiles.forEach(f -> System.out.println(String.format("\t-%s", f.getAbsoluteFile())));

        // write coordinates to file
        // writer.write(fileLocationMapping, InputStream);

    }

    private String createAndGetOutputFilePath() {
        String dir = this.outputDirectory;
        String filename = "output.gpx";
        return dir + "/" + filename;
    }

}
