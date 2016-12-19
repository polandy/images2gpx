package org.i2g.service;


import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReaderService implements FileReader {

    @Override
    public List<File> readFiles(File inputDirectory, boolean isRecursive) {
        List<File> imageFiles = new ArrayList<>();
        return getAllImages(inputDirectory, imageFiles, isRecursive);
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
