package org.i2g.client.argument.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import org.i2g.service.writers.OutputType;

public class OutputTypeValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            OutputType.getByValue(value);
        } catch (IllegalArgumentException e) {
            // TODO Log
            throw new ParameterException(String.format("Invalid OutputType: '%s'", value));
        }
    }
}
