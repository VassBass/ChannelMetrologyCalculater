package service.file_extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ResourceFileExtractor implements FileExtractor {
    private static final Logger logger = LoggerFactory.getLogger(ResourceFileExtractor.class);

    private static volatile ResourceFileExtractor instance;
    private ResourceFileExtractor(){}
    public static ResourceFileExtractor getInstance() {
        if (instance == null) {
            synchronized (ResourceFileExtractor.class) {
                if (instance == null) instance = new ResourceFileExtractor();
            }
        }
        return instance;
    }

    @Override
    public void extract(String source, String destination) {
        logger.info(String.format("Extracting %s to %s", source, destination));
        try (InputStream in = ResourceFileExtractor.class.getClassLoader().getResourceAsStream(source)) {
            if (in == null) {
                logger.warn(String.format("File not found: %s", source));
            } else {
                Files.copy(in, Paths.get(destination), REPLACE_EXISTING);
            }
        } catch (IOException e) {
            String message = String.format("Exception while extract %s", source);
            logger.warn(message, e);
        }
    }
}
