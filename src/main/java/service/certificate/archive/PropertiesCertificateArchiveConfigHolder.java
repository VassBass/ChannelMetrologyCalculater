package service.certificate.archive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PropertiesCertificateArchiveConfigHolder implements CertificateArchiveConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesCertificateArchiveConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/calculation.properties";

    private static final String KEY_CERTIFICATE_FOLDER = "calculation.protocol.folder";

    private String certificateFolder;

    public PropertiesCertificateArchiveConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesCertificateArchiveConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesCertificateArchiveConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            certificateFolder = properties.getProperty(KEY_CERTIFICATE_FOLDER, EMPTY);

        } catch (IOException e) {
            logger.warn("Exception was thrown: ", e);
        }
    }

    @Override
    public String getCertificatesFolderName() {
        return certificateFolder;
    }
}
