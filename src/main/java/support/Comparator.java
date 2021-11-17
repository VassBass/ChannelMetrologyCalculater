package support;

import constants.MeasurementConstants;

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
}
