package service.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PropertiesCalculationConfigHolder implements CalculationConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesCalculationConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/calculation.properties";

    private static final String KEY_CONTROL_CONDITION_DIALOG_WIDTH = "calculation.condition.dialog.width";
    private static final String KEY_CONTROL_CONDITION_DIALOG_HEIGHT = "calculation.condition.dialog.height";
    private static final String KEY_INPUT_DIALOG_WIDTH = "calculation.input.dialog.width";
    private static final String KEY_INPUT_DIALOG_HEIGHT = "calculation.input.dialog.height";
    private static final String KEY_RESULT_DIALOG_WIDTH = "calculation.result.dialog.width";
    private static final String KEY_RESULT_DIALOG_HEIGHT = "calculation.result.dialog.height";
    private static final String KEY_PERSONS_DIALOG_WIDTH = "calculation.persons.dialog.width";
    private static final String KEY_PERSONS_DIALOG_HEIGHT = "calculation.persons.dialog.height";
    private static final String KEY_PROTOCOL_FOLDER = "calculation.protocol.folder";

    private int controlConditionDialogWidth = 685;
    private int controlConditionDialogHeight = 295;
    private int inputDialogWidth = 780;
    private int inputDialogHeight = 360;
    private int resultDialogWidth = 450;
    private int resultDialogHeight = 350;
    private int personsDialogWidth = 500;
    private int personsDialogHeight = 300;
    private String protocolFolder;

    public PropertiesCalculationConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public PropertiesCalculationConfigHolder(String propertiesFile) {
        try (InputStream in = PropertiesCalculationConfigHolder.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);

            Integer controlConditionDialogWidth = StringHelper.parseInt(properties.getProperty(KEY_CONTROL_CONDITION_DIALOG_WIDTH, null));
            if (Objects.nonNull(controlConditionDialogWidth)) this.controlConditionDialogWidth = controlConditionDialogWidth;

            Integer controlConditionDialogHeight = StringHelper.parseInt(properties.getProperty(KEY_CONTROL_CONDITION_DIALOG_HEIGHT, null));
            if (Objects.nonNull(controlConditionDialogHeight)) this.controlConditionDialogHeight = controlConditionDialogHeight;

            Integer inputDialogWidth = StringHelper.parseInt(properties.getProperty(KEY_INPUT_DIALOG_WIDTH, null));
            if (Objects.nonNull(inputDialogWidth)) this.inputDialogWidth = inputDialogWidth;

            Integer inputDialogHeight = StringHelper.parseInt(properties.getProperty(KEY_INPUT_DIALOG_HEIGHT, null));
            if (Objects.nonNull(inputDialogHeight)) this.inputDialogHeight = inputDialogHeight;

            Integer resultDialogWidth = StringHelper.parseInt(properties.getProperty(KEY_RESULT_DIALOG_WIDTH, null));
            if (Objects.nonNull(resultDialogWidth)) this.resultDialogWidth = resultDialogWidth;

            Integer resultDialogHeight = StringHelper.parseInt(properties.getProperty(KEY_RESULT_DIALOG_HEIGHT, null));
            if (Objects.nonNull(resultDialogHeight)) this.resultDialogHeight = resultDialogHeight;

            Integer personsDialogWidth = StringHelper.parseInt(properties.getProperty(KEY_PERSONS_DIALOG_WIDTH, null));
            if (Objects.nonNull(personsDialogWidth)) this.personsDialogWidth = personsDialogWidth;

            Integer personsDialogHeight = StringHelper.parseInt(properties.getProperty(KEY_PERSONS_DIALOG_HEIGHT, null));
            if (Objects.nonNull(personsDialogHeight)) this.personsDialogHeight = personsDialogHeight;

            protocolFolder = properties.getProperty(KEY_PROTOCOL_FOLDER, EMPTY);

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

    @Override
    public int getInputDialogWidth() {
        return inputDialogWidth;
    }

    @Override
    public int getInputDialogHeight() {
        return inputDialogHeight;
    }

    @Override
    public int getResultDialogWidth() {
        return resultDialogWidth;
    }

    @Override
    public int getResultDialogHeight() {
        return resultDialogHeight;
    }

    @Override
    public int getPersonsDialogWidth() {
        return personsDialogWidth;
    }

    @Override
    public int getPersonsDialogHeight() {
        return personsDialogHeight;
    }

    @Override
    public String getProtocolsFolder() {
        return protocolFolder;
    }
}
