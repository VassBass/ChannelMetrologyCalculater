package service.importer.updater.from_v5.to_v6;

import mock.RepositoryConfigHolderMock;
import mock.RepositoryMockFactory;
import model.dto.*;
import model.dto.builder.CalibratorBuilder;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.ControlPointsBuilder;
import model.dto.builder.SensorBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.sensor.SensorRepository;
import service.importer.ImportOption;
import service.importer.Importer;
import service.importer.model.Model;
import service.importer.model.ModelField;
import service.importer.model.ModelHolder;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultImporterTest {
    private RepositoryMockFactory repositoryMockFactory;

    private MeasurementRepository measurementRepository;
    private CalibratorRepository calibratorRepository;
    private ChannelRepository channelRepository;
    private ControlPointsRepository controlPointsRepository;
    private PersonRepository personRepository;
    private SensorRepository sensorRepository;
    private MeasurementFactorRepository measurementFactorRepository;

    @Before
    public void init() {
        repositoryMockFactory = new RepositoryMockFactory(new RepositoryConfigHolderMock());
        measurementRepository = repositoryMockFactory.getImplementation(MeasurementRepository.class);
        calibratorRepository = repositoryMockFactory.getImplementation(CalibratorRepository.class);
        channelRepository = repositoryMockFactory.getImplementation(ChannelRepository.class);
        controlPointsRepository = repositoryMockFactory.getImplementation(ControlPointsRepository.class);
        personRepository = repositoryMockFactory.getImplementation(PersonRepository.class);
        sensorRepository = repositoryMockFactory.getImplementation(SensorRepository.class);
        measurementFactorRepository = repositoryMockFactory.getImplementation(MeasurementFactorRepository.class);

        assertNotNull(measurementRepository);
        assertNotNull(calibratorRepository);
        assertNotNull(channelRepository);
        assertNotNull(controlPointsRepository);
        assertNotNull(personRepository);
        assertNotNull(sensorRepository);
        assertNotNull(measurementFactorRepository);

        Measurement measurement = new Measurement("Розрядження", "кПа");
        Measurement measurementT = new Measurement("Температура", "℃");
        measurementRepository.add(measurement);
        measurementRepository.add(measurementT);

        Calibrator calibrator = new CalibratorBuilder("Fluke 725")
                .setType("Fluke 725")
                .setNumber("Number")
                .setCertificate("Certificate", "Certificate", "24.02.2022", "rushists")
                .setRangeMin(0.0)
                .setRangeMax(0.0)
                .setMeasurementName("Температура")
                .setMeasurementValue("℃")
                .setErrorFormula("0.7")
                .build();
        calibratorRepository.add(calibrator);

        Channel channel = new ChannelBuilder("813.000540")
                .setName("Температура підшипників розвантаження млину №32")
                .setMeasurementName("Температура")
                .setMeasurementValue("℃")
                .setDepartment("Department")
                .setArea("Area")
                .setProcess("Process")
                .setInstallation("Installation")
                .setDate("14.12.2021")
                .setFrequency(2.0)
                .setTechnologyNumber("32ТЕ17")
                .setNumberOfProtocol("570")
                .setRangeMin(0.0)
                .setRangeMax(100.0)
                .setSuitability(true)
                .setReference("")
                .setAllowableErrorInPercent(1.5)
                .setAllowableErrorInValue(1.5)
                .build();
        channelRepository.add(channel);

        String controlPointsName = ControlPoints.createName("ТСМ-50М", 0.0, 100.0);
        ControlPoints controlPoints = new ControlPointsBuilder(controlPointsName)
                .setSensorType("ТСМ-50М")
                .addControlPoint(5.0, 5.14)
                .addControlPoint(50.0, 55.0)
                .addControlPoint(95.0, 94.86)
                .build();
        controlPointsRepository.add(controlPoints);

        Person person = new Person(50);
        person.setName("Петр");
        person.setSurname("Петров");
        person.setPatronymic("Петрович");
        person.setPosition("Інженер");
        personRepository.add(person);

        Sensor sensor = new SensorBuilder("813.000540")
                .setType("ТСМ-50М")
                .setRangeMin(-50.0)
                .setRangeMax(180.0)
                .setMeasurementName("Температура")
                .setMeasurementValue("℃")
                .setSerialNumber("Number")
                .setErrorFormula("Formula")
                .build();
        sensorRepository.add(sensor);
    }

    @After
    public void dispose() {
        repositoryMockFactory.dispose();
    }

    @Test
    public void impostWithIgnoreExisted() {
        Importer importer = new DefaultImporter(ImportOption.IGNORE_EXISTED, repositoryMockFactory);

        List<ModelHolder> in = new ArrayList<>(getTestMeasurements());
        in.addAll(getTestDepartments());
        in.addAll(getTestAreas());
        in.addAll(getTestProcesses());
        in.addAll(getTestInstallations());
        in.addAll(getTestCalibrators());
        in.addAll(getTestChannels());
        in.addAll(getTestSensors());
        in.addAll(getTestControlPoints());
        in.addAll(getTestPersons());

        assertTrue(importer.importing(in));

        Measurement measurement1 = measurementRepository.getByValue("кПа");
        assertNotNull(measurement1);
        assertEquals("Тиск", measurement1.getName());

        Measurement measurement2 = measurementRepository.getByValue("℃");
        assertNotNull(measurement2);
        assertEquals("Температура", measurement2.getName());

        Measurement measurement3 = measurementRepository.getByValue("мм вод ст");
        assertNotNull(measurement3);
        assertEquals("Тиск", measurement3.getName());

        Measurement measurement4 = measurementRepository.getByValue("кгс/см²");
        assertNotNull(measurement4);
        assertEquals("Тиск", measurement4.getName());

        Measurement measurement5 = measurementRepository.getByValue("м³/h");
        assertNotNull(measurement5);
        assertEquals("Витрата", measurement5.getName());

        assertEquals(5, calibratorRepository.getAll().size());

        Calibrator actualCalibrator1 = calibratorRepository.get("Fluke 725");
        assertNotNull(actualCalibrator1);
        assertEquals("Fluke 725", actualCalibrator1.getType());
        assertEquals("Number", actualCalibrator1.getNumber());
        assertEquals("Certificate", actualCalibrator1.getCertificate().getType());
        assertEquals("Certificate", actualCalibrator1.getCertificate().getName());
        assertEquals("24.02.2022", actualCalibrator1.getCertificate().getDate());
        assertEquals("rushists", actualCalibrator1.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator1.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator1.getRangeMax(), 0.0);
        assertEquals("Температура", actualCalibrator1.getMeasurementName());
        assertEquals("℃", actualCalibrator1.getMeasurementValue());
        assertEquals("0.7", actualCalibrator1.getErrorFormula());

        Calibrator actualCalibrator2 = calibratorRepository.get("Prova-123 t < 0℃");
        assertNotNull(actualCalibrator2);
        assertEquals("Prova-123", actualCalibrator2.getType());
        assertEquals("13180302", actualCalibrator2.getNumber());
        assertEquals("Сертифікат калібрування", actualCalibrator2.getCertificate().getType());
        assertEquals("№06/2315К", actualCalibrator2.getCertificate().getName());
        assertEquals("21.07.2020", actualCalibrator2.getCertificate().getDate());
        assertEquals("ДП\"ХарківСтандартМетрологія\"", actualCalibrator2.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator2.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator2.getRangeMax(), 0.0);
        assertEquals("Температура", actualCalibrator2.getMeasurementName());
        assertEquals("℃", actualCalibrator2.getMeasurementValue());
        assertEquals("1.1", actualCalibrator2.getErrorFormula());

        Calibrator actualCalibrator3 = calibratorRepository.get("Fluke 718 30G");
        assertNotNull(actualCalibrator3);
        assertEquals("Fluke 718 30G", actualCalibrator3.getType());
        assertEquals("2427047", actualCalibrator3.getNumber());
        assertEquals("Свідоцтво про перевірку МХ", actualCalibrator3.getCertificate().getType());
        assertEquals("№M-140726-2", actualCalibrator3.getCertificate().getName());
        assertEquals("16.05.2022", actualCalibrator3.getCertificate().getDate());
        assertEquals("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"", actualCalibrator3.getCertificate().getCompany());
        assertEquals(-83.0, actualCalibrator3.getRangeMin(), 0.0);
        assertEquals(207.0, actualCalibrator3.getRangeMax(), 0.0);
        assertEquals("Тиск", actualCalibrator3.getMeasurementName());
        assertEquals("кПа", actualCalibrator3.getMeasurementValue());
        assertEquals("(convR / 100) * 0.05", actualCalibrator3.getErrorFormula());

        Calibrator actualCalibrator4 = calibratorRepository.get("Fluke 750PD2");
        assertNotNull(actualCalibrator4);
        assertEquals("Fluke 750PD2", actualCalibrator4.getType());
        assertEquals("4043273", actualCalibrator4.getNumber());
        assertEquals("Свідоцтво про перевірку МХ", actualCalibrator4.getCertificate().getType());
        assertEquals("№M-140726-2", actualCalibrator4.getCertificate().getName());
        assertEquals("16.05.2022", actualCalibrator4.getCertificate().getDate());
        assertEquals("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"", actualCalibrator4.getCertificate().getCompany());
        assertEquals(-7.0, actualCalibrator4.getRangeMin(), 0.0);
        assertEquals(7.0, actualCalibrator4.getRangeMax(), 0.0);
        assertEquals("Тиск", actualCalibrator4.getMeasurementName());
        assertEquals("кПа", actualCalibrator4.getMeasurementValue());
        assertEquals("(convR / 100) * 0.05", actualCalibrator4.getErrorFormula());

        Calibrator actualCalibrator5 = calibratorRepository.get("YAKOGAWA AM012");
        assertNotNull(actualCalibrator5);
        assertEquals("AM012", actualCalibrator5.getType());
        assertEquals("S5T800358", actualCalibrator5.getNumber());
        assertEquals("Сертифікат калібрування", actualCalibrator5.getCertificate().getType());
        assertEquals("№UA/24/200717/265", actualCalibrator5.getCertificate().getName());
        assertEquals("17.07.2020", actualCalibrator5.getCertificate().getDate());
        assertEquals("ДП\"Укрметртестстандарт\"", actualCalibrator5.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator5.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator5.getRangeMax(), 0.0);
        assertEquals("Витрата", actualCalibrator5.getMeasurementName());
        assertTrue(actualCalibrator5.getMeasurementValue().isEmpty());
        assertEquals("(R / 100) * 0.06", actualCalibrator5.getErrorFormula());

        assertEquals(5, channelRepository.getAll().size());

        Channel actualChannel1 = channelRepository.get("813.000540");
        assertNotNull(actualChannel1);
        assertEquals("Температура підшипників розвантаження млину №32", actualChannel1.getName());
        assertEquals("Температура", actualChannel1.getMeasurementName());
        assertEquals("℃", actualChannel1.getMeasurementValue());
        assertEquals("Department", actualChannel1.getDepartment());
        assertEquals("Area", actualChannel1.getArea());
        assertEquals("Process", actualChannel1.getProcess());
        assertEquals("Installation", actualChannel1.getInstallation());
        assertEquals("14.12.2021", actualChannel1.getDate());
        assertEquals(2.0, actualChannel1.getFrequency(), 0.0);
        assertEquals("32ТЕ17", actualChannel1.getTechnologyNumber());
        assertEquals("570", actualChannel1.getNumberOfProtocol());
        assertEquals(0.0, actualChannel1.getRangeMin(), 0.0);
        assertEquals(100.0, actualChannel1.getRangeMax(), 0.0);
        assertTrue(actualChannel1.isSuitability());
        assertTrue(actualChannel1.getReference().isEmpty());
        assertEquals(1.5, actualChannel1.getAllowableErrorPercent(), 0.0);
        assertEquals(1.5, actualChannel1.getAllowableErrorValue(), 0.0);

        Channel actualChannel2 = channelRepository.get("813.000211");
        assertNotNull(actualChannel2);
        assertEquals("Регулювання тиску в зоні набору кека фільтра 13510", actualChannel2.getName());
        assertEquals("ЦВО", actualChannel2.getDepartment());
        assertEquals("ЦВО-1", actualChannel2.getArea());
        assertEquals("Технологічна лінія №2", actualChannel2.getProcess());
        assertTrue(actualChannel2.getInstallation().isEmpty());
        assertEquals("Р-013-4", actualChannel2.getTechnologyNumber());
        assertEquals("562", actualChannel2.getNumberOfProtocol());
        assertTrue(actualChannel2.getReference().isEmpty());
        assertEquals("03.12.2021", actualChannel2.getDate());
        assertTrue(actualChannel2.isSuitability());
        assertEquals("Тиск", actualChannel2.getMeasurementName());
        assertEquals("кгс/см²", actualChannel2.getMeasurementValue());
        assertEquals(2.0, actualChannel2.getFrequency(), 0.0);
        assertEquals(-1.0, actualChannel2.getRangeMin(), 0.0);
        assertEquals(0.0, actualChannel2.getRangeMax(), 0.0);
        assertEquals(1.5, actualChannel2.getAllowableErrorPercent(), 0.0);
        assertEquals(0.015, actualChannel2.getAllowableErrorValue(), 0.0);

        Channel actualChannel3 = channelRepository.get("813.000226");
        assertNotNull(actualChannel3);
        assertEquals("Температура у трубі охолоджувача", actualChannel3.getName());
        assertEquals("ЦВО", actualChannel3.getDepartment());
        assertEquals("ЦВО-1", actualChannel3.getArea());
        assertEquals("Технологічна лінія №2", actualChannel3.getProcess());
        assertEquals("Охолоджувач", actualChannel3.getInstallation());
        assertEquals("Т-201-8", actualChannel3.getTechnologyNumber());
        assertEquals("545", actualChannel3.getNumberOfProtocol());
        assertTrue(actualChannel3.getReference().isEmpty());
        assertEquals("03.12.2021", actualChannel3.getDate());
        assertTrue(actualChannel3.isSuitability());
        assertEquals("Температура", actualChannel3.getMeasurementName());
        assertEquals("℃", actualChannel3.getMeasurementValue());
        assertEquals(2.0, actualChannel3.getFrequency(), 0.0);
        assertEquals(0.0, actualChannel3.getRangeMin(), 0.0);
        assertEquals(1250.0, actualChannel3.getRangeMax(), 0.0);
        assertEquals(2.0, actualChannel3.getAllowableErrorPercent(), 0.0);
        assertEquals(25.0, actualChannel3.getAllowableErrorValue(), 0.0);

        Channel actualChannel4 = channelRepository.get("813.000230");
        assertNotNull(actualChannel4);
        assertEquals("Тиск на виході з колектора пилу відпрацьованого газу", actualChannel4.getName());
        assertEquals("ЦВО", actualChannel4.getDepartment());
        assertEquals("ЦВО-1", actualChannel4.getArea());
        assertEquals("Технологічна лінія №2", actualChannel4.getProcess());
        assertTrue(actualChannel4.getInstallation().isEmpty());
        assertEquals("Р-202-4", actualChannel4.getTechnologyNumber());
        assertEquals("514", actualChannel4.getNumberOfProtocol());
        assertTrue(actualChannel4.getReference().isEmpty());
        assertEquals("29.11.2021", actualChannel4.getDate());
        assertTrue(actualChannel4.isSuitability());
        assertEquals("Тиск", actualChannel4.getMeasurementName());
        assertEquals("мм вод ст", actualChannel4.getMeasurementValue());
        assertEquals(2.0, actualChannel4.getFrequency(), 0.0);
        assertEquals(-750.0, actualChannel4.getRangeMin(), 0.0);
        assertEquals(0.0, actualChannel4.getRangeMax(), 0.0);
        assertEquals(1.5, actualChannel4.getAllowableErrorPercent(), 0.0);
        assertEquals(11.25, actualChannel4.getAllowableErrorValue(), 0.0);

        Channel actualChannel5 = channelRepository.get("811.013469");
        assertNotNull(actualChannel5);
        assertEquals("Витрата води в млин №144", actualChannel5.getName());
        assertEquals("ДЗФ", actualChannel5.getDepartment());
        assertEquals("ОВДЗ-4", actualChannel5.getArea());
        assertEquals("Секція №14", actualChannel5.getProcess());
        assertEquals("Млин №144", actualChannel5.getInstallation());
        assertEquals("14FT006", actualChannel5.getTechnologyNumber());
        assertEquals("504", actualChannel5.getNumberOfProtocol());
        assertTrue(actualChannel5.getReference().isEmpty());
        assertEquals("24.11.2021", actualChannel5.getDate());
        assertTrue(actualChannel5.isSuitability());
        assertEquals("Витрата", actualChannel5.getMeasurementName());
        assertEquals("м³/h", actualChannel5.getMeasurementValue());
        assertEquals(2.0, actualChannel5.getFrequency(), 0.0);
        assertEquals(0.0, actualChannel5.getRangeMin(), 0.0);
        assertEquals(50.0, actualChannel5.getRangeMax(), 0.0);
        assertEquals(0.35, actualChannel5.getAllowableErrorPercent(), 0.0);
        assertEquals(0.18, actualChannel5.getAllowableErrorValue(), 0.0);

        assertEquals(2, controlPointsRepository.getAll().size());

        String expectedType1 = "ТСМ-50М";
        double expectedRangeMin1 = 0.0;
        double expectedRangeMax1 = 100.0;
        Map<Double, Double> expectedValues1 = new HashMap<>();
        expectedValues1.put(5.0, 5.14);
        expectedValues1.put(50.0, 55.0);
        expectedValues1.put(95.0, 94.86);
        String controlPointsName1 = ControlPoints.createName(expectedType1, expectedRangeMin1, expectedRangeMax1);
        ControlPoints actualControlPoints1 = controlPointsRepository.get(controlPointsName1);
        assertNotNull(actualControlPoints1);
        assertEquals(expectedType1, actualControlPoints1.getSensorType());
        assertEquals(expectedValues1, actualControlPoints1.getValues());

        String expectedType2 = "ТСМ-50М";
        double expectedRangeMin2 = -50.0;
        double expectedRangeMax2 = 180.0;
        Map<Double, Double> expectedValues2 = new HashMap<>();
        expectedValues2.put(5.0, -39.59);
        expectedValues2.put(50.0, 64.95);
        expectedValues2.put(95.0, 168.22);
        String controlPointsName2 = ControlPoints.createName(expectedType2, expectedRangeMin2, expectedRangeMax2);
        ControlPoints actualControlPoints2 = controlPointsRepository.get(controlPointsName2);
        assertNotNull(actualControlPoints2);
        assertEquals(expectedType2, actualControlPoints2.getSensorType());
        assertEquals(expectedValues2, actualControlPoints2.getValues());

        Collection<Person> actualPersons = personRepository.getAll();
        assertEquals(3, actualPersons.size());

        Person person1 = new Person();
        person1.setName("Петр");
        person1.setSurname("Петров");
        person1.setPatronymic("Петрович");
        person1.setPosition("Інженер");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person1)).count());

        Person person2 = new Person();
        person2.setName("Сидор");
        person2.setSurname("Сидоров");
        person2.setPatronymic("Сидорович");
        person2.setPosition("Начальник дільниці");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person2)).count());

        Person person3 = new Person();
        person3.setName("Василь");
        person3.setSurname("Васильєв");
        person3.setPatronymic("Васильйович");
        person3.setPosition("Слюсар");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person3)).count());

        assertEquals(5, sensorRepository.getAll().size());

        Sensor actualSensor1 = sensorRepository.get("813.000540");
        assertNotNull(actualSensor1);
        assertEquals("ТСМ-50М", actualSensor1.getType());
        assertEquals(-50.0, actualSensor1.getRangeMin(), 0.0);
        assertEquals(180.0, actualSensor1.getRangeMax(), 0.0);
        assertEquals("Температура", actualSensor1.getMeasurementName());
        assertEquals("℃", actualSensor1.getMeasurementValue());
        assertEquals("Number", actualSensor1.getSerialNumber());
        assertEquals("Formula", actualSensor1.getErrorFormula());

        Sensor actualSensor2 = sensorRepository.get("813.000211");
        assertNotNull(actualSensor2);
        assertEquals("CERABAR M", actualSensor2.getType());
        assertEquals(-1.0, actualSensor2.getRangeMin(), 0.0);
        assertEquals(0.0, actualSensor2.getRangeMax(), 0.0);
        assertEquals("Тиск", actualSensor2.getMeasurementName());
        assertEquals("кгс/см²", actualSensor2.getMeasurementValue());
        assertTrue(actualSensor2.getSerialNumber().isEmpty());
        assertEquals("(convR/100) * 0.15", actualSensor2.getErrorFormula());

        Sensor actualSensor3 = sensorRepository.get("813.000226");
        assertNotNull(actualSensor3);
        assertEquals("Термопара TXA-2388 (тип К)", actualSensor3.getType());
        assertEquals(-50.0, actualSensor3.getRangeMin(), 0.0);
        assertEquals(1250.0, actualSensor3.getRangeMax(), 0.0);
        assertEquals("Температура", actualSensor3.getMeasurementName());
        assertEquals("℃", actualSensor3.getMeasurementValue());
        assertTrue(actualSensor3.getSerialNumber().isEmpty());
        assertEquals("0.0075 * R", actualSensor3.getErrorFormula());

        Sensor actualSensor4 = sensorRepository.get("813.000230");
        assertNotNull(actualSensor4);
        assertEquals("Deltabar S", actualSensor4.getType());
        assertEquals(-750.0, actualSensor4.getRangeMin(), 0.0);
        assertEquals(0.0, actualSensor4.getRangeMax(), 0.0);
        assertEquals("Тиск", actualSensor4.getMeasurementName());
        assertEquals("мм вод ст", actualSensor4.getMeasurementValue());
        assertTrue(actualSensor4.getSerialNumber().isEmpty());
        assertEquals("(convR / 100) * 0.075", actualSensor4.getErrorFormula());

        Sensor actualSensor5 = sensorRepository.get("811.013469");
        assertNotNull(actualSensor5);
        assertEquals("YOKOGAWA AXF050G", actualSensor5.getType());
        assertEquals(0.0, actualSensor5.getRangeMin(), 0.0);
        assertEquals(50.0, actualSensor5.getRangeMax(), 0.0);
        assertEquals("Витрата", actualSensor5.getMeasurementName());
        assertEquals("м³/h", actualSensor5.getMeasurementValue());
        assertEquals("S5V107745 904/M01737 903", actualSensor5.getSerialNumber());
        assertEquals("(R / 100) * 0.35", actualSensor5.getErrorFormula());

        assertEquals(6, measurementFactorRepository.getAll().size());

        MeasurementTransformFactor actualMtf1 = measurementFactorRepository.getBySource("кПа").stream()
                .filter(mtf -> mtf.getTransformTo().equals("мм вод ст"))
                .findAny().orElse(null);
        assertNotNull(actualMtf1);
        assertEquals(101.9716, actualMtf1.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf2 = measurementFactorRepository.getBySource("кПа").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кгс/см²"))
                .findAny().orElse(null);
        assertNotNull(actualMtf2);
        assertEquals(0.01019716, actualMtf2.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf3 = measurementFactorRepository.getBySource("мм вод ст").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кгс/см²"))
                .findAny().orElse(null);
        assertNotNull(actualMtf3);
        assertEquals(1.0E-4, actualMtf3.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf4 = measurementFactorRepository.getBySource("мм вод ст").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кПа"))
                .findAny().orElse(null);
        assertNotNull(actualMtf4);
        assertEquals(0.00980775, actualMtf4.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf5 = measurementFactorRepository.getBySource("кгс/см²").stream()
                .filter(mtf -> mtf.getTransformTo().equals("мм вод ст"))
                .findAny().orElse(null);
        assertNotNull(actualMtf5);
        assertEquals(10000.0, actualMtf5.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf6 = measurementFactorRepository.getBySource("кгс/см²").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кПа"))
                .findAny().orElse(null);
        assertNotNull(actualMtf6);
        assertEquals(98.0665, actualMtf6.getTransformFactor(), 0.0);
    }

    @Test
    public void impostWithReplaceExisted() {
        Importer importer = new DefaultImporter(ImportOption.REPLACE_EXISTED, repositoryMockFactory);

        List<ModelHolder> in = new ArrayList<>(getTestMeasurements());
        in.addAll(getTestDepartments());
        in.addAll(getTestAreas());
        in.addAll(getTestProcesses());
        in.addAll(getTestInstallations());
        in.addAll(getTestCalibrators());
        in.addAll(getTestChannels());
        in.addAll(getTestSensors());
        in.addAll(getTestControlPoints());
        in.addAll(getTestPersons());

        assertTrue(importer.importing(in));

        Measurement measurement1 = measurementRepository.getByValue("кПа");
        assertNotNull(measurement1);
        assertEquals("Тиск", measurement1.getName());

        Measurement measurement2 = measurementRepository.getByValue("℃");
        assertNotNull(measurement2);
        assertEquals("Температура", measurement2.getName());

        Measurement measurement3 = measurementRepository.getByValue("мм вод ст");
        assertNotNull(measurement3);
        assertEquals("Тиск", measurement3.getName());

        Measurement measurement4 = measurementRepository.getByValue("кгс/см²");
        assertNotNull(measurement4);
        assertEquals("Тиск", measurement4.getName());

        Measurement measurement5 = measurementRepository.getByValue("м³/h");
        assertNotNull(measurement5);
        assertEquals("Витрата", measurement5.getName());

        assertEquals(5, calibratorRepository.getAll().size());

        Calibrator actualCalibrator1 = calibratorRepository.get("Fluke 725");
        assertNotNull(actualCalibrator1);
        assertEquals("Fluke 725", actualCalibrator1.getType());
        assertEquals("1988293", actualCalibrator1.getNumber());
        assertEquals("Свідоцтво про перевірку МХ", actualCalibrator1.getCertificate().getType());
        assertEquals("№M-140726-1", actualCalibrator1.getCertificate().getName());
        assertEquals("16.05.2022", actualCalibrator1.getCertificate().getDate());
        assertEquals("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"", actualCalibrator1.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator1.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator1.getRangeMax(), 0.0);
        assertEquals("Температура", actualCalibrator1.getMeasurementName());
        assertEquals("℃", actualCalibrator1.getMeasurementValue());
        assertEquals("0.7", actualCalibrator1.getErrorFormula());

        Calibrator actualCalibrator2 = calibratorRepository.get("Prova-123 t < 0℃");
        assertNotNull(actualCalibrator2);
        assertEquals("Prova-123", actualCalibrator2.getType());
        assertEquals("13180302", actualCalibrator2.getNumber());
        assertEquals("Сертифікат калібрування", actualCalibrator2.getCertificate().getType());
        assertEquals("№06/2315К", actualCalibrator2.getCertificate().getName());
        assertEquals("21.07.2020", actualCalibrator2.getCertificate().getDate());
        assertEquals("ДП\"ХарківСтандартМетрологія\"", actualCalibrator2.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator2.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator2.getRangeMax(), 0.0);
        assertEquals("Температура", actualCalibrator2.getMeasurementName());
        assertEquals("℃", actualCalibrator2.getMeasurementValue());
        assertEquals("1.1", actualCalibrator2.getErrorFormula());

        Calibrator actualCalibrator3 = calibratorRepository.get("Fluke 718 30G");
        assertNotNull(actualCalibrator3);
        assertEquals("Fluke 718 30G", actualCalibrator3.getType());
        assertEquals("2427047", actualCalibrator3.getNumber());
        assertEquals("Свідоцтво про перевірку МХ", actualCalibrator3.getCertificate().getType());
        assertEquals("№M-140726-2", actualCalibrator3.getCertificate().getName());
        assertEquals("16.05.2022", actualCalibrator3.getCertificate().getDate());
        assertEquals("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"", actualCalibrator3.getCertificate().getCompany());
        assertEquals(-83.0, actualCalibrator3.getRangeMin(), 0.0);
        assertEquals(207.0, actualCalibrator3.getRangeMax(), 0.0);
        assertEquals("Тиск", actualCalibrator3.getMeasurementName());
        assertEquals("кПа", actualCalibrator3.getMeasurementValue());
        assertEquals("(convR / 100) * 0.05", actualCalibrator3.getErrorFormula());

        Calibrator actualCalibrator4 = calibratorRepository.get("Fluke 750PD2");
        assertNotNull(actualCalibrator4);
        assertEquals("Fluke 750PD2", actualCalibrator4.getType());
        assertEquals("4043273", actualCalibrator4.getNumber());
        assertEquals("Свідоцтво про перевірку МХ", actualCalibrator4.getCertificate().getType());
        assertEquals("№M-140726-2", actualCalibrator4.getCertificate().getName());
        assertEquals("16.05.2022", actualCalibrator4.getCertificate().getDate());
        assertEquals("ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"", actualCalibrator4.getCertificate().getCompany());
        assertEquals(-7.0, actualCalibrator4.getRangeMin(), 0.0);
        assertEquals(7.0, actualCalibrator4.getRangeMax(), 0.0);
        assertEquals("Тиск", actualCalibrator4.getMeasurementName());
        assertEquals("кПа", actualCalibrator4.getMeasurementValue());
        assertEquals("(convR / 100) * 0.05", actualCalibrator4.getErrorFormula());

        Calibrator actualCalibrator5 = calibratorRepository.get("YAKOGAWA AM012");
        assertNotNull(actualCalibrator5);
        assertEquals("AM012", actualCalibrator5.getType());
        assertEquals("S5T800358", actualCalibrator5.getNumber());
        assertEquals("Сертифікат калібрування", actualCalibrator5.getCertificate().getType());
        assertEquals("№UA/24/200717/265", actualCalibrator5.getCertificate().getName());
        assertEquals("17.07.2020", actualCalibrator5.getCertificate().getDate());
        assertEquals("ДП\"Укрметртестстандарт\"", actualCalibrator5.getCertificate().getCompany());
        assertEquals(0.0, actualCalibrator5.getRangeMin(), 0.0);
        assertEquals(0.0, actualCalibrator5.getRangeMax(), 0.0);
        assertEquals("Витрата", actualCalibrator5.getMeasurementName());
        assertTrue(actualCalibrator5.getMeasurementValue().isEmpty());
        assertEquals("(R / 100) * 0.06", actualCalibrator5.getErrorFormula());

        assertEquals(5, channelRepository.getAll().size());

        Channel actualChannel1 = channelRepository.get("813.000540");
        assertNotNull(actualChannel1);
        assertEquals("Температура підшипників розвантаження млину №32", actualChannel1.getName());
        assertEquals("Температура", actualChannel1.getMeasurementName());
        assertEquals("℃", actualChannel1.getMeasurementValue());
        assertEquals("ДЗФ", actualChannel1.getDepartment());
        assertEquals("ОВДЗ-2", actualChannel1.getArea());
        assertEquals("Секція №3", actualChannel1.getProcess());
        assertEquals("Млин №32", actualChannel1.getInstallation());
        assertEquals("14.12.2021", actualChannel1.getDate());
        assertEquals(2.0, actualChannel1.getFrequency(), 0.0);
        assertEquals("32ТЕ17", actualChannel1.getTechnologyNumber());
        assertEquals("570", actualChannel1.getNumberOfProtocol());
        assertEquals(0.0, actualChannel1.getRangeMin(), 0.0);
        assertEquals(100.0, actualChannel1.getRangeMax(), 0.0);
        assertTrue(actualChannel1.isSuitability());
        assertTrue(actualChannel1.getReference().isEmpty());
        assertEquals(1.5, actualChannel1.getAllowableErrorPercent(), 0.0);
        assertEquals(1.5, actualChannel1.getAllowableErrorValue(), 0.0);

        Channel actualChannel2 = channelRepository.get("813.000211");
        assertNotNull(actualChannel2);
        assertEquals("Регулювання тиску в зоні набору кека фільтра 13510", actualChannel2.getName());
        assertEquals("ЦВО", actualChannel2.getDepartment());
        assertEquals("ЦВО-1", actualChannel2.getArea());
        assertEquals("Технологічна лінія №2", actualChannel2.getProcess());
        assertTrue(actualChannel2.getInstallation().isEmpty());
        assertEquals("Р-013-4", actualChannel2.getTechnologyNumber());
        assertEquals("562", actualChannel2.getNumberOfProtocol());
        assertTrue(actualChannel2.getReference().isEmpty());
        assertEquals("03.12.2021", actualChannel2.getDate());
        assertTrue(actualChannel2.isSuitability());
        assertEquals("Тиск", actualChannel2.getMeasurementName());
        assertEquals("кгс/см²", actualChannel2.getMeasurementValue());
        assertEquals(2.0, actualChannel2.getFrequency(), 0.0);
        assertEquals(-1.0, actualChannel2.getRangeMin(), 0.0);
        assertEquals(0.0, actualChannel2.getRangeMax(), 0.0);
        assertEquals(1.5, actualChannel2.getAllowableErrorPercent(), 0.0);
        assertEquals(0.015, actualChannel2.getAllowableErrorValue(), 0.0);

        Channel actualChannel3 = channelRepository.get("813.000226");
        assertNotNull(actualChannel3);
        assertEquals("Температура у трубі охолоджувача", actualChannel3.getName());
        assertEquals("ЦВО", actualChannel3.getDepartment());
        assertEquals("ЦВО-1", actualChannel3.getArea());
        assertEquals("Технологічна лінія №2", actualChannel3.getProcess());
        assertEquals("Охолоджувач", actualChannel3.getInstallation());
        assertEquals("Т-201-8", actualChannel3.getTechnologyNumber());
        assertEquals("545", actualChannel3.getNumberOfProtocol());
        assertTrue(actualChannel3.getReference().isEmpty());
        assertEquals("03.12.2021", actualChannel3.getDate());
        assertTrue(actualChannel3.isSuitability());
        assertEquals("Температура", actualChannel3.getMeasurementName());
        assertEquals("℃", actualChannel3.getMeasurementValue());
        assertEquals(2.0, actualChannel3.getFrequency(), 0.0);
        assertEquals(0.0, actualChannel3.getRangeMin(), 0.0);
        assertEquals(1250.0, actualChannel3.getRangeMax(), 0.0);
        assertEquals(2.0, actualChannel3.getAllowableErrorPercent(), 0.0);
        assertEquals(25.0, actualChannel3.getAllowableErrorValue(), 0.0);

        Channel actualChannel4 = channelRepository.get("813.000230");
        assertNotNull(actualChannel4);
        assertEquals("Тиск на виході з колектора пилу відпрацьованого газу", actualChannel4.getName());
        assertEquals("ЦВО", actualChannel4.getDepartment());
        assertEquals("ЦВО-1", actualChannel4.getArea());
        assertEquals("Технологічна лінія №2", actualChannel4.getProcess());
        assertTrue(actualChannel4.getInstallation().isEmpty());
        assertEquals("Р-202-4", actualChannel4.getTechnologyNumber());
        assertEquals("514", actualChannel4.getNumberOfProtocol());
        assertTrue(actualChannel4.getReference().isEmpty());
        assertEquals("29.11.2021", actualChannel4.getDate());
        assertTrue(actualChannel4.isSuitability());
        assertEquals("Тиск", actualChannel4.getMeasurementName());
        assertEquals("мм вод ст", actualChannel4.getMeasurementValue());
        assertEquals(2.0, actualChannel4.getFrequency(), 0.0);
        assertEquals(-750.0, actualChannel4.getRangeMin(), 0.0);
        assertEquals(0.0, actualChannel4.getRangeMax(), 0.0);
        assertEquals(1.5, actualChannel4.getAllowableErrorPercent(), 0.0);
        assertEquals(11.25, actualChannel4.getAllowableErrorValue(), 0.0);

        Channel actualChannel5 = channelRepository.get("811.013469");
        assertNotNull(actualChannel5);
        assertEquals("Витрата води в млин №144", actualChannel5.getName());
        assertEquals("ДЗФ", actualChannel5.getDepartment());
        assertEquals("ОВДЗ-4", actualChannel5.getArea());
        assertEquals("Секція №14", actualChannel5.getProcess());
        assertEquals("Млин №144", actualChannel5.getInstallation());
        assertEquals("14FT006", actualChannel5.getTechnologyNumber());
        assertEquals("504", actualChannel5.getNumberOfProtocol());
        assertTrue(actualChannel5.getReference().isEmpty());
        assertEquals("24.11.2021", actualChannel5.getDate());
        assertTrue(actualChannel5.isSuitability());
        assertEquals("Витрата", actualChannel5.getMeasurementName());
        assertEquals("м³/h", actualChannel5.getMeasurementValue());
        assertEquals(2.0, actualChannel5.getFrequency(), 0.0);
        assertEquals(0.0, actualChannel5.getRangeMin(), 0.0);
        assertEquals(50.0, actualChannel5.getRangeMax(), 0.0);
        assertEquals(0.35, actualChannel5.getAllowableErrorPercent(), 0.0);
        assertEquals(0.18, actualChannel5.getAllowableErrorValue(), 0.0);

        assertEquals(2, controlPointsRepository.getAll().size());

        String expectedType1 = "ТСМ-50М";
        double expectedRangeMin1 = 0.0;
        double expectedRangeMax1 = 100.0;
        Map<Double, Double> expectedValues1 = new HashMap<>();
        expectedValues1.put(5.0, 5.14);
        expectedValues1.put(50.0, 50.0);
        expectedValues1.put(95.0, 94.86);
        String controlPointsName1 = ControlPoints.createName(expectedType1, expectedRangeMin1, expectedRangeMax1);
        ControlPoints actualControlPoints1 = controlPointsRepository.get(controlPointsName1);
        assertNotNull(actualControlPoints1);
        assertEquals(expectedType1, actualControlPoints1.getSensorType());
        assertEquals(expectedValues1, actualControlPoints1.getValues());

        String expectedType2 = "ТСМ-50М";
        double expectedRangeMin2 = -50.0;
        double expectedRangeMax2 = 180.0;
        Map<Double, Double> expectedValues2 = new HashMap<>();
        expectedValues2.put(5.0, -39.59);
        expectedValues2.put(50.0, 64.95);
        expectedValues2.put(95.0, 168.22);
        String controlPointsName2 = ControlPoints.createName(expectedType2, expectedRangeMin2, expectedRangeMax2);
        ControlPoints actualControlPoints2 = controlPointsRepository.get(controlPointsName2);
        assertNotNull(actualControlPoints2);
        assertEquals(expectedType2, actualControlPoints2.getSensorType());
        assertEquals(expectedValues2, actualControlPoints2.getValues());

        Collection<Person> actualPersons = personRepository.getAll();
        assertEquals(3, actualPersons.size());

        Person person1 = new Person();
        person1.setName("Петр");
        person1.setSurname("Петров");
        person1.setPatronymic("Петрович");
        person1.setPosition("Інженер");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person1)).count());

        Person person2 = new Person();
        person2.setName("Сидор");
        person2.setSurname("Сидоров");
        person2.setPatronymic("Сидорович");
        person2.setPosition("Начальник дільниці");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person2)).count());

        Person person3 = new Person();
        person3.setName("Василь");
        person3.setSurname("Васильєв");
        person3.setPatronymic("Васильйович");
        person3.setPosition("Слюсар");
        assertEquals(1, actualPersons.stream().filter(p -> p.equalsIgnoreId(person3)).count());

        assertEquals(5, sensorRepository.getAll().size());

        Sensor actualSensor1 = sensorRepository.get("813.000540");
        assertNotNull(actualSensor1);
        assertEquals("ТСМ-50М", actualSensor1.getType());
        assertEquals(-50.0, actualSensor1.getRangeMin(), 0.0);
        assertEquals(180.0, actualSensor1.getRangeMax(), 0.0);
        assertEquals("Температура", actualSensor1.getMeasurementName());
        assertEquals("℃", actualSensor1.getMeasurementValue());
        assertTrue(actualSensor1.getSerialNumber().isEmpty());
        assertEquals("(0.005 * R) + 0.3", actualSensor1.getErrorFormula());

        Sensor actualSensor2 = sensorRepository.get("813.000211");
        assertNotNull(actualSensor2);
        assertEquals("CERABAR M", actualSensor2.getType());
        assertEquals(-1.0, actualSensor2.getRangeMin(), 0.0);
        assertEquals(0.0, actualSensor2.getRangeMax(), 0.0);
        assertEquals("Тиск", actualSensor2.getMeasurementName());
        assertEquals("кгс/см²", actualSensor2.getMeasurementValue());
        assertTrue(actualSensor2.getSerialNumber().isEmpty());
        assertEquals("(convR/100) * 0.15", actualSensor2.getErrorFormula());

        Sensor actualSensor3 = sensorRepository.get("813.000226");
        assertNotNull(actualSensor3);
        assertEquals("Термопара TXA-2388 (тип К)", actualSensor3.getType());
        assertEquals(-50.0, actualSensor3.getRangeMin(), 0.0);
        assertEquals(1250.0, actualSensor3.getRangeMax(), 0.0);
        assertEquals("Температура", actualSensor3.getMeasurementName());
        assertEquals("℃", actualSensor3.getMeasurementValue());
        assertTrue(actualSensor3.getSerialNumber().isEmpty());
        assertEquals("0.0075 * R", actualSensor3.getErrorFormula());

        Sensor actualSensor4 = sensorRepository.get("813.000230");
        assertNotNull(actualSensor4);
        assertEquals("Deltabar S", actualSensor4.getType());
        assertEquals(-750.0, actualSensor4.getRangeMin(), 0.0);
        assertEquals(0.0, actualSensor4.getRangeMax(), 0.0);
        assertEquals("Тиск", actualSensor4.getMeasurementName());
        assertEquals("мм вод ст", actualSensor4.getMeasurementValue());
        assertTrue(actualSensor4.getSerialNumber().isEmpty());
        assertEquals("(convR / 100) * 0.075", actualSensor4.getErrorFormula());

        Sensor actualSensor5 = sensorRepository.get("811.013469");
        assertNotNull(actualSensor5);
        assertEquals("YOKOGAWA AXF050G", actualSensor5.getType());
        assertEquals(0.0, actualSensor5.getRangeMin(), 0.0);
        assertEquals(50.0, actualSensor5.getRangeMax(), 0.0);
        assertEquals("Витрата", actualSensor5.getMeasurementName());
        assertEquals("м³/h", actualSensor5.getMeasurementValue());
        assertEquals("S5V107745 904/M01737 903", actualSensor5.getSerialNumber());
        assertEquals("(R / 100) * 0.35", actualSensor5.getErrorFormula());

        assertEquals(6, measurementFactorRepository.getAll().size());

        MeasurementTransformFactor actualMtf1 = measurementFactorRepository.getBySource("кПа").stream()
                .filter(mtf -> mtf.getTransformTo().equals("мм вод ст"))
                .findAny().orElse(null);
        assertNotNull(actualMtf1);
        assertEquals(101.9716, actualMtf1.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf2 = measurementFactorRepository.getBySource("кПа").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кгс/см²"))
                .findAny().orElse(null);
        assertNotNull(actualMtf2);
        assertEquals(0.01019716, actualMtf2.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf3 = measurementFactorRepository.getBySource("мм вод ст").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кгс/см²"))
                .findAny().orElse(null);
        assertNotNull(actualMtf3);
        assertEquals(1.0E-4, actualMtf3.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf4 = measurementFactorRepository.getBySource("мм вод ст").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кПа"))
                .findAny().orElse(null);
        assertNotNull(actualMtf4);
        assertEquals(0.00980775, actualMtf4.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf5 = measurementFactorRepository.getBySource("кгс/см²").stream()
                .filter(mtf -> mtf.getTransformTo().equals("мм вод ст"))
                .findAny().orElse(null);
        assertNotNull(actualMtf5);
        assertEquals(10000.0, actualMtf5.getTransformFactor(), 0.0);

        MeasurementTransformFactor actualMtf6 = measurementFactorRepository.getBySource("кгс/см²").stream()
                .filter(mtf -> mtf.getTransformTo().equals("кПа"))
                .findAny().orElse(null);
        assertNotNull(actualMtf6);
        assertEquals(98.0665, actualMtf6.getTransformFactor(), 0.0);
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
                    "  \"company\" : \"ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"\",\n" +
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
                    "  \"company\" : \"ДП\"ХарківСтандартМетрологія\"\",\n" +
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
                    "  \"company\" : \"ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"\",\n" +
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
                    "  \"company\" : \"ДП\"ПОЛТАВАСТАНДАРТМЕТРОЛОГІЯ\"\",\n" +
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
                    "  \"company\" : \"ДП\"Укрметртестстандарт\"\",\n" +
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