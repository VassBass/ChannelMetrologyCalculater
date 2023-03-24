package service.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.PropertiesChannelListConfigHolder;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCalculationConfigHolder implements CalculationConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesCalculationConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/calculation.properties";

    private static final String KEY_CONTROL_CONDITION_DIALOG_WIDTH = "calculation.collect.condition.dialog.width";
    private static final String KEY_CONTROL_CONDITION_DIALOG_HEIGHT = "calculation.collect.condition.dialog.height";

    private int controlConditionDialogWidth = 685;
    private int controlConditionDialogHeight = 295;

    public PropertiesCalculationConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesCalculationConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesChannelListConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            Integer controlConditionDialogWidth = StringHelper.parseInt(properties.getProperty(KEY_CONTROL_CONDITION_DIALOG_WIDTH, null));
            if (controlConditionDialogWidth != null) this.controlConditionDialogWidth = controlConditionDialogWidth;

            Integer controlConditionDialogHeight = StringHelper.parseInt(properties.getProperty(KEY_CONTROL_CONDITION_DIALOG_HEIGHT, null));
            if (controlConditionDialogHeight != null) this.controlConditionDialogHeight = controlConditionDialogHeight;

        } catch (IOException e) {
            logger.warn("Exception was thrown: ", e);
        }
    }

    @Override
    public int getControlConditionDialogWidth() {
        return controlConditionDialogWidth;
    }

    @Override
    public int getControlConditionDialogHeight() {
        return controlConditionDialogHeight;
    }
}
