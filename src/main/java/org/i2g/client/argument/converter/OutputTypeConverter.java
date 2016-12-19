package org.i2g.client.argument.converter;

import com.beust.jcommander.IStringConverter;
import org.i2g.service.writers.OutputType;

public class OutputTypeConverter implements IStringConverter<OutputType> {

    @Override
    public OutputType convert(String value) {
        return OutputType.getByValue(value);
    }
}
