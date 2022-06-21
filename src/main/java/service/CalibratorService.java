package service;

import model.Calibrator;
import model.Measurement;

import java.util.ArrayList;

public interface CalibratorService {
    ArrayList<Calibrator> getAll();
    String[] getAllNames(Measurement measurement);

    boolean add(Calibrator calibrator);

    boolean remove(String name);
    boolean removeByMeasurementValue(String measurementValue);

    boolean set(Calibrator oldCalibrator, Calibrator newCalibrator);
    boolean changeMeasurementValue(String oldValue, String newValue);

    Calibrator get(String name);

    boolean clear();

    boolean importData(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange);
    boolean rewrite(ArrayList<Calibrator>calibrators);
    boolean resetToDefault();

    boolean isExists(Calibrator calibrator);
}
