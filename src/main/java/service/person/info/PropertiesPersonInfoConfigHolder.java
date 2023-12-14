package service.person.info;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesPersonInfoConfigHolder implements PersonInfoConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesPersonInfoConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/person.properties";
    private static final String KEY_DIALOG_WIDTH = "person.info.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "person.info.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesPersonInfoConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesPersonInfoConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesPersonInfoConfigHolder.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            String dialogWidth = properties.getProperty(KEY_DIALOG_WIDTH);
            if (StringHelper.isInt(dialogWidth)) this.dialogWidth = Integer.parseInt(dialogWidth);

            String dialogHeight = properties.getProperty(KEY_DIALOG_HEIGHT);
            if (StringHelper.isInt(dialogHeight)) this.dialogHeight = Integer.parseInt(dialogHeight);

        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
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
