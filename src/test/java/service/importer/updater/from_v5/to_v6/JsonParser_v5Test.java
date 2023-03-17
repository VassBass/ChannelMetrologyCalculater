package service.importer.updater.from_v5.to_v6;

import org.junit.Before;
import org.junit.Test;
import service.importer.model.Model;
import service.importer.model.ModelField;
import service.importer.model.ModelHolder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonParser_v5Test {

    private JsonParser_v5 jsonParser;

    @Before
    public void init() {
        jsonParser = JsonParser_v5.getInstance();
    }

    @Test
    public void testParseSensor() {
        String json = "{\n" +
                "  \"type\" : \"ТСМ-50М\",\n" +
                "  \"name\" : \"ТСМ-50М\",\n" +
                "  \"rangeMin\" : -50.0,\n" +
                "  \"rangeMax\" : 180.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"℃\",\n" +
                "  \"measurement\" : \"Температура\",\n" +
                "  \"errorFormula\" : \"(0.005 * R) + 0.3\"\n" +
                "}";
        ModelHolder expected = new ModelHolder(Model.SENSOR);
        expected.setField(ModelField.SENSOR_TYPE, "ТСМ-50М");
        expected.setField(ModelField.SENSOR_NAME, "ТСМ-50М");
        expected.setField(ModelField.SENSOR_RANGE_MIN, "-50.0");
        expected.setField(ModelField.SENSOR_RANGE_MAX, "180.0");
        expected.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        expected.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "℃");
        expected.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Температура");
        expected.setField(ModelField.SENSOR_ERROR_FORMULA, "(0.005 * R) + 0.3");

        assertEquals(expected, jsonParser.parse(json, Model.SENSOR));
    }

    @Test
    public void testParseCalibratorCertificate() {
        String json = "{\n" +
                "  \"name\" : \"№M-140726-1\",\n" +
                "  \"date\" : \"16.05.2022\",\n" +
                "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n" +
                "  \"type\" : \"Свідоцтво про перевірку МХ\"\n" +
                "}";
        ModelHolder expected = new ModelHolder(Model.CALIBRATOR_CERTIFICATE);
        expected.setField(ModelField.CALIBRATOR_CERTIFICATE_NAME, "№M-140726-1");
        expected.setField(ModelField.CALIBRATOR_CERTIFICATE_DATE, "16.05.2022");
        expected.setField(ModelField.CALIBRATOR_CERTIFICATE_COMPANY, "ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"");
        expected.setField(ModelField.CALIBRATOR_CERTIFICATE_TYPE, "Свідоцтво про перевірку МХ");

        assertEquals(expected, jsonParser.parse(json, Model.CALIBRATOR_CERTIFICATE));
    }

    @Test
    public void testParseUnsupportedModel() {
        String json = "{\n" +
                "  \"name\" : \"Unsupported name\",\n" +
                "  \"value\" : \"Unsupported value\",\n" +
                "  \"tralala\" : \"Unsupported tralala\",\n" +
                "  \"type\" : \"Свідоцтво про перевірку МХ\"\n" +
                "}";
        assertNull(jsonParser.parse(json, Model.CHANNEL));
    }

    @Test
    public void testParseMeasurementFactors() {
        String json = "{\n" +
                "  \"мм вод ст\" : 101.9716,\n" +
                "  \"кгс/см²\" : 0.01019716\n" +
                "}";
        Map<String, String> expected = new HashMap<>();
        expected.put("мм вод ст", "101.9716");
        expected.put("кгс/см²", "0.01019716");

        assertEquals(expected, jsonParser.parse(json));
    }
}