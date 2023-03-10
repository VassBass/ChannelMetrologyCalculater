package service.importer.updater.from_v5_4.to_v6_0;

import service.importer.JsonParser;
import service.importer.Model;
import service.importer.ModelHolder;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static service.importer.ModelField.*;
import static service.importer.ModelField.SENSOR_ERROR_FORMULA;

public class JsonParser_v5_4 implements JsonParser {
    private static volatile JsonParser_v5_4 instance;
    private JsonParser_v5_4(){}
    public static JsonParser_v5_4 getInstance() {
        if (instance == null) {
            synchronized (JsonParser_v5_4.class) {
                if (instance == null) {
                    instance = new JsonParser_v5_4();
                }
            }
        }
        return instance;
    }

    private static final String UNNECESSARY_DOUBLE_QUOTES_REGEX = "^\\\"|\\\"$|\\\"(?=\\s\\:)|(?<=\\:\\s)\\\"";
    private static final String CURLY_BRACKETS_REGEX = "^\\{|\\}$";
    private static final String COLON_REGEX = "\\s\\:\\s";

    private ModelHolder parseSensor(String json) {
        ModelHolder sensor = new ModelHolder(Model.SENSOR);
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, "").split("\\,");
        for (String f : fields) {
            String[] vals = f.replaceAll(UNNECESSARY_DOUBLE_QUOTES_REGEX, "").split(COLON_REGEX);
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
                        sensor.setField(SENSOR_RANGE_MIN, "0");
                    }
                    break;
                case "rangeMax":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_RANGE_MAX, vals[1]);
                    } else {
                        sensor.setField(SENSOR_RANGE_MAX, "100");
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

    private ModelHolder parseCalibratorCertificate(String json) {
        ModelHolder certificate = new ModelHolder(Model.CALIBRATOR_CERTIFICATE);
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, "").split("\\,");
        for (String f : fields) {
            String[] vals = f.replaceAll(UNNECESSARY_DOUBLE_QUOTES_REGEX, "").split(COLON_REGEX);
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

    @Nullable
    @Override
    public ModelHolder parse(String json, Model model) {
        switch (model) {
            case SENSOR: return parseSensor(json);
            case CALIBRATOR_CERTIFICATE: return parseCalibratorCertificate(json);
        }
        return null;
    }

    @Override
    public Map<String, String> parse(String json) {
        Map<String, String> result = new HashMap<>();
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, "").split("\\,");
        for (String f : fields) {
            String[] vals = f.replaceAll(UNNECESSARY_DOUBLE_QUOTES_REGEX, "").split(COLON_REGEX);
            result.put(vals[0], vals[1]);
        }
        return result;
    }
}
