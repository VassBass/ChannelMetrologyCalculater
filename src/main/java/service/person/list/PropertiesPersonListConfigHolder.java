package service.person.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.PropertiesChannelListConfigHolder;
import service.sensor_error.list.PropertiesSensorErrorListConfigHolder;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesPersonListConfigHolder implements PersonListConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesPersonListConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/person.properties";
    private static final String KEY_DIALOG_WIDTH = "person.list.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "person.list.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesPersonListConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesPersonListConfigHolder(String propertiesFile) {
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
