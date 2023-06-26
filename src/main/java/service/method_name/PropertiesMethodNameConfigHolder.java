package service.method_name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesMethodNameConfigHolder implements MethodNameConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesMethodNameConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/method_name.properties";
    private static final String KEY_DIALOG_WIDTH = "method_name.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "method_name.dialog.height";

    private int dialogWidth = 100;
    private int dialogHeight = 100;

    public PropertiesMethodNameConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesMethodNameConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesMethodNameConfigHolder.class.getClassLoader().getResourceAsStream(propertiesFile)) {
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
