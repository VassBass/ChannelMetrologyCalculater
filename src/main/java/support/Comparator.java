package support;

import constants.MeasurementConstants;
import converters.VariableConverter;
import measurements.Measurement;

import java.util.Calendar;

public class Comparator {

    public static boolean sensorsMatch(Sensor sensor1, Sensor sensor2){
        if (sensor1.getMeasurement().equals(sensor2.getMeasurement())){
            if (sensor1.getMeasurement().equals(MeasurementConstants.TEMPERATURE.getValue())) {
                return sensor1.getName().equals(sensor2.getName()) &&
                        sensor1.getType().equals(sensor2.getType()) &&
                        sensor1.getRangeMin() == sensor2.getRangeMin() &&
                        sensor1.getRangeMax() == sensor2.getRangeMax() &&
                        sensor1.getNumber().equals(sensor2.getNumber()) &&
                        sensor1.getValue().equals(sensor2.getValue()) &&
                        sensor1.getErrorFormula().equals(sensor2.getErrorFormula());
            } else if (sensor1.getMeasurement().equals(MeasurementConstants.PRESSURE.getValue())) {
                return sensor1.getName().equals(sensor2.getName()) &&
                        sensor1.getType().equals(sensor2.getType()) &&
                        sensor1.getNumber().equals(sensor2.getNumber()) &&
                        sensor1.getErrorFormula().equals(sensor2.getErrorFormula());
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    public static boolean calibratorsMatch(Calibrator calibrator1, Calibrator calibrator2){
        return calibrator1.getName().equals(calibrator2.getName()) &&
                calibrator1.getType().equals(calibrator2.getType()) &&
                calibrator1.getNumber().equals(calibrator2.getNumber()) &&
                calibrator1.getMeasurement().equals(calibrator2.getMeasurement()) &&
                calibrator1.getCertificate().equals(calibrator2.getCertificate()) &&
                calibrator1.getRangeMin() == calibrator2.getRangeMin() &&
                calibrator1.getRangeMax() == calibrator2.getRangeMax() &&
                calibrator1.getValue().equals(calibrator2.getValue()) &&
                calibrator1.getErrorFormula().equals(calibrator2.getErrorFormula());
    }

    public static boolean channelsMatch(Channel channel1, Channel channel2){
        return channel1.getCode().equals(channel2.getCode())
                && channel1.getName().equals(channel2.getName())
                && measurementsMatch(channel1.getMeasurement(), channel2.getMeasurement())
                && channel1.getDepartment().equals(channel2.getDepartment())
                && channel1.getArea().equals(channel2.getArea())
                && channel1.getProcess().equals(channel2.getProcess())
                && channel1.getInstallation().equals(channel2.getInstallation())
                && datesMatch(channel1.getDate(), channel2.getDate())
                && channel1.getFrequency() == channel2.getFrequency()
                && channel1.getTechnologyNumber().equals(channel2.getTechnologyNumber())
                && sensorsMatch(channel1.getSensor(), channel2.getSensor())
                && channel1.getNumberOfProtocol().equals(channel2.getNumberOfProtocol())
                && channel1.getReference().equals(channel2.getReference())
                && channel1.getRangeMin() == channel2.getRangeMin()
                && channel1.getRangeMax() == channel2.getRangeMax()
                && channel1.getAllowableErrorPercent() == channel2.getAllowableErrorPercent()
                && channel1.getAllowableError() == channel2.getAllowableError()
                && channel1.isGood == channel2.isGood;
    }

    public static boolean measurementsMatch(Measurement measurement1, Measurement measurement2){
        return measurement1.getNameConstant() == measurement2.getNameConstant() &&
                measurement1.getValueConstant() == measurement2.getValueConstant();
    }

    public static boolean datesMatch(Calendar date1, Calendar date2){
        return VariableConverter.dateToString(date1).equals(VariableConverter.dateToString(date2));
    }
}
