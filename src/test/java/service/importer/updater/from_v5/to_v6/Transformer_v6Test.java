package service.importer.updater.from_v5.to_v6;

import model.dto.*;
import model.dto.builder.CalibratorBuilder;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.ControlPointsBuilder;
import model.dto.builder.SensorBuilder;
import org.junit.Before;
import org.junit.Test;
import service.importer.model.Model;
import service.importer.model.ModelField;
import service.importer.model.ModelHolder;

import java.util.Map;

import static org.junit.Assert.*;

public class Transformer_v6Test {

    private Transformer_v6 transformer;

    @Before
    public void init() {
        transformer = Transformer_v6.getInstance();
    }

    @Test
    public void testTransformToUnsupported() {
        assertNull(transformer.transform(new ModelHolder(Model.MEASUREMENT_TRANSFORM_FACTOR), MeasurementTransformFactor.class));
    }

    @Test
    public void testTransformToSensor() {
        ModelHolder input = new ModelHolder(Model.SENSOR);
        input.setField(ModelField.SENSOR_TYPE, "ТСМ-50М");
        input.setField(ModelField.SENSOR_NAME, "ТСМ-50М");
        input.setField(ModelField.SENSOR_RANGE_MIN, "-50.0");
        input.setField(ModelField.SENSOR_RANGE_MAX, "180.0");
        input.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        input.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "℃");
        input.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Температура");
        input.setField(ModelField.SENSOR_ERROR_FORMULA, "(0.005 * R) + 0.3");

        Sensor expected = new SensorBuilder()
                .setType("ТСМ-50М")
                .setRangeMin(-50.0)
                .setRangeMax(180.0)
                .setMeasurementName("Температура")
                .setMeasurementValue("℃")
                .setErrorFormula("(0.005 * R) + 0.3")
                .build();

        Sensor actual = transformer.transform(input, Sensor.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getChannelCode(), actual.getChannelCode());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getRangeMin(), actual.getRangeMin(), 0.0);
        assertEquals(expected.getRangeMax(), actual.getRangeMax(), 0.0);
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getMeasurementName(), actual.getMeasurementName());
        assertEquals(expected.getMeasurementValue(), actual.getMeasurementValue());
        assertEquals(expected.getErrorFormula(), actual.getErrorFormula());
    }

    @Test
    public void testTransformToChannel() {
        ModelHolder input = new ModelHolder(Model.CHANNEL);
        input.setField(ModelField.CHANNEL_CODE, "813.000540");
        input.setField(ModelField.CHANNEL_NAME, "Температура підшипників розвантаження млину №32");
        input.setField(ModelField.CHANNEL_DEPARTMENT, "ДЗФ");
        input.setField(ModelField.CHANNEL_AREA, "ОВДЗ-2");
        input.setField(ModelField.CHANNEL_PROCESS, "Секція №3");
        input.setField(ModelField.CHANNEL_INSTALLATION, "Млин №32");
        input.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "32ТЕ17");
        input.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "570");
        input.setField(ModelField.CHANNEL_REFERENCE, "");
        input.setField(ModelField.CHANNEL_DATE, "14.12.2021");
        input.setField(ModelField.CHANNEL_SUITABILITY, "true");
        input.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "℃");
        input.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"ТСМ-50М\",\n" +
                "  \"name\" : \"ТСМ-50М\",\n" +
                "  \"rangeMin\" : -50.0,\n" +
                "  \"rangeMax\" : 180.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"℃\",\n" +
                "  \"measurement\" : \"Температура\",\n" +
                "  \"errorFormula\" : \"(0.005 * R) + 0.3\"\n" +
                "}");
        input.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        input.setField(ModelField.CHANNEL_RANGE_MIN, "0.0");
        input.setField(ModelField.CHANNEL_RANGE_MAX, "100.0");
        input.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "1.5");
        input.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "1.5");

        Channel expected = new ChannelBuilder()
                .setCode("813.000540")
                .setName("Температура підшипників розвантаження млину №32")
                .setMeasurementName("")
                .setMeasurementValue("℃")
                .setDepartment("ДЗФ").setArea("ОВДЗ-2").setProcess("Секція №3").setInstallation("Млин №32")
                .setDate("14.12.2021")
                .setTechnologyNumber("32ТЕ17")
                .setNumberOfProtocol("570")
                .setFrequency(2.0)
                .setRangeMin(0.0)
                .setRangeMax(100.0)
                .setReference("")
                .setSuitability(true)
                .setAllowableErrorInPercent(1.5)
                .setAllowableErrorInValue(1.5)
                .build();

        Channel actual = transformer.transform(input, Channel.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getMeasurementName(), actual.getMeasurementName());
        assertEquals(expected.getMeasurementValue(), actual.getMeasurementValue());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getProcess(), actual.getProcess());
        assertEquals(expected.getArea(), actual.getArea());
        assertEquals(expected.getInstallation(), actual.getInstallation());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTechnologyNumber(), actual.getTechnologyNumber());
        assertEquals(expected.getNumberOfProtocol(), actual.getNumberOfProtocol());
        assertEquals(expected.getReference(), actual.getReference());
        assertEquals(expected.isSuitability(), actual.isSuitability());
        assertEquals(expected.getRangeMin(), actual.getRangeMin(), 0.0);
        assertEquals(expected.getRangeMax(), actual.getRangeMax(), 0.0);
        assertEquals(expected.getFrequency(), actual.getFrequency(), 0.0);
        assertEquals(expected.getAllowableErrorPercent(), actual.getAllowableErrorPercent(), 0.0);
        assertEquals(expected.getAllowableErrorValue(), actual.getAllowableErrorValue(), 0.0);
    }

    @Test
    public void testTransformToCalibratorCertificate() {
        ModelHolder input = new ModelHolder(Model.CALIBRATOR_CERTIFICATE);
        input.setField(ModelField.CALIBRATOR_CERTIFICATE_NAME, "№M-140726-1");
        input.setField(ModelField.CALIBRATOR_CERTIFICATE_DATE, "16.05.2022");
        input.setField(ModelField.CALIBRATOR_CERTIFICATE_COMPANY, "ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"");
        input.setField(ModelField.CALIBRATOR_CERTIFICATE_TYPE, "Свідоцтво про перевірку МХ");

        Calibrator.Certificate expected = new Calibrator.Certificate();
        expected.setName("№M-140726-1");
        expected.setDate("16.05.2022");
        expected.setCompany("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"");
        expected.setType("Свідоцтво про перевірку МХ");

        Calibrator.Certificate actual = transformer.transform(input, Calibrator.Certificate.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getCompany(), actual.getCompany());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    public void testTransformCalibrator() {
        ModelHolder input = new ModelHolder(Model.CALIBRATOR);
        input.setField(ModelField.CALIBRATOR_NAME, "Prova-123 t < 0℃");
        input.setField(ModelField.CALIBRATOR_TYPE, "Prova-123");
        input.setField(ModelField.CALIBRATOR_NUMBER, "13180302");
        input.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Температура");
        input.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "℃");
        input.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "1.1");
        input.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№06/2315К\",\n"
                + "  \"date\" : \"21.07.2020\",\n"
                + "  \"company\" : \"ДП\\\"ХарківСтандартМетрологія\\\"\",\n"
                + "  \"type\" : \"Сертифікат калібрування\"\n"
                + "}");
        input.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
        input.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

        Calibrator expected = new CalibratorBuilder()
                .setType("Prova-123")
                .setName("Prova-123 t < 0℃")
                .setNumber("13180302")
                .setCertificate("Сертифікат калібрування", "№06/2315К", "21.07.2020", "ДП\"ХарківСтандартМетрологія\"")
                .setRangeMin(0.0)
                .setRangeMax(0.0)
                .setMeasurementName("Температура")
                .setMeasurementValue("℃")
                .setErrorFormula("1.1")
                .build();

        Calibrator actual = transformer.transform(input, Calibrator.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getCertificate(), actual.getCertificate());
        assertEquals(expected.getRangeMin(), actual.getRangeMin(), 0.0);
        assertEquals(expected.getRangeMax(), actual.getRangeMax(), 0.0);
        assertEquals(expected.getMeasurementName(), actual.getMeasurementName());
        assertEquals(expected.getMeasurementValue(), actual.getMeasurementValue());
        assertEquals(expected.getErrorFormula(), actual.getErrorFormula());
    }

    @Test
    public void testTransformControlPoints() {
        ModelHolder input = new ModelHolder(Model.CONTROL_POINTS);
        input.setField(ModelField.CONTROL_POINTS_SENSOR_TYPE, "ТСМ-50М");
        input.setField(ModelField.CONTROL_POINTS_VALUES, "0.0|5.14|50.0|94.86|100.0|");
        input.setField(ModelField.CONTROL_POINTS_RANGE_MIN, "0.0");
        input.setField(ModelField.CONTROL_POINTS_RANGE_MAX, "100.0");

        String name = ControlPoints.createName("ТСМ-50М", 0.0, 100.0);
        ControlPoints expected = new ControlPointsBuilder(name)
                .setSensorType("ТСМ-50М")
                .addControlPoint(5D, 5.14)
                .addControlPoint(50D, 50.0)
                .addControlPoint(95D, 94.86)
                .build();

        ControlPoints actual = transformer.transform(input, ControlPoints.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSensorType(), actual.getSensorType());

        Map<Double, Double> actualPoints = actual.getValues();
        for (Map.Entry<Double, Double> entry : expected.getValues().entrySet()) {
            assertTrue(actualPoints.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), actualPoints.get(entry.getKey()));
        }
    }

    @Test
    public void testTransformPerson() {
        ModelHolder input = new ModelHolder(Model.PERSON);
        input.setField(ModelField.PERSON_NAME, "Сидор");
        input.setField(ModelField.PERSON_SURNAME, "Сидоров");
        input.setField(ModelField.PERSON_PATRONYMIC, "Сидорович");
        input.setField(ModelField.PERSON_POSITION, "Начальник дільниці");

        Person expected = new Person();
        expected.setName("Сидор");
        expected.setSurname("Сидоров");
        expected.setPatronymic("Сидорович");
        expected.setPosition("Начальник дільниці");

        Person actual = transformer.transform(input, Person.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getPatronymic(), actual.getPatronymic());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}