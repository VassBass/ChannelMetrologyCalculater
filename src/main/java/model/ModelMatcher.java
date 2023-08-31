package model;

import model.dto.*;
import util.NotNullObject;

import java.util.Map;
import java.util.Set;

public class ModelMatcher {
    public static boolean match(Calibrator c1, Calibrator c2) {
        return c1.getName().equals(c2.getName()) &&
                c1.getType().equals(c2.getType()) &&
                c1.getCertificate().equals(c2.getCertificate()) &&
                c1.getNumber().equals(c2.getNumber()) &&
                c1.getRangeMin() == c2.getRangeMin() &&
                c1.getRangeMax() == c2.getRangeMax() &&
                c1.getMeasurementName().equals(c2.getMeasurementName()) &&
                c1.getMeasurementValue().equals(c2.getMeasurementValue()) &&
                c1.getErrorFormula().equals(c2.getErrorFormula());
    }

    public static boolean match(Channel c1, Channel c2) {
        return c1.getCode().equals(c2.getCode()) &&
                c1.getName().equals(c2.getName()) &&
                NotNullObject.get(c1.getMeasurementName()).equals(NotNullObject.get(c2.getMeasurementName())) &&
                NotNullObject.get(c1.getMeasurementValue()).equals(NotNullObject.get(c2.getMeasurementValue())) &&
                NotNullObject.get(c1.getDepartment()).equals(NotNullObject.get(c2.getDepartment())) &&
                NotNullObject.get(c1.getArea()).equals(NotNullObject.get(c2.getArea())) &&
                NotNullObject.get(c1.getProcess()).equals(NotNullObject.get(c2.getProcess())) &&
                NotNullObject.get(c1.getInstallation()).equals(NotNullObject.get(c2.getInstallation())) &&
                c1.getDate().equals(c2.getDate()) &&
                NotNullObject.get(c1.getTechnologyNumber()).equals(NotNullObject.get(c2.getTechnologyNumber())) &&
                NotNullObject.get(c1.getNumberOfProtocol()).equals(NotNullObject.get(c2.getNumberOfProtocol())) &&
                c1.getFrequency() == c2.getFrequency() &&
                c1.getRangeMin() == c2.getRangeMin() &&
                c1.getRangeMax() == c2.getRangeMax() &&
                NotNullObject.get(c1.getReference()).equals(NotNullObject.get(c2.getReference())) &&
                c1.getAllowableErrorPercent() == c2.getAllowableErrorPercent() &&
                c1.getAllowableErrorValue() == c2.getAllowableErrorValue() &&
                c1.isSuitability() == c2.isSuitability();
    }

    public static boolean match(ControlPoints c1, ControlPoints c2) {
        Set<Map.Entry<Double, Double>> entries1 = c1.getValues().entrySet();
        Set<Map.Entry<Double, Double>> entries2 = c2.getValues().entrySet();

        return NotNullObject.get(c1.getName()).equals(NotNullObject.get(c2.getName())) &&
                NotNullObject.get(c1.getSensorType()).equals(NotNullObject.get(c2.getSensorType())) &&
                entries1.size() == entries2.size() &&
                entries1.containsAll(entries2);
    }

    public static boolean match(Measurement m1, Measurement m2) {
        return m1.getValue().equals(m2.getValue()) &&
                m1.getName().equals(m2.getName());
    }

    public static boolean match(MeasurementTransformFactor m1, MeasurementTransformFactor m2) {
        return m1.getId() == m2.getId() &&
                NotNullObject.get(m1.getTransformFrom()).equals(NotNullObject.get(m2.getTransformFrom())) &&
                NotNullObject.get(m1.getTransformTo()).equals(NotNullObject.get(m2.getTransformTo())) &&
                m1.getTransformFactor() == m2.getTransformFactor();
    }

    public static boolean match(Person p1, Person p2) {
        return p1.getId() == p2.getId() &&
                NotNullObject.get(p1.getSurname()).equals(NotNullObject.get(p2.getSurname())) &&
                NotNullObject.get(p1.getName()).equals(NotNullObject.get(p2.getName())) &&
                NotNullObject.get(p1.getPatronymic()).equals(NotNullObject.get(p2.getPatronymic())) &&
                NotNullObject.get(p1.getPosition()).equals(NotNullObject.get(p2.getPosition()));
    }

    public static boolean match(Sensor s1, Sensor s2) {
        return NotNullObject.get(s1.getChannelCode()).equals(NotNullObject.get(s2.getChannelCode())) &&
                NotNullObject.get(s1.getType()).equals(NotNullObject.get(s2.getType())) &&
                NotNullObject.get(s1.getSerialNumber()).equals(NotNullObject.get(s2.getSerialNumber())) &&
                NotNullObject.get(s1.getMeasurementValue()).equals(NotNullObject.get(s2.getMeasurementValue())) &&
                NotNullObject.get(s1.getMeasurementName()).equals(NotNullObject.get(s2.getMeasurementName())) &&
                NotNullObject.get(s1.getErrorFormula()).equals(NotNullObject.get(s2.getErrorFormula())) &&
                s1.getRangeMin() == s2.getRangeMin() &&
                s1.getRangeMax() == s2.getRangeMax();
    }

    public static boolean match(SensorError s1, SensorError s2) {
        return NotNullObject.get(s1.getId()).equals(NotNullObject.get(s2.getId())) &&
                NotNullObject.get(s1.getType()).equals(NotNullObject.get(s2.getType())) &&
                NotNullObject.get(s1.getErrorFormula()).equals(NotNullObject.get(s2.getErrorFormula())) &&
                NotNullObject.get(s1.getMeasurementValue()).equals(NotNullObject.get(s2.getMeasurementValue())) &&
                s1.getRangeMin() == s2.getRangeMin() &&
                s1.getRangeMax() == s2.getRangeMax();
    }
}
