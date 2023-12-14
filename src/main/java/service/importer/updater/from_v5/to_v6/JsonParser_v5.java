package service.importer.updater.from_v5.to_v6;

import localization.Labels;
import service.importer.JsonParser;
import service.importer.model.Model;
import service.importer.model.ModelHolder;
import util.RegexHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static service.importer.model.ModelField.*;
import static service.importer.model.ModelField.SENSOR_ERROR_FORMULA;

public class JsonParser_v5 implements JsonParser {
    private static volatile JsonParser_v5 instance;
    private JsonParser_v5(){}
    public static JsonParser_v5 getInstance() {
        if (instance == null) {
            synchronized (JsonParser_v5.class) {
                if (instance == null) {
                    instance = new JsonParser_v5();
                }
            }
        }
        return instance;
    }

    private static final String UNNECESSARY_SYMBOLS_REGEX = "^\\\"|\\\"$|\\\"(?=\\s\\:)|(?<=\\:\\s)\\\"|\\\\(?=\\\")";
    private static final String CURLY_BRACKETS_REGEX = "^\\{|\\}$";
    private static final String NEW_STRING_REGEX = "(\\n\\s\\s)|(\\n)";
    private static final String COLON_REGEX = "\\s\\:\\s";

    /**
     * @param json in format of v5 DB.
     * @param model to parse. Supported models {@link model.dto.Sensor}, {@link model.dto.Calibrator.Certificate}
     * @return ModelHolder of model
     * @see #parseSensor(String)
     * @see #parseCalibratorCertificate(String)
     *
     * @see ModelHolder
     * @see model.dto.Sensor
     * @see model.dto.Calibrator.Certificate
     */
    @Nullable
    @Override
    public ModelHolder parse(@Nonnull String json, @Nonnull Model model) {
        switch (model) {
            case SENSOR: return parseSensor(json);
            case CALIBRATOR_CERTIFICATE: return parseCalibratorCertificate(json);
        }
        return null;
    }

    /**
     * Parses json string of measurement factors to {@code Map<String, String>}
     * where key = output measurement value, value = transform factor (in double)
     * @param json in format of v5 DB.
     * Example of input json:
     * {
     *   "мм вод ст" : 101.9716,
     *   "кгс/см²" : 0.01019716
     * }
     *
     * @return {@code Map<String, String>} key = output measurement value, value = transform factor (in double)
     */
    @Override
    public Map<String, String> parse(@Nonnull String json) {
        Map<String, String> result = new HashMap<>();
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, EMPTY).replaceAll(NEW_STRING_REGEX, EMPTY).split(Labels.COMMA);
        for (String f : fields) {
            String checkStr = f.replaceAll(RegexHelper.SPACE_REGEX, EMPTY);
            if (checkStr.isEmpty()) continue;

            String[] vals = f.replaceAll(UNNECESSARY_SYMBOLS_REGEX, EMPTY).split(COLON_REGEX);
            result.put(vals[0], vals[1]);
        }
        return result;
    }

    /**
     * Parse json string to model of Sensor
     * Example of json:
     * {
     *   "type" : "ТСМ-50М",
     *   "name" : "ТСМ-50М",
     *   "rangeMin" : -50.0,
     *   "rangeMax" : 180.0,
     *   "number" : "",
     *   "value" : "℃",
     *   "measurement" : "Температура",
     *   "errorFormula" : "(0.005 * R) + 0.3"
     * }
     *
     * @param json in format of v5 DB
     * @return model of Sensor
     * @see ModelHolder
     * @see model.dto.Sensor
     */
    private ModelHolder parseSensor(String json) {
        ModelHolder sensor = new ModelHolder(Model.SENSOR);
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, EMPTY).replaceAll(NEW_STRING_REGEX, EMPTY).split(Labels.COMMA);
        for (String f : fields) {
            String[] vals = f.replaceAll(UNNECESSARY_SYMBOLS_REGEX, EMPTY).split(COLON_REGEX);
            switch (vals[0]) {
                case "type":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_TYPE, vals[1]);
                    } else {
                        sensor.setField(SENSOR_TYPE, EMPTY);
                    }
                    break;
                case "name":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_NAME, vals[1]);
                    } else {
                        sensor.setField(SENSOR_NAME, EMPTY);
                    }
                    break;
                case "rangeMin":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_RANGE_MIN, vals[1]);
                    } else {
                        sensor.setField(SENSOR_RANGE_MIN, Labels.ZERRO);
                    }
                    break;
                case "rangeMax":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_RANGE_MAX, vals[1]);
                    } else {
                        sensor.setField(SENSOR_RANGE_MAX, Labels.ONE_HUNDRED);
                    }
                    break;
                case "number":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_SERIAL_NUMBER, vals[1]);
                    } else {
                        sensor.setField(SENSOR_SERIAL_NUMBER, EMPTY);
                    }
                    break;
                case "value":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_MEASUREMENT_VALUE, vals[1]);
                    } else {
                        sensor.setField(SENSOR_MEASUREMENT_VALUE, EMPTY);
                    }
                    break;
                case "measurement":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_MEASUREMENT_NAME, vals[1]);
                    } else {
                        sensor.setField(SENSOR_MEASUREMENT_NAME, EMPTY);
                    }
                    break;
                case "errorFormula":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_ERROR_FORMULA, vals[1]);
                    } else {
                        sensor.setField(SENSOR_ERROR_FORMULA, EMPTY);
                    }
                    break;
            }
        }
        return sensor;
    }

    /**
     * Parse json string to model of CalibratorCertificate
     * Example of json:
     * {
     *   "name" : "№M-140726-1",
     *   "date" : "16.05.2022",
     *   "company" : "ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"",
     *   "type" : "Свідоцтво про перевірку МХ"
     * }
     *
     * @param json in format of v5 DB
     * @return model of CalibratorCertificate
     * @see ModelHolder
     * @see model.dto.Calibrator.Certificate
     */
    private ModelHolder parseCalibratorCertificate(String json) {
        ModelHolder certificate = new ModelHolder(Model.CALIBRATOR_CERTIFICATE);
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, EMPTY).replaceAll(NEW_STRING_REGEX, EMPTY).split(Labels.COMMA);
        for (String f : fields) {
            String[] vals = f.replaceAll(UNNECESSARY_SYMBOLS_REGEX, EMPTY).split(COLON_REGEX);
            switch (vals[0]) {
                case "name":
                    if (vals.length > 1) {
                        certificate.setField(CALIBRATOR_CERTIFICATE_NAME, vals[1]);
                    } else {
                        certificate.setField(CALIBRATOR_CERTIFICATE_NAME, EMPTY);
                    }
                    break;
                case "date":
                    if (vals.length > 1) {
                        certificate.setField(CALIBRATOR_CERTIFICATE_DATE, vals[1]);
                    } else {
                        certificate.setField(CALIBRATOR_CERTIFICATE_DATE, EMPTY);
                    }
                    break;
                case "company":
                    if (vals.length > 1) {
                        certificate.setField(CALIBRATOR_CERTIFICATE_COMPANY, vals[1]);
                    } else {
                        certificate.setField(CALIBRATOR_CERTIFICATE_COMPANY, EMPTY);
                    }
                    break;
                case "type":
                    if (vals.length > 1) {
                        certificate.setField(CALIBRATOR_CERTIFICATE_TYPE, vals[1]);
                    } else {
                        certificate.setField(CALIBRATOR_CERTIFICATE_TYPE, EMPTY);
                    }
                    break;
            }
        }
        return certificate;
    }
}
