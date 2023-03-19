package service.importer.updater.from_v5.to_v6;

import mock.RepositoryMockFactory;
import org.junit.Before;
import service.importer.ImportOption;
import service.importer.Importer;
import service.importer.model.Model;
import service.importer.model.ModelField;
import service.importer.model.ModelHolder;

import java.util.Arrays;
import java.util.List;

public class DefaultImporterTest {
    private final RepositoryMockFactory repositoryMockFactory = new RepositoryMockFactory();
    private Importer importerWithReplaceExisted;
    private Importer importerWithIgnoreExisted;

    @Before
    public void init() {
        importerWithReplaceExisted = new DefaultImporter(ImportOption.REPLACE_EXISTED, repositoryMockFactory);
        importerWithReplaceExisted = new DefaultImporter(ImportOption.REPLACE_EXISTED, repositoryMockFactory);
    }

    private List<ModelHolder> testMeasurements;
    private List<ModelHolder> getTestMeasurements() {
        if (testMeasurements == null) {
            ModelHolder measurement1 = new ModelHolder(Model.MEASUREMENT);
            measurement1.setField(ModelField.MEASUREMENT_NAME, "Температура");
            measurement1.setField(ModelField.MEASUREMENT_VALUE, "℃");
            measurement1.setField(ModelField.MEASUREMENT_FACTORS, "{ }");

            ModelHolder measurement2 = new ModelHolder(Model.MEASUREMENT);
            measurement2.setField(ModelField.MEASUREMENT_NAME, "Тиск");
            measurement2.setField(ModelField.MEASUREMENT_VALUE, "кПа");
            measurement2.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                    "  \"мм вод ст\" : 101.9716,\n" +
                    "  \"кгс/см²\" : 0.01019716\n" +
                    "}");

            ModelHolder measurement3 = new ModelHolder(Model.MEASUREMENT);
            measurement3.setField(ModelField.MEASUREMENT_NAME, "Тиск");
            measurement3.setField(ModelField.MEASUREMENT_VALUE, "мм вод ст");
            measurement3.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                    "  \"кгс/см²\" : 1.0E-4,\n" +
                    "  \"кПа\" : 0.00980775\n" +
                    "}");

            ModelHolder measurement4 = new ModelHolder(Model.MEASUREMENT);
            measurement4.setField(ModelField.MEASUREMENT_NAME, "Тиск");
            measurement4.setField(ModelField.MEASUREMENT_VALUE, "кгс/см²");
            measurement4.setField(ModelField.MEASUREMENT_FACTORS, "{\n" +
                    "  \"мм вод ст\" : 10000.0,\n" +
                    "  \"кПа\" : 98.0665\n" +
                    "}");

            ModelHolder measurement5 = new ModelHolder(Model.MEASUREMENT);
            measurement5.setField(ModelField.MEASUREMENT_NAME, "Витрата");
            measurement5.setField(ModelField.MEASUREMENT_VALUE, "м³/h");
            measurement5.setField(ModelField.MEASUREMENT_FACTORS, "{ }");

            testMeasurements = Arrays.asList(measurement1, measurement2, measurement3, measurement4, measurement5);
        }
        return testMeasurements;
    }

    private List<ModelHolder> testDepartments;
    private List<ModelHolder> getTestDepartments() {
        if (testDepartments == null) {
            ModelHolder department1 = new ModelHolder(Model.DEPARTMENT);
            department1.setField(ModelField.DEPARTMENT, "ЦВО");

            ModelHolder department2 = new ModelHolder(Model.DEPARTMENT);
            department2.setField(ModelField.DEPARTMENT, "ДЗФ");

            testDepartments = Arrays.asList(department1, department2);
        }
        return testDepartments;
    }

    private List<ModelHolder> testAreas;
    private List<ModelHolder> getTestAreas() {
        if (testAreas == null) {
            ModelHolder area1 = new ModelHolder(Model.AREA);
            area1.setField(ModelField.AREA, "ЦВО-1");

            ModelHolder area2 = new ModelHolder(Model.AREA);
            area2.setField(ModelField.AREA, "ЦВО-2");

            ModelHolder area3 = new ModelHolder(Model.AREA);
            area3.setField(ModelField.AREA, "ОВДЗ-2");

            ModelHolder area4 = new ModelHolder(Model.AREA);
            area4.setField(ModelField.AREA, "ОВДЗ-4");

            testAreas = Arrays.asList(area1, area2, area3, area4);
        }
        return testAreas;
    }

    private List<ModelHolder> testProcesses;
    private List<ModelHolder> getTestProcesses() {
        if (testProcesses == null) {
            ModelHolder process1 = new ModelHolder(Model.PROCESS);
            process1.setField(ModelField.PROCESS, "Бармак");

            ModelHolder process2 = new ModelHolder(Model.PROCESS);
            process2.setField(ModelField.PROCESS, "Секція");

            ModelHolder process3 = new ModelHolder(Model.PROCESS);
            process3.setField(ModelField.PROCESS, "Тракт");

            ModelHolder process4 = new ModelHolder(Model.PROCESS);
            process4.setField(ModelField.PROCESS, "Технологічна лінія");

            testProcesses = Arrays.asList(process1, process2, process3, process4);
        }
        return testProcesses;
    }

    private List<ModelHolder> testInstallations;
    private List<ModelHolder> getTestInstallations() {
        if (testInstallations == null) {
            ModelHolder installation1 = new ModelHolder(Model.INSTALLATION);
            installation1.setField(ModelField.INSTALLATION, "Млин");

            ModelHolder installation2 = new ModelHolder(Model.INSTALLATION);
            installation2.setField(ModelField.INSTALLATION, "Гідроциклон 710мм");

            ModelHolder installation3 = new ModelHolder(Model.INSTALLATION);
            installation3.setField(ModelField.INSTALLATION, "Вентилятор");

            ModelHolder installation4 = new ModelHolder(Model.INSTALLATION);
            installation4.setField(ModelField.INSTALLATION, "Охолоджувач");

            testInstallations = Arrays.asList(installation1, installation2, installation3, installation4);
        }
        return testInstallations;
    }

    private List<ModelHolder> testCalibrators;
    private List<ModelHolder> getTestCalibrators() {
        if (testCalibrators == null) {
            ModelHolder calibrator1 = new ModelHolder(Model.CALIBRATOR);
            calibrator1.setField(ModelField.CALIBRATOR_NAME, "Fluke 725");
            calibrator1.setField(ModelField.CALIBRATOR_TYPE, "Fluke 725");
            calibrator1.setField(ModelField.CALIBRATOR_NUMBER, "1988293");
            calibrator1.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Температура");
            calibrator1.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "℃");
            calibrator1.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "0.7");
            calibrator1.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n" +
                    "  \"name\" : \"№M-140726-1\",\n" +
                    "  \"date\" : \"16.05.2022\",\n" +
                    "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n" +
                    "  \"type\" : \"Свідоцтво про перевірку МХ\"\n" +
                    "}");
            calibrator1.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
            calibrator1.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

            ModelHolder calibrator2 = new ModelHolder(Model.CALIBRATOR);
            calibrator2.setField(ModelField.CALIBRATOR_NAME, "Prova-123 t < 0℃");
            calibrator2.setField(ModelField.CALIBRATOR_TYPE, "Prova-123");
            calibrator2.setField(ModelField.CALIBRATOR_NUMBER, "13180302");
            calibrator2.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Температура");
            calibrator2.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "℃");
            calibrator2.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "1.1");
            calibrator2.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n" +
                    "  \"name\" : \"№06/2315К\",\n" +
                    "  \"date\" : \"21.07.2020\",\n" +
                    "  \"company\" : \"ДП\\\"ХарківСтандартМетрологія\\\"\",\n" +
                    "  \"type\" : \"Сертифікат калібрування\"\n" +
                    "}");
            calibrator2.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
            calibrator2.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

            ModelHolder calibrator3 = new ModelHolder(Model.CALIBRATOR);
            calibrator3.setField(ModelField.CALIBRATOR_NAME, "Fluke 718 30G");
            calibrator3.setField(ModelField.CALIBRATOR_TYPE, "Fluke 718 30G");
            calibrator3.setField(ModelField.CALIBRATOR_NUMBER, "2427047");
            calibrator3.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Тиск");
            calibrator3.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "кПа");
            calibrator3.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(convR / 100) * 0.05");
            calibrator3.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n" +
                    "  \"name\" : \"№M-140726-2\",\n" +
                    "  \"date\" : \"16.05.2022\",\n" +
                    "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n" +
                    "  \"type\" : \"Свідоцтво про перевірку МХ\"\n" +
                    "}");
            calibrator3.setField(ModelField.CALIBRATOR_RANGE_MIN, "-83.0");
            calibrator3.setField(ModelField.CALIBRATOR_RANGE_MAX, "207.0");

            ModelHolder calibrator4 = new ModelHolder(Model.CALIBRATOR);
            calibrator4.setField(ModelField.CALIBRATOR_NAME, "Fluke 750PD2");
            calibrator4.setField(ModelField.CALIBRATOR_TYPE, "Fluke 750PD2");
            calibrator4.setField(ModelField.CALIBRATOR_NUMBER, "4043273");
            calibrator4.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Тиск");
            calibrator4.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "кПа");
            calibrator4.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(convR / 100) * 0.05");
            calibrator4.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n" +
                    "  \"name\" : \"№M-140726-2\",\n" +
                    "  \"date\" : \"16.05.2022\",\n" +
                    "  \"company\" : \"ДП\\\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\\\"\",\n" +
                    "  \"type\" : \"Свідоцтво про перевірку МХ\"\n" +
                    "}");
            calibrator4.setField(ModelField.CALIBRATOR_RANGE_MIN, "-7.0");
            calibrator4.setField(ModelField.CALIBRATOR_RANGE_MAX, "7.0");

            ModelHolder calibrator5 = new ModelHolder(Model.CALIBRATOR);
            calibrator5.setField(ModelField.CALIBRATOR_NAME, "YAKOGAWA AM012");
            calibrator5.setField(ModelField.CALIBRATOR_TYPE, "AM012");
            calibrator5.setField(ModelField.CALIBRATOR_NUMBER, "S5T800358");
            calibrator5.setField(ModelField.CALIBRATOR_MEASUREMENT_NAME, "Витрата");
            calibrator5.setField(ModelField.CALIBRATOR_MEASUREMENT_VALUE, "");
            calibrator5.setField(ModelField.CALIBRATOR_ERROR_FORMULA, "(R / 100) * 0.06");
            calibrator5.setField(ModelField.CALIBRATOR_CERTIFICATE, "{\n" +
                    "  \"name\" : \"№UA/24/200717/265\",\n" +
                    "  \"date\" : \"17.07.2020\",\n" +
                    "  \"company\" : \"ДП\\\"Укрметртестстандарт\\\"\",\n" +
                    "  \"type\" : \"Сертифікат калібрування\"\n" +
                    "}");
            calibrator5.setField(ModelField.CALIBRATOR_RANGE_MIN, "0.0");
            calibrator5.setField(ModelField.CALIBRATOR_RANGE_MAX, "0.0");

            testCalibrators = Arrays.asList(calibrator1, calibrator2, calibrator3, calibrator4, calibrator5);
        }
        return testCalibrators;
    }

    private List<ModelHolder> testChannels;
    private List<ModelHolder> getTestChannels() {
        if (testChannels == null) {
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

            testChannels = Arrays.asList(channel1, channel2, channel3, channel4, channel5);
        }
        return testChannels;
    }

    private List<ModelHolder> testControlPoints;
    private List<ModelHolder> getTestControlPoints() {
        if (testControlPoints == null) {
            ModelHolder controlPoints1 = new ModelHolder(Model.CONTROL_POINTS);
            controlPoints1.setField(ModelField.CONTROL_POINTS_SENSOR_TYPE, "ТСМ-50М");
            controlPoints1.setField(ModelField.CONTROL_POINTS_RANGE_MIN, "0.0");
            controlPoints1.setField(ModelField.CONTROL_POINTS_RANGE_MAX, "100.0");
            controlPoints1.setField(ModelField.CONTROL_POINTS_VALUES, "0.0|5.14|50.0|94.86|100.0|");

            ModelHolder controlPoints2 = new ModelHolder(Model.CONTROL_POINTS);
            controlPoints2.setField(ModelField.CONTROL_POINTS_SENSOR_TYPE, "ТСМ-50М");
            controlPoints2.setField(ModelField.CONTROL_POINTS_RANGE_MIN, "-50.0");
            controlPoints2.setField(ModelField.CONTROL_POINTS_RANGE_MAX, "180.0");
            controlPoints2.setField(ModelField.CONTROL_POINTS_VALUES, "-50.13|-39.59|64.95|168.22|179.9|");

            testControlPoints = Arrays.asList(controlPoints1, controlPoints2);
        }
        return testControlPoints;
    }

    private List<ModelHolder> testPersons;
    private List<ModelHolder> getTestPersons() {
        if (testPersons == null) {
            ModelHolder person1 = new ModelHolder(Model.PERSON);
            person1.setField(ModelField.PERSON_NAME, "Сидор");
            person1.setField(ModelField.PERSON_SURNAME, "Сидоров");
            person1.setField(ModelField.PERSON_PATRONYMIC, "Сидорович");
            person1.setField(ModelField.PERSON_POSITION, "Начальник дільниці");

            ModelHolder person2 = new ModelHolder(Model.PERSON);
            person2.setField(ModelField.PERSON_NAME, "Петр");
            person2.setField(ModelField.PERSON_SURNAME, "Петров");
            person2.setField(ModelField.PERSON_PATRONYMIC, "Петрович");
            person2.setField(ModelField.PERSON_POSITION, "Інженер");

            ModelHolder person3 = new ModelHolder(Model.PERSON);
            person3.setField(ModelField.PERSON_NAME, "Василь");
            person3.setField(ModelField.PERSON_SURNAME, "Васильєв");
            person3.setField(ModelField.PERSON_PATRONYMIC, "Васильйович");
            person3.setField(ModelField.PERSON_POSITION, "Слюсар");

            testPersons = Arrays.asList(person1, person2, person3);
        }
        return testPersons;
    }

    private List<ModelHolder> testSensors;
    private List<ModelHolder> getTestSensors() {
        if (testSensors == null) {
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
            sensor4.setField(ModelField.SENSOR_NAME, "YOKOGAWA AXF050G");
            sensor4.setField(ModelField.SENSOR_TYPE, "YOKOGAWA AXF050G");
            sensor4.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
            sensor4.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Витрата");
            sensor4.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "");
            sensor4.setField(ModelField.SENSOR_ERROR_FORMULA, "(R / 100) * 0.35");
            sensor4.setField(ModelField.SENSOR_RANGE_MIN, "0.0");
            sensor4.setField(ModelField.SENSOR_RANGE_MAX, "0.0");

            ModelHolder sensor5 = new ModelHolder(Model.SENSOR);
            sensor5.setField(ModelField.SENSOR_NAME, "CERABAR M");
            sensor5.setField(ModelField.SENSOR_TYPE, "CERABAR M");
            sensor5.setField(ModelField.SENSOR_SERIAL_NUMBER, "");
            sensor5.setField(ModelField.SENSOR_MEASUREMENT_NAME, "Тиск");
            sensor5.setField(ModelField.SENSOR_MEASUREMENT_VALUE, "кПа");
            sensor5.setField(ModelField.SENSOR_ERROR_FORMULA, "(convR/100) * 0.15");
            sensor5.setField(ModelField.SENSOR_RANGE_MIN, "-100.0");
            sensor5.setField(ModelField.SENSOR_RANGE_MAX, "30.0");

            testSensors = Arrays.asList(sensor1, sensor2, sensor3, sensor4, sensor5);
        }
        return testSensors;
    }
}