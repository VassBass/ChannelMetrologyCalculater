package service.sensor_types.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesSensorTypesInfoConfigHolder implements SensorTypesInfoConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesSensorTypesInfoConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/sensor_types.properties";
    private static final String KEY_DIALOG_WIDTH = "sensor_types.info.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "sensor_types.info.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesSensorTypesInfoConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesSensorTypesInfoConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesSensorTypesInfoConfigHolder.class.getClassLoader().getResourceAsStream(propertiesFile)) {
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
