package service.sensor_error.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesSensorErrorListConfigHolder implements SensorErrorListConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesSensorErrorListConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/sensor_error.properties";
    private static final String KEY_DIALOG_WIDTH = "sensor_error.list.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "sensor_error.list.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesSensorErrorListConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesSensorErrorListConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesSensorErrorListConfigHolder.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            String dialogWidth = properties.getProperty(KEY_DIALOG_WIDTH);
            if (StringHelper.isInt(dialogWidth)) this.dialogWidth = Integer.parseInt(dialogWidth);

            String dialogHeight = properties.getProperty(KEY_DIALOG_HEIGHT);
            if (StringHelper.isInt(dialogHeight)) this.dialogHeight = Integer.parseInt(dialogHeight);

        } catch (IOException e) {
            logger.warn("Exception was thrown: ", e);
        }
    }

    @Override
    public int getDialogWidth() {
        return dialogWidth;
    }

    @Override
    public int getDialogHeight() {
        return dialogHeight;
    }
}
