package service.importer.updater.from_v5_4.to_v6_0;

import model.dto.*;
import model.dto.builder.CalibratorBuilder;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.ControlPointsBuilder;
import model.dto.builder.SensorBuilder;
import service.importer.JsonParser;
import service.importer.Model;
import service.importer.ModelHolder;
import service.importer.Transformer;

import javax.annotation.Nullable;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.importer.ModelField.*;

public class Transformer_v6_0 implements Transformer {
    private static volatile Transformer_v6_0 instance;
    private Transformer_v6_0(){}
    public static Transformer_v6_0 getInstance() {
        if (instance == null) {
            synchronized (Transformer_v6_0.class) {
                if (instance == null) {
                    instance = new Transformer_v6_0();
                }
            }
        }
        return instance;
    }

    private final JsonParser jsonParser = JsonParser_v5_4.getInstance();

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
     * @see JsonParser_v5_4#parseSensor(String)
     */
    private Sensor transformToSensor(ModelHolder source) {
        if (source.getModel() != Model.SENSOR || Sensor.serialVersionUID != 6L) return null;

        return new SensorBuilder()
                .setType(source.getValue(SENSOR_TYPE))
                .setSerialNumber(source.getValue(SENSOR_SERIAL_NUMBER))
                .setMeasurementName(source.getValue(SENSOR_MEASUREMENT_NAME))
                .setMeasurementValue(source.getValue(SENSOR_MEASUREMENT_VALUE))
                .setRangeMin(Double.parseDouble(source.getValue(SENSOR_RANGE_MIN)))
                .setRangeMax(Double.parseDouble(source.getValue(SENSOR_RANGE_MAX)))
                .setErrorFormula(source.getValue(SENSOR_ERROR_FORMULA))
                .build();
    }

    /**
     * Transform source object to Channel model of version 6.0 (without measurementName field)
     * @param source object of Channel model version 5.4
     * @return Channel model of version 6.0 (without measurementName field)
     * returns null if source object are not valid
     * @see Channel
     * @see SqliteReaderOfv5_4#appendChannels(List, Statement)
     */
    private Channel transformToChannel(ModelHolder source) {
        if (source.getModel() != Model.CHANNEL || Channel.serialVersionUID != 6L) return null;

        return new ChannelBuilder(source.getValue(CHANNEL_CODE))
                .setName(source.getValue(CHANNEL_NAME))
                .setMeasurementValue(source.getValue(CHANNEL_MEASUREMENT_VALUE))
                .setDepartment(source.getValue(CHANNEL_DEPARTMENT))
                .setArea(source.getValue(CHANNEL_AREA))
                .setProcess(source.getValue(CHANNEL_PROCESS))
                .setInstallation(source.getValue(CHANNEL_INSTALLATION))
                .setDate(source.getValue(CHANNEL_DATE))
                .setFrequency(Double.parseDouble(source.getValue(CHANNEL_FREQUENCY)))
                .setTechnologyNumber(source.getValue(CHANNEL_TECHNOLOGY_NUMBER))
                .setNumberOfProtocol(source.getValue(CHANNEL_PROTOCOL_NUMBER))
                .setRangeMin(Double.parseDouble(source.getValue(CHANNEL_RANGE_MIN)))
                .setRangeMax(Double.parseDouble(source.getValue(CHANNEL_RANGE_MAX)))
                .setReference(source.getValue(CHANNEL_REFERENCE))
                .setSuitability(Boolean.parseBoolean(source.getValue(CHANNEL_SUITABILITY)))
                .setAllowableErrorInPercent(Double.parseDouble(source.getValue(CHANNEL_ALLOWABLE_ERROR_PERCENT)))
                .setAllowableErrorInValue(Double.parseDouble(source.getValue(CHANNEL_ALLOWABLE_ERROR_VALUE)))
                .build();
    }

    /**
     * Transform source object to Calibrator model of version 6.0
     * @param source object of Calibrator model version 5.4
     * @return Calibrator model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator
     * @see SqliteReaderOfv5_4#appendCalibrators(List, Statement)
     */
    private Calibrator transformToCalibrator(ModelHolder source) {
        if (source.getModel() != Model.CALIBRATOR || Calibrator.serialVersionUID != 6L) return null;

        String certificateJson = source.getValue(CALIBRATOR_CERTIFICATE);
        ModelHolder certificateModelHolder = jsonParser.parse(certificateJson, Model.CALIBRATOR_CERTIFICATE);
        Calibrator.Certificate certificate = transformToCalibratorCertificate(certificateModelHolder);

        return new CalibratorBuilder()
                .setName(source.getValue(CALIBRATOR_NAME))
                .setType(source.getValue(CALIBRATOR_TYPE))
                .setNumber(source.getValue(CALIBRATOR_NUMBER))
                .setCertificate(certificate == null ? new Calibrator.Certificate() : certificate)
                .setRangeMin(Double.parseDouble(source.getValue(CALIBRATOR_RANGE_MIN)))
                .setRangeMax(Double.parseDouble(source.getValue(CALIBRATOR_RANGE_MAX)))
                .setMeasurementName(source.getValue(CALIBRATOR_MEASUREMENT_NAME))
                .setMeasurementValue(source.getValue(CALIBRATOR_MEASUREMENT_VALUE))
                .setErrorFormula(source.getValue(CALIBRATOR_ERROR_FORMULA))
                .build();
    }

    /**
     * Transform source object to Calibrator.Certificate model of version 6.0
     * @param source object of Calibrator.Certificate model version 5.4
     * @return Calibrator.Certificate model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator.Certificate
     * @see SqliteReaderOfv5_4#appendCalibrators(List, Statement)
     * @see JsonParser_v5_4#parseCalibratorCertificate(String)
     */
    private Calibrator.Certificate transformToCalibratorCertificate(ModelHolder source) {
        if (source == null || source.getModel() != Model.CALIBRATOR_CERTIFICATE || Calibrator.Certificate.serialVersionUID != 6L) return null;

        return new Calibrator.Certificate.CertificateBuilder()
                .setName(source.getValue(CALIBRATOR_CERTIFICATE_NAME))
                .setType(source.getValue(CALIBRATOR_CERTIFICATE_TYPE))
                .setCompany(source.getValue(CALIBRATOR_CERTIFICATE_COMPANY))
                .setDate(source.getValue(CALIBRATOR_CERTIFICATE_DATE))
                .build();
    }

    /**
     * Transform source object to Calibrator.Certificate model of version 6.0
     * @param source object of Calibrator.Certificate model version 5.4
     * @return Calibrator.Certificate model of version 6.0
     * returns null if source object are not valid
     * @see Calibrator.Certificate
     * @see SqliteReaderOfv5_4#appendCalibrators(List, Statement)
     * @see JsonParser_v5_4#parseCalibratorCertificate(String)
     */
    private ControlPoints transformToControlPoints(ModelHolder source) {
        if (source == null || source.getModel() != Model.CONTROL_POINTS || ControlPoints.serialVersionUID != 6L) return null;
        String sensorType = source.getValue(CONTROL_POINTS_SENSOR_TYPE);
        double rangeMin = Double.parseDouble(source.getValue(CONTROL_POINTS_RANGE_MIN));
        double rangeMax = Double.parseDouble(source.getValue(CONTROL_POINTS_RANGE_MAX));
        String name = ControlPoints.createName(sensorType, rangeMin, rangeMax);
        String[] vals = source.getValue(CONTROL_POINTS_VALUES).split("\\|");
        if (vals.length == 5) {
            Map<Double, Double> values = new HashMap<>();
            values.put(5D, Double.parseDouble(vals[1]));
            values.put(50D, Double.parseDouble(vals[2]));
            values.put(95D, Double.parseDouble(vals[3]));

            return new ControlPointsBuilder(name)
                    .setSensorType(sensorType)
                    .setPoints(values)
                    .build();
        }

        return null;
    }

    /**
     * Transform source object to Person model of version 6.0 (without id field)
     * @param source object of Person model version 5.4
     * @return Person model of version 6.0 (without id field)
     * returns null if source object are not valid
     * @see model.dto.Person
     * @see SqliteReaderOfv5_4#appendPersons(List, Statement)
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
