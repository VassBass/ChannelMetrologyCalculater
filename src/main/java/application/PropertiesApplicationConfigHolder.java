package application;

import localization.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PropertiesApplicationConfigHolder implements ApplicationConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesApplicationConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/application.properties";
    private static final String KEY_APPLICATION_VERSION = "application.version";
    private static final String KEY_FOLDER_IMAGES = "folder.images";

    private Integer screenWidth;
    private Integer screenHeight;
    private String applicationVersion = EMPTY;
    private String folderImages = EMPTY;

    public PropertiesApplicationConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesApplicationConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesApplicationConfigHolder.class.getClassLoader()
                    .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
            applicationVersion = properties.getProperty(KEY_APPLICATION_VERSION, EMPTY);
            folderImages = properties.getProperty(KEY_FOLDER_IMAGES, EMPTY);
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }

    @Override
    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public String getApplicationVersion() {
        return applicationVersion;
    }

    @Override
    public String getImagesFolderPath() {
        return folderImages;
    }
}
