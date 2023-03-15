package service.importer.updater.from_v5.to_v6;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import service.importer.model.Model;
import service.importer.model.ModelField;
import service.importer.model.ModelHolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class SqliteReaderOfv5Test {
    private static final String TEMPLATE_DB = "testDB_v5.db";
    private static final String TEST_DB = "TestData.db";
    private static final String DB_URL = "jdbc:sqlite:" + TEST_DB;

    private SqliteReaderOfv5 readerOfv5;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() throws IOException {
        readerOfv5 = new SqliteReaderOfv5();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEMPLATE_DB)) {
            assertNotNull(in);

            File dest = new File(TEST_DB);
            dest.mkdirs();
            dest.createNewFile();
            Files.copy(in, Paths.get(TEST_DB), REPLACE_EXISTING);
        }
    }

    @AfterClass
    public static void dropTables() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement()) {
            String sql = "SELECT name FROM sqlite_master WHERE type = 'table'";
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> tableNames = new ArrayList<>();
            while (resultSet.next()) tableNames.add(resultSet.getString("name"));
            for (String table : tableNames) {
                String dropTableSql = "DROP TABLE IF EXISTS " + table;
                statement.executeUpdate(dropTableSql);
            }
        }
    }

    @Test
    public void testRead() {
        List<ModelHolder> read = readerOfv5.read(new File(TEST_DB));
        assertFalse(read.isEmpty());

        checkDepartments(read);
        checkAreas(read);
        checkProcesses(read);
        checkInstallations(read);
        checkCalibrators(read);
        checkChannels(read);
        checkControlPoints(read);
        checkMeasurements(read);
        checkPersons(read);
        checkSensors(read);
    }

    private void checkDepartments(List<ModelHolder> read) {
        List<String> expected = Arrays.asList("ЦВО", "ДЗФ");
        List<String> actual = read.stream()
                .filter(m -> m.getModel() == Model.DEPARTMENT)
                .map(m -> m.getValue(ModelField.DEPARTMENT))
                .collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private void checkAreas(List<ModelHolder> read) {
        List<String> expected = Arrays.asList("ОВДЗ-2", "ОВДЗ-4", "ЦВО-1", "ЦВО-2");
        List<String> actual = read.stream()
                .filter(m -> m.getModel() == Model.AREA)
                .map(m -> m.getValue(ModelField.AREA))
                .collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private void checkProcesses(List<ModelHolder> read) {
        List<String> expected = Arrays.asList("Бармак", "Секція", "Тракт", "Технологічна лінія");
        List<String> actual = read.stream()
                .filter(m -> m.getModel() == Model.PROCESS)
                .map(m -> m.getValue(ModelField.PROCESS))
                .collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private void checkInstallations(List<ModelHolder> read) {
        List<String> expected = Arrays.asList("Млин", "Гідроциклон 710мм", "Вентилятор", "Охолоджувач");
        List<String> actual = read.stream()
                .filter(m -> m.getModel() == Model.INSTALLATION)
                .map(m -> m.getValue(ModelField.INSTALLATION))
                .collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedCalibrators() {
        ModelHolder fluke725 = new ModelHolder(Model.CALIBRATOR);
        fluke725.setField(ModelField.CALIBRATOR_NAME, "Fluke 725");
        fluke725.setField(ModelField.CALIBRATOR_TYPE, "Fluke 725");
        fluke725.setField(ModelField.CALIBRATOR_NUMBER, "1988293");
        fluke725.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Температура");
        fluke725.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "℃");
        fluke725.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "0.7");
        fluke725.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№M-140726-1\",\n"
                + "  \"date\" : \"16.05.2022\",\n"
                + "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n"
                + "  \"type\" : \"Свідоцтво про перевірку МХ\"\n"
                + "}");
        fluke725.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
        fluke725.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

        ModelHolder prova123 = new ModelHolder(Model.CALIBRATOR);
        prova123.setField(ModelField.CALIBRATOR_NAME, "Prova-123 t < 0℃");
        prova123.setField(ModelField.CALIBRATOR_TYPE, "Prova-123");
        prova123.setField(ModelField.CALIBRATOR_NUMBER, "13180302");
        prova123.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Температура");
        prova123.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "℃");
        prova123.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "1.1");
        prova123.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№06/2315К\",\n"
                + "  \"date\" : \"21.07.2020\",\n"
                + "  \"company\" : \"ДП\\\"ХарківСтандартМетрологія\\\"\",\n"
                + "  \"type\" : \"Сертифікат калібрування\"\n"
                + "}");
        prova123.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
        prova123.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

        ModelHolder fluke718 = new ModelHolder(Model.CALIBRATOR);
        fluke718.setField(ModelField.CALIBRATOR_NAME, "Fluke 718 30G");
        fluke718.setField(ModelField.CALIBRATOR_TYPE, "Fluke 718 30G");
        fluke718.setField(ModelField.CALIBRATOR_NUMBER, "2427047");
        fluke718.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Тиск");
        fluke718.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "кПа");
        fluke718.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(convR / 100) * 0.05");
        fluke718.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№M-140726-2\",\n"
                + "  \"date\" : \"16.05.2022\",\n"
                + "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n"
                + "  \"type\" : \"Свідоцтво про перевірку МХ\"\n"
                + "}");
        fluke718.setField(ModelField.CALIBRATOR_RANGE_MIN, "-83.0");
        fluke718.setField(ModelField.CALIBRATOR_RANGE_MAX, "207.0");

        ModelHolder fluke750PD2 = new ModelHolder(Model.CALIBRATOR);
        fluke750PD2.setField(ModelField.CALIBRATOR_NAME, "Fluke 750PD2");
        fluke750PD2.setField(ModelField.CALIBRATOR_TYPE, "Fluke 750PD2");
        fluke750PD2.setField(ModelField.CALIBRATOR_NUMBER, "4043273");
        fluke750PD2.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Тиск");
        fluke750PD2.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "кПа");
        fluke750PD2.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(convR / 100) * 0.05");
        fluke750PD2.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№M-140726-2\",\n"
                + "  \"date\" : \"16.05.2022\",\n"
                + "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n"
                + "  \"type\" : \"Свідоцтво про перевірку МХ\"\n"
                + "}");
        fluke750PD2.setField(ModelField.CALIBRATOR_RANGE_MIN, "-7.0");
        fluke750PD2.setField(ModelField.CALIBRATOR_RANGE_MAX, "7.0");

        ModelHolder YAKOGAWA_AM012 = new ModelHolder(Model.CALIBRATOR);
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_NAME, "YAKOGAWA AM012");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_TYPE, "AM012");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_NUMBER, "S5T800358");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Витрата");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(R / 100) * 0.06");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n"
                + "  \"name\" : \"№UA/24/200717/265\",\n"
                + "  \"date\" : \"17.07.2020\",\n"
                + "  \"company\" : \"ДП\\\"Укрметртестстандарт\\\"\",\n"
                + "  \"type\" : \"Сертифікат калібрування\"\n"
                + "}");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
        YAKOGAWA_AM012.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

        return Arrays.asList(fluke725, prova123, fluke718, fluke750PD2, YAKOGAWA_AM012);
    }

    private void checkCalibrators(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedCalibrators();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.CALIBRATOR).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedChannels() {
        ModelHolder channel1 = new ModelHolder(Model.CHANNEL);
        channel1.setField(ModelField.CHANNEL_CODE, "813.000540");
        channel1.setField(ModelField.CHANNEL_NAME, "Температура підшипників розвантаження млину №32");
        channel1.setField(ModelField.CHANNEL_DEPARTMENT, "ДЗФ");
        channel1.setField(ModelField.CHANNEL_AREA, "ОВДЗ-2");
        channel1.setField(ModelField.CHANNEL_PROCESS, "Секція №3");
        channel1.setField(ModelField.CHANNEL_INSTALLATION, "Млин №32");
        channel1.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "32ТЕ17");
        channel1.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "570");
        channel1.setField(ModelField.CHANNEL_REFERENCE, "");
        channel1.setField(ModelField.CHANNEL_DATE, "14.12.2021");
        channel1.setField(ModelField.CHANNEL_SUITABILITY, "true");
        channel1.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "℃");
        channel1.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"ТСМ-50М\",\n" +
                "  \"name\" : \"ТСМ-50М\",\n" +
                "  \"rangeMin\" : -50.0,\n" +
                "  \"rangeMax\" : 180.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"℃\",\n" +
                "  \"measurement\" : \"Температура\",\n" +
                "  \"errorFormula\" : \"(0.005 * R) + 0.3\"\n" +
                "}");
        channel1.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        channel1.setField(ModelField.CHANNEL_RANGE_MIN, "0.0");
        channel1.setField(ModelField.CHANNEL_RANGE_MAX, "100.0");
        channel1.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "1.5");
        channel1.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "1.5");

        ModelHolder channel2 = new ModelHolder(Model.CHANNEL);
        channel2.setField(ModelField.CHANNEL_CODE, "813.000211");
        channel2.setField(ModelField.CHANNEL_NAME, "Регулювання тиску в зоні набору кека фільтра 13510");
        channel2.setField(ModelField.CHANNEL_DEPARTMENT, "ЦВО");
        channel2.setField(ModelField.CHANNEL_AREA, "ЦВО-1");
        channel2.setField(ModelField.CHANNEL_PROCESS, "Технологічна лінія №2");
        channel2.setField(ModelField.CHANNEL_INSTALLATION, "");
        channel2.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "Р-013-4");
        channel2.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "562");
        channel2.setField(ModelField.CHANNEL_REFERENCE, "");
        channel2.setField(ModelField.CHANNEL_DATE, "03.12.2021");
        channel2.setField(ModelField.CHANNEL_SUITABILITY, "true");
        channel2.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "кгс/см²");
        channel2.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"CERABAR M\",\n" +
                "  \"name\" : \"CERABAR M\",\n" +
                "  \"rangeMin\" : -1.0,\n" +
                "  \"rangeMax\" : 0.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"кгс/см²\",\n" +
                "  \"measurement\" : \"Тиск\",\n" +
                "  \"errorFormula\" : \"(convR/100) * 0.15\"\n" +
                "}");
        channel2.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        channel2.setField(ModelField.CHANNEL_RANGE_MIN, "-1.0");
        channel2.setField(ModelField.CHANNEL_RANGE_MAX, "0.0");
        channel2.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "1.5");
        channel2.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "0.015");

        ModelHolder channel3 = new ModelHolder(Model.CHANNEL);
        channel3.setField(ModelField.CHANNEL_CODE, "813.000226");
        channel3.setField(ModelField.CHANNEL_NAME, "Температура у трубі охолоджувача");
        channel3.setField(ModelField.CHANNEL_DEPARTMENT, "ЦВО");
        channel3.setField(ModelField.CHANNEL_AREA, "ЦВО-1");
        channel3.setField(ModelField.CHANNEL_PROCESS, "Технологічна лінія №2");
        channel3.setField(ModelField.CHANNEL_INSTALLATION, "Охолоджувач");
        channel3.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "Т-201-8");
        channel3.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "545");
        channel3.setField(ModelField.CHANNEL_REFERENCE, "");
        channel3.setField(ModelField.CHANNEL_DATE, "03.12.2021");
        channel3.setField(ModelField.CHANNEL_SUITABILITY, "true");
        channel3.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "℃");
        channel3.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"Термопара TXA-2388 (тип К)\",\n" +
                "  \"name\" : \"Термопара TXA-2388 (тип К) > 333.5℃\",\n" +
                "  \"rangeMin\" : -50.0,\n" +
                "  \"rangeMax\" : 1250.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"℃\",\n" +
                "  \"measurement\" : \"Температура\",\n" +
                "  \"errorFormula\" : \"0.0075 * R\"\n" +
                "}");
        channel3.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        channel3.setField(ModelField.CHANNEL_RANGE_MIN, "0.0");
        channel3.setField(ModelField.CHANNEL_RANGE_MAX, "1250.0");
        channel3.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "2.0");
        channel3.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "25.0");

        ModelHolder channel4 = new ModelHolder(Model.CHANNEL);
        channel4.setField(ModelField.CHANNEL_CODE, "813.000230");
        channel4.setField(ModelField.CHANNEL_NAME, "Тиск на виході з колектора пилу відпрацьованого газу");
        channel4.setField(ModelField.CHANNEL_DEPARTMENT, "ЦВО");
        channel4.setField(ModelField.CHANNEL_AREA, "ЦВО-1");
        channel4.setField(ModelField.CHANNEL_PROCESS, "Технологічна лінія №2");
        channel4.setField(ModelField.CHANNEL_INSTALLATION, "");
        channel4.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "Р-202-4");
        channel4.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "514");
        channel4.setField(ModelField.CHANNEL_REFERENCE, "");
        channel4.setField(ModelField.CHANNEL_DATE, "29.11.2021");
        channel4.setField(ModelField.CHANNEL_SUITABILITY, "true");
        channel4.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "мм вод ст");
        channel4.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"Deltabar S\",\n" +
                "  \"name\" : \"Deltabar S\",\n" +
                "  \"rangeMin\" : -750.0,\n" +
                "  \"rangeMax\" : 0.0,\n" +
                "  \"number\" : \"\",\n" +
                "  \"value\" : \"мм вод ст\",\n" +
                "  \"measurement\" : \"Тиск\",\n" +
                "  \"errorFormula\" : \"(convR / 100) * 0.075\"\n" +
                "}");
        channel4.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        channel4.setField(ModelField.CHANNEL_RANGE_MIN, "-750.0");
        channel4.setField(ModelField.CHANNEL_RANGE_MAX, "0.0");
        channel4.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "1.5");
        channel4.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "11.25");

        ModelHolder channel5 = new ModelHolder(Model.CHANNEL);
        channel5.setField(ModelField.CHANNEL_CODE, "811.013469");
        channel5.setField(ModelField.CHANNEL_NAME, "Витрата води в млин №144");
        channel5.setField(ModelField.CHANNEL_DEPARTMENT, "ДЗФ");
        channel5.setField(ModelField.CHANNEL_AREA, "ОВДЗ-4");
        channel5.setField(ModelField.CHANNEL_PROCESS, "Секція №14");
        channel5.setField(ModelField.CHANNEL_INSTALLATION, "Млин №144");
        channel5.setField(ModelField.CHANNEL_TECHNOLOGY_NUMBER, "14FT006");
        channel5.setField(ModelField.CHANNEL_PROTOCOL_NUMBER, "504");
        channel5.setField(ModelField.CHANNEL_REFERENCE, "");
        channel5.setField(ModelField.CHANNEL_DATE, "24.11.2021");
        channel5.setField(ModelField.CHANNEL_SUITABILITY, "true");
        channel5.setField(ModelField.CHANNEL_MEASUREMENT_VALUE, "м³/h");
        channel5.setField(ModelField.CHANNEL_SENSOR_JSON, "{\n" +
                "  \"type\" : \"YOKOGAWA AXF050G\",\n" +
                "  \"name\" : \"YOKOGAWA AXF050G\",\n" +
                "  \"rangeMin\" : 0.0,\n" +
                "  \"rangeMax\" : 50.0,\n" +
                "  \"number\" : \"S5V107745 904/M01737 903\",\n" +
                "  \"value\" : \"м³/h\",\n" +
                "  \"measurement\" : \"Витрата\",\n" +
                "  \"errorFormula\" : \"(R / 100) * 0.35\"\n" +
                "}");
        channel5.setField(ModelField.CHANNEL_FREQUENCY, "2.0");
        channel5.setField(ModelField.CHANNEL_RANGE_MIN, "0.0");
        channel5.setField(ModelField.CHANNEL_RANGE_MAX, "50.0");
        channel5.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_PERCENT, "0.35");
        channel5.setField(ModelField.CHANNEL_ALLOWABLE_ERROR_VALUE, "0.18");

        return Arrays.asList(channel1, channel2, channel3, channel4, channel5);
    }

    private void checkChannels(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedChannels();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.CHANNEL).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedControlPoints() {
        ModelHolder controlPoints1 = new ModelHolder(Model.CONTROL_POINTS);
        controlPoints1.setField(ModelField.CONTROL_POINTS_SENSOR_TYPE, "ТСМ-50М");
        controlPoints1.setField(ModelField.CONTROL_POINTS_VALUES, "0.0|5.14|50.0|94.86|100.0|");
        controlPoints1.setField(ModelField.CONTROL_POINTS_RANGE_MIN, "0.0");
        controlPoints1.setField(ModelField.CONTROL_POINTS_RANGE_MAX, "100.0");

        ModelHolder controlPoints2 = new ModelHolder(Model.CONTROL_POINTS);
        controlPoints2.setField(ModelField.CONTROL_POINTS_SENSOR_TYPE, "ТСМ-50М");
        controlPoints2.setField(ModelField.CONTROL_POINTS_VALUES, "-50.13|-39.59|64.95|168.22|179.9|");
        controlPoints2.setField(ModelField.CONTROL_POINTS_RANGE_MIN, "-50.0");
        controlPoints2.setField(ModelField.CONTROL_POINTS_RANGE_MAX, "180.0");

        return Arrays.asList(controlPoints1, controlPoints2);
    }

    private void checkControlPoints(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedControlPoints();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.CONTROL_POINTS).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedMeasurement() {
        ModelHolder temperature = new ModelHolder(Model.MEASUREMENT);
        temperature.setField(ModelField.MEASUREMENT_NAME, "Температура");
        temperature.setField(ModelField.MEASUREMENT_VALUE, "℃");
        temperature.setField(ModelField.MEASUREMENT_FACTORS, "{ }");

        ModelHolder pressure1 = new ModelHolder(Model.MEASUREMENT);
        pressure1.setField(ModelField.MEASUREMENT_NAME, "Тиск");
        pressure1.setField(ModelField.MEASUREMENT_VALUE, "кПа");
        pressure1.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                "  \"мм вод ст\" : 101.9716,\n" +
                "  \"кгс/см²\" : 0.01019716\n" +
                "}");

        ModelHolder pressure2 = new ModelHolder(Model.MEASUREMENT);
        pressure2.setField(ModelField.MEASUREMENT_NAME, "Тиск");
        pressure2.setField(ModelField.MEASUREMENT_VALUE, "мм вод ст");
        pressure2.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                "  \"кгс/см²\" : 1.0E-4,\n" +
                "  \"кПа\" : 0.00980775\n" +
                "}");

        ModelHolder pressure3 = new ModelHolder(Model.MEASUREMENT);
        pressure3.setField(ModelField.MEASUREMENT_NAME, "Тиск");
        pressure3.setField(ModelField.MEASUREMENT_VALUE, "кгс/см²");
        pressure3.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                "  \"мм вод ст\" : 10000.0,\n" +
                "  \"кПа\" : 98.0665\n" +
                "}");

        ModelHolder consumption = new ModelHolder(Model.MEASUREMENT);
        consumption.setField(ModelField.MEASUREMENT_NAME, "Витрата");
        consumption.setField(ModelField.MEASUREMENT_VALUE, "м³/h");
        consumption.setField(ModelField.MEASUREMENT_FACTORS, "{ }");

        return Arrays.asList(temperature, pressure1, pressure2, pressure3, consumption);
    }

    private void checkMeasurements(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedMeasurement();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.MEASUREMENT).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedPersons() {
        ModelHolder sidor = new ModelHolder(Model.PERSON);
        sidor.setField(ModelField.PERSON_NAME, "Сидор");
        sidor.setField(ModelField.PERSON_SURNAME, "Сидоров");
        sidor.setField(ModelField.PERSON_PATRONYMIC, "Сидорович");
        sidor.setField(ModelField.PERSON_POSITION, "Начальник дільниці");

        ModelHolder petr = new ModelHolder(Model.PERSON);
        petr.setField(ModelField.PERSON_NAME, "Петр");
        petr.setField(ModelField.PERSON_SURNAME, "Петров");
        petr.setField(ModelField.PERSON_PATRONYMIC, "Петрович");
        petr.setField(ModelField.PERSON_POSITION, "Інженер");

        ModelHolder vasil = new ModelHolder(Model.PERSON);
        vasil.setField(ModelField.PERSON_NAME, "Василь");
        vasil.setField(ModelField.PERSON_SURNAME, "Васильєв");
        vasil.setField(ModelField.PERSON_PATRONYMIC, "Васильйович");
        vasil.setField(ModelField.PERSON_POSITION, "Слюсар");

        return Arrays.asList(sidor, petr, vasil);
    }

    private void checkPersons(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedPersons();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.PERSON).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    private List<ModelHolder> createExpectedSensors() {
        ModelHolder sensor1 = new ModelHolder(Model.SENSOR);
        sensor1.setField(ModelField.SENSOR_NAME, "ТСМ-50М");
        sensor1.setField(ModelField.SENSOR_TYPE, "ТСМ-50М");
        sensor1.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        sensor1.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Температура");
        sensor1.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "℃");
        sensor1.setField(ModelField.SENSOR_ERROR_FORMULA, "(0.005 * R) + 0.3");
        sensor1.setField(ModelField.SENSOR_RANGE_MIN, "-50.0");
        sensor1.setField(ModelField.SENSOR_RANGE_MAX, "180.0");

        ModelHolder sensor2 = new ModelHolder(Model.SENSOR);
        sensor2.setField(ModelField.SENSOR_NAME, "Термопара TXA-2388 (тип К) > 333.5℃");
        sensor2.setField(ModelField.SENSOR_TYPE, "Термопара TXA-2388 (тип К)");
        sensor2.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        sensor2.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Температура");
        sensor2.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "℃");
        sensor2.setField(ModelField.SENSOR_ERROR_FORMULA, "0.0075 * R");
        sensor2.setField(ModelField.SENSOR_RANGE_MIN, "-50.0");
        sensor2.setField(ModelField.SENSOR_RANGE_MAX, "1250.0");

        ModelHolder sensor3 = new ModelHolder(Model.SENSOR);
        sensor3.setField(ModelField.SENSOR_NAME, "Deltabar S");
        sensor3.setField(ModelField.SENSOR_TYPE, "Deltabar S");
        sensor3.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        sensor3.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Тиск");
        sensor3.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "кПа");
        sensor3.setField(ModelField.SENSOR_ERROR_FORMULA, "(convR / 100) * 0.075");
        sensor3.setField(ModelField.SENSOR_RANGE_MIN, "-10.0");
        sensor3.setField(ModelField.SENSOR_RANGE_MAX, "10.0");

        ModelHolder sensor4 = new ModelHolder(Model.SENSOR);
        sensor4.setField(ModelField.SENSOR_NAME, "CERABAR M");
        sensor4.setField(ModelField.SENSOR_TYPE, "CERABAR M");
        sensor4.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        sensor4.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Тиск");
        sensor4.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "кПа");
        sensor4.setField(ModelField.SENSOR_ERROR_FORMULA, "(convR/100) * 0.15");
        sensor4.setField(ModelField.SENSOR_RANGE_MIN, "-100.0");
        sensor4.setField(ModelField.SENSOR_RANGE_MAX, "30.0");

        ModelHolder sensor5 = new ModelHolder(Model.SENSOR);
        sensor5.setField(ModelField.SENSOR_NAME, "YOKOGAWA AXF050G");
        sensor5.setField(ModelField.SENSOR_TYPE, "YOKOGAWA AXF050G");
        sensor5.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
        sensor5.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Витрата");
        sensor5.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "");
        sensor5.setField(ModelField.SENSOR_ERROR_FORMULA, "(R / 100) * 0.35");
        sensor5.setField(ModelField.SENSOR_RANGE_MIN, "0.0");
        sensor5.setField(ModelField.SENSOR_RANGE_MAX, "0.0");

        return Arrays.asList(sensor1, sensor2, sensor3, sensor4, sensor5);
    }

    private void checkSensors(List<ModelHolder> read) {
        List<ModelHolder> expected = createExpectedSensors();
        List<ModelHolder> actual = read.stream().filter(m -> m.getModel() == Model.SENSOR).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}