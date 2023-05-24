package service.measurement.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.PropertiesChannelListConfigHolder;
import service.measurement.list.PropertiesMeasurementListConfigHolder;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesMeasurementInfoConfigHolder implements MeasurementInfoConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesMeasurementInfoConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/measurement.properties";
    private static final String KEY_DIALOG_WIDTH = "measurement.info.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "measurement.info.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesMeasurementInfoConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesMeasurementInfoConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesChannelListConfigHolder.class.getClassLoader().getResourceAsStream(propertiesFile)) {
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
