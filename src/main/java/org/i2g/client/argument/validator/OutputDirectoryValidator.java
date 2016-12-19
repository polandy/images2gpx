package org.i2g.client.argument.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class OutputDirectoryValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        File file = new File(value);
        if (!file.exists()) {
            throw new ParameterException(String.format("Directory %s does not exist", value));
        }
        if (!file.canWrite()) {
            throw new ParameterException(String.format("Directory %s is not writable", value));
        }
    }
}
