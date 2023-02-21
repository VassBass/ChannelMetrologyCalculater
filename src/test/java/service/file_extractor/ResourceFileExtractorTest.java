package service.file_extractor;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ResourceFileExtractorTest {

    private static final FileExtractor extractor = ResourceFileExtractor.getInstance();

    @Test
    public void getInstance() {
        assertSame(extractor, ResourceFileExtractor.getInstance());
    }

    @Test
    public void extract() {
        File testFile = new File("test.file");

        assertFalse(testFile.exists());

        extractor.extract("test.file", testFile.getAbsolutePath());
        assertTrue(testFile.exists());

        assertTrue(testFile.delete());
    }
}