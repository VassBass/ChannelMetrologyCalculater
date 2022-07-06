package repository;

import model.Calibrator;
import model.Measurement;

import java.util.ArrayList;

public interface CalibratorRepository {
    ArrayList<Calibrator> getAll();
    String[]getAllNames(Measurement measurement);

    Calibrator get(String name);

    boolean add(Calibrator calibrator);

    boolean remove(String name);
    boolean removeByMeasurementValue(String measurementValue);

    boolean set(Calibrator oldCalibrator, Calibrator newCalibrator);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean clear();

    boolean rewrite(ArrayList<Calibrator>calibrators);
    boolean importData(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange);

    boolean isExists(Calibrator calibrator);
}
