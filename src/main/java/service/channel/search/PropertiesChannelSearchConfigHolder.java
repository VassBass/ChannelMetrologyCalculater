package service.channel.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.info.PropertiesChannelInfoConfigHolder;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesChannelSearchConfigHolder implements ChannelSearchConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesChannelSearchConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/channel.properties";
    private static final String KEY_DIALOG_WIDTH = "channel.search.dialog.width";
    private static final String KEY_DIALOG_HEIGHT = "channel.search.dialog.height";

    private int dialogWidth = 475;
    private int dialogHeight = 730;

    public PropertiesChannelSearchConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesChannelSearchConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesChannelInfoConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            Integer dialogWidth = StringHelper.parseInt(properties.getProperty(KEY_DIALOG_WIDTH, null));
            if (dialogWidth != null) this.dialogWidth = dialogWidth;

            Integer dialogHeight = StringHelper.parseInt(properties.getProperty(KEY_DIALOG_HEIGHT, null));
            if (dialogHeight != null) this.dialogHeight = dialogHeight;

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
