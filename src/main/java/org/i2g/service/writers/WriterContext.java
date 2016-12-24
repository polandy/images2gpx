package org.i2g.service.writers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WriterContext {
    private File outputDirectory;
    private File inputDirectory;
    private Boolean recursive;
    private String apiKey;
}
