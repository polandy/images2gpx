package org.i2g.client.argument.converter;

import com.beust.jcommander.IStringConverter;

import java.io.File;

public class FileConverter implements IStringConverter<File> {

    @Override
    public File convert(String value) {
        return new File(value);
    }
}
