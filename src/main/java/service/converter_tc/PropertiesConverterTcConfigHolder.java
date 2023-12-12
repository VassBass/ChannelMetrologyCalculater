package service.converter_tc;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConverterTcConfigHolder implements ConverterTcConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesConverterTcConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/converter_tc.properties";
    private static final String KEY_DIALOG_WIDTH = "converter_tc.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "converter_tc.dialog.height";

    private int dialogWidth = 500;
    private int dialogHeight = 250;

    public PropertiesConverterTcConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesConverterTcConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesConverterTcConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            Integer dialogWidth = StringHelper.parseInt(properties.getProperty(KEY_DIALOG_WIDTH, null));
            if (dialogWidth != null) this.dialogWidth = dialogWidth;

            Integer dialogHeight = StringHelper.parseInt(properties.getProperty(KEY_DIALOG_HEIGHT, null));
            if (dialogHeight != null) this.dialogHeight = dialogHeight;

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
