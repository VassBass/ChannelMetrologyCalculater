package service.channel.info;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesChannelInfoConfigHolder implements ChannelInfoConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesChannelInfoConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/channel.properties";
    private static final String KEY_DIALOG_WIDTH = "channel.info.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "channel.info.dialog.height";

    private int dialogWidth = 475;
    private int dialogHeight = 730;

    public PropertiesChannelInfoConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesChannelInfoConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesChannelInfoConfigHolder.class.getClassLoader()
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
