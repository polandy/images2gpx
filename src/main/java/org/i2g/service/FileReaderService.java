package org.i2g.service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileReaderService {

    private static FileReaderService instance;

    private FileReaderService() {
    }

    public static FileReaderService getInstance() {
        if (instance == null) {
            instance = new FileReaderService();
        }
        return instance;
    }

    public List<File> readFiles(String inputDirectory) {
        return readFiles(inputDirectory, false);
    }

    private List<File> readFiles(String inputDirectory, boolean isRecursive) {
        List<File> imageFiles = new ArrayList<>();
        File folder = new File(inputDirectory);
        return getAllImages(folder, imageFiles, isRecursive);
    }

    private List<File> getAllImages(File inputDirectory, List<File> imageFiles, boolean isRecursive) {
        for (final File fileEntry : inputDirectory.listFiles()) {
            if (fileEntry.isDirectory() && isRecursive) {
                getAllImages(fileEntry, imageFiles, isRecursive);
            } else {
                if (fileEntry.getName().endsWith(".jpg")) {
                    imageFiles.add(fileEntry);
                }
            }
        }
        return imageFiles;
    }
}
