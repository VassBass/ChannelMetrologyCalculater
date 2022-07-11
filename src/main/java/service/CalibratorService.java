package service;

import model.Calibrator;
import model.Measurement;

import java.util.List;

public interface CalibratorService {
    List<Calibrator> getAll();
    String[] getAllNames(Measurement measurement);

    boolean add(Calibrator calibrator);

    boolean remove(String name);
    boolean removeByMeasurementValue(String measurementValue);

    boolean set(Calibrator oldCalibrator, Calibrator newCalibrator);
    boolean changeMeasurementValue(String oldValue, String newValue);

    Calibrator get(String name);

    boolean clear();

    boolean importData(List<Calibrator>newCalibrators, List<Calibrator>calibratorsForChange);
    boolean rewrite(List<Calibrator>calibrators);
    boolean resetToDefault();

    boolean isExists(Calibrator calibrator);
}
