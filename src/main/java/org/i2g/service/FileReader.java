package org.i2g.service;

import java.io.File;
import java.util.List;

public interface FileReader {

    List<File> readFiles(String inputDirectory, boolean isRecursive);
}
