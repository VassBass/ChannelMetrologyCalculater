package service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PropertiesChannelConfigHolder implements ChannelConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesChannelConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/channel.properties";
    private static final String KEY_CHANNELS_CERTIFICATE_FOLDER = "channel.certificate.folder";

    private String channelCertificateFolder = EMPTY;

    public PropertiesChannelConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesChannelConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesChannelConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            channelCertificateFolder = properties.getProperty(KEY_CHANNELS_CERTIFICATE_FOLDER, EMPTY);
        } catch (IOException e) {
            logger.warn("Exception was thrown: ", e);
        }
    }

    @Override
    public String getChannelsCertificatesFolder() {
        return channelCertificateFolder;
    }
}
