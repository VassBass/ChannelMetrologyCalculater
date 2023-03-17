package service.importer.updater.from_v5.to_v6;

import model.dto.*;
import model.dto.builder.CalibratorBuilder;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.ControlPointsBuilder;
import model.dto.builder.SensorBuilder;
import service.importer.JsonParser;
import service.importer.model.Model;
import service.importer.model.ModelHolder;
import service.importer.Transformer;
import util.StringHelper;

import javax.annotation.Nullable;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static service.importer.model.ModelField.*;

public class Transformer_v6 implements Transformer {
    private static volatile Transformer_v6 instance;
    private Transformer_v6(){}
    public static Transformer_v6 getInstance() {
        if (instance == null) {
            synchronized (Transformer_v6.class) {
                if (instance == null) {
                    instance = new Transformer_v6();
                }
            }
        }
        return instance;
    }

    private final JsonParser jsonParser = JsonParser_v5.getInstance();

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T transform(ModelHolder source, Class<T> result) {
        if (result.isAssignableFrom(Sensor.class)) return (T) transformToSensor(source);
        if (result.isAssignableFrom(Channel.class)) return (T) transformToChannel(source);
        if (result.isAssignableFrom(Calibrator.class)) return (T) transformToCalibrator(source);
        if (result.isAssignableFrom(Calibrator.Certificate.class)) return (T) transformToCalibratorCertificate(source);
        if (result.isAssignableFrom(ControlPoints.class)) return (T) transformToControlPoints(source);
        if (result.isAssignableFrom(Person.class)) return (T) transformToPerson(source);

        return null;
    }

    /**
     * Transform source object to Sensor model of version 6.0 (without channelCode field)
     * @param source object of Sensor model version 5.4
     * @return Sensor model of version 6.0 (without channelCode field)
     * returns null if source object are not valid
     * @see Sensor
     * @see JsonParser_v5#parseSensor(String)
     */
    private Sensor transformToSensor(ModelHolder source) {
        if (source.getModel() != Model.SENSOR || Sensor.serialVersionUID != 6L) return null;

        String type = source.getValue(SENSOR_TYPE);
        String serialNumber = source.getValue(SENSOR_SERIAL_NUMBER);
        String measurementName = source.getValue(SENSOR_MEASUREMENT_NAME);
        String measurementValue = source.getValue(SENSOR_MEASUREMENT_VALUE);
        String rangeMin = source.getValue(SENSOR_RANGE_MIN);
        String rangeMax = source.getValue(SENSOR_RANGE_MAX);
        String errorFormula = source.getValue(SENSOR_ERROR_FORMULA);

        return new SensorBuilder()
                .setType(type == null ? EMPTY : type)
                .setSerialNumber(serialNumber == null ? EMPTY : serialNumber)
                .setMeasurementName(measurementName == null ? EMPTY : measurementName)
                .setMeasurementValue(measurementValue == null ? EMPTY : measurementValue)
                .setRangeMin(StringHelper.isDouble(rangeMin) ? Double.parseDouble(rangeMin) : 0D)
                .setRangeMax(StringHelper.isDouble(rangeMax) ? Double.parseDouble(rangeMax) : 100D)
                .setErrorFormula(errorFormula == null ? EMPTY : errorFormula)
                .build();
    }

    /**
     * Transform source object to Channel model of version 6.0 (without measurementName field)
     * @param source object of Channel model version 5.4
     * @return Channel model of version 6.0 (without measurementName field)
     * returns null if source object are not valid
     * @see Channel
     * @see SqliteReaderOfv5#appendChannels(List, Statement)
     */
    private Channel transformToChannel(ModelHolder source) {
        if (source.getModel() != Model.CHANNEL || Channel.serialVersionUID != 6L) return null;

        String code = source.getValue(CHANNEL_CODE);
        String name = source.getValue(CHANNEL_NAME);
        String measurementValue = source.getValue(CHANNEL_MEASUREMENT_VALUE);
        String department = source.getValue(CHANNEL_DEPARTMENT);
        String area = source.getValue(CHANNEL_AREA);
        String process = source.getValue(CHANNEL_PROCESS);
        String installation = source.getValue(CHANNEL_INSTALLATION);
        String date = source.getValue(CHANNEL_DATE);
        String frequency = source.getValue(CHANNEL_FREQUENCY);
        String technologyNumber = source.getValue(CHANNEL_TECHNOLOGY_NUMBER);
        String protocolNumber = source.getValue(CHANNEL_PROTOCOL_NUMBER);
        String rangeMin = source.getValue(CHANNEL_RANGE_MIN);
        String rangeMax = source.getValue(CHANNEL_RANGE_MAX);
        String reference = source.getValue(CHANNEL_REFERENCE);
        String suitability = source.getValue(CHANNEL_SUITABILITY);
        String allowableErrorPercent = source.getValue(CHANNEL_ALLOWABLE_ERROR_PERCENT);
        String allowableErrorValue = source.getValue(CHANNEL_ALLOWABLE_ERROR_VALUE);

        return new ChannelBuilder(code == null ? EMPTY : code)
                .setName(name == null ? EMPTY : name)
                .setMeasurementValue(measurementValue == null ? EMPTY : measurementValue)
                .setDepartment(department == null ? EMPTY : department)
                .setArea(area == null ? EMPTY : area)
                .setProcess(process == null ? EMPTY : process)
                .setInstallation(installation == null ? EMPTY : installation)
                .setDate(date == null ? EMPTY : date)
                .setFrequency(StringHelper.isDouble(frequency) ? Double.parseDouble(frequency) : 2.0)
                .setTechnologyNumber(technologyNumber == null ? EMPTY : technologyNumber)
                .setNumberOfProtocol(protocolNumber == null ? EMPTY : protocolNumber)
                .setRangeMin(StringHelper.isDouble(rangeMin) ? Double.parseDouble(rangeMin) : 0.0)
                .setRangeMax(StringHelper.isDouble(rangeMax) ? Double.parseDouble(rangeMax) : 100.0)
                .setReference(reference == null ? EMPTY : reference)
                .setSuitability(Boolean.parseBoolean(suitability))
                .setAllowableErrorInPercent(StringHelper.isDouble(allowableErrorPercent) ? Double.parseDouble(allowableErrorPercent) : 0.0)
                .setAllowableErrorInValue(StringHelper.isDouble(allowableErrorValue) ? Double.parseDouble(allowableErrorValue) : 0.0)
                .build();
    }

    /**
     * Transform source object to Calibrator model of version 6.0
     * @param source object of Calibrator model version 5.4
     * @return Calibrator model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator
     * @see SqliteReaderOfv5#appendCalibrators(List, Statement)
     */
    private Calibrator transformToCalibrator(ModelHolder source) {
        if (source.getModel() != Model.CALIBRATOR || Calibrator.serialVersionUID != 6L) return null;

        String name = source.getValue(CALIBRATOR_NAME);
        String type = source.getValue(CALIBRATOR_TYPE);
        String number = source.getValue(CALIBRATOR_NUMBER);
        String rangeMin = source.getValue(CALIBRATOR_RANGE_MIN);
        String rangeMax = source.getValue(CALIBRATOR_RANGE_MAX);
        String measurementName = source.getValue(CALIBRATOR_MEASUREMENT_NAME);
        String measurementValue = source.getValue(CALIBRATOR_MEASUREMENT_VALUE);
        String errorFormula = source.getValue(CALIBRATOR_ERROR_FORMULA);

        String certificateJson = source.getValue(CALIBRATOR_CERTIFICATE);
        ModelHolder certificateModelHolder = jsonParser.parse(certificateJson, Model.CALIBRATOR_CERTIFICATE);
        Calibrator.Certificate certificate = transformToCalibratorCertificate(certificateModelHolder);

        return new CalibratorBuilder()
                .setName(name == null ? EMPTY : name)
                .setType(type == null ? EMPTY : type)
                .setNumber(number == null ? EMPTY : number)
                .setCertificate(certificate == null ? new Calibrator.Certificate() : certificate)
                .setRangeMin(StringHelper.isDouble(rangeMin) ? Double.parseDouble(rangeMin) : 0.0)
                .setRangeMax(StringHelper.isDouble(rangeMax) ? Double.parseDouble(rangeMax) : 100.0)
                .setMeasurementName(measurementName == null ? EMPTY : measurementName)
                .setMeasurementValue(measurementValue == null ? EMPTY : measurementValue)
                .setErrorFormula(errorFormula == null ? EMPTY : errorFormula)
                .build();
    }

    /**
     * Transform source object to Calibrator.Certificate model of version 6.0
     * @param source object of Calibrator.Certificate model version 5.4
     * @return Calibrator.Certificate model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator.Certificate
     * @see SqliteReaderOfv5#appendCalibrators(List, Statement)
     * @see JsonParser_v5#parseCalibratorCertificate(String)
     */
    private Calibrator.Certificate transformToCalibratorCertificate(ModelHolder source) {
        if (source == null || source.getModel() != Model.CALIBRATOR_CERTIFICATE || Calibrator.Certificate.serialVersionUID != 6L) return null;

        String name = source.getValue(CALIBRATOR_CERTIFICATE_NAME);
        String type = source.getValue(CALIBRATOR_CERTIFICATE_TYPE);
        String company = source.getValue(CALIBRATOR_CERTIFICATE_COMPANY);
        String date = source.getValue(CALIBRATOR_CERTIFICATE_DATE);

        return new Calibrator.Certificate.CertificateBuilder()
                .setName(name == null ? EMPTY : name)
                .setType(type == null ? EMPTY : type)
                .setCompany(company == null ? EMPTY : company)
                .setDate(date == null ? EMPTY : date)
                .build();
    }

    /**
     * Transform source object to Calibrator.Certificate model of version 6.0
     * @param source object of Calibrator.Certificate model version 5.4
     * @return Calibrator.Certificate model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator.Certificate
     * @see SqliteReaderOfv5#appendCalibrators(List, Statement)
     * @see JsonParser_v5#parseCalibratorCertificate(String)
     */
    private ControlPoints transformToControlPoints(ModelHolder source) {
        if (source == null || source.getModel() != Model.CONTROL_POINTS || ControlPoints.serialVersionUID != 6L) return null;

        String sensorType = source.getValue(CONTROL_POINTS_SENSOR_TYPE);
        String rangeMin = source.getValue(CONTROL_POINTS_RANGE_MIN);
        String rangeMax = source.getValue(CONTROL_POINTS_RANGE_MAX);
        String name = ControlPoints.createName(
                sensorType,
                StringHelper.isDouble(rangeMin) ? Double.parseDouble(rangeMin) : 0.0,
                StringHelper.isDouble(rangeMax) ? Double.parseDouble(rangeMax) : 100.0
        );

        String[] vals = source.getValue(CONTROL_POINTS_VALUES).split("\\|");
        Map<Double, Double> values = new HashMap<>();
        if (vals.length == 5) {
            values.put(5D, StringHelper.isDouble(vals[1]) ? Double.parseDouble(vals[1]) : 5D);
            values.put(50D, StringHelper.isDouble(vals[2]) ? Double.parseDouble(vals[2]) : 50D);
            values.put(95D, StringHelper.isDouble(vals[3]) ? Double.parseDouble(vals[3]) : 95D);
        } else {
            values.put(5D, 5D);
            values.put(50D, 50D);
            values.put(95D, 95D);
        }

        return new ControlPointsBuilder(name)
                .setSensorType(sensorType)
                .setPoints(values)
                .build();
    }

    /**
     * Transform source object to Person model of version 6.0 (without id field)
     * @param source object of Person model version 5.4
     * @return Person model of version 6.0 (without id field)
     * returns null if source object are not valid
     * @see model.dto.Person
     * @see SqliteReaderOfv5#appendPersons(List, Statement)
     */
    private Person transformToPerson(ModelHolder source) {
        if (source == null || source.getModel() != Model.PERSON || ControlPoints.serialVersionUID != 6L) return null;

        Person person = new Person();
        person.setName(source.getValue(PERSON_NAME));
        person.setSurname(source.getValue(PERSON_SURNAME));
        person.setPatronymic(source.getValue(PERSON_PATRONYMIC));
        person.setPosition(source.getValue(PERSON_POSITION));

        return person;
    }
}
