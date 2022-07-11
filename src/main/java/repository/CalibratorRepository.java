package repository;

import model.Calibrator;
import model.Measurement;

import java.util.List;

public interface CalibratorRepository {
    List<Calibrator> getAll();
    String[]getAllNames(Measurement measurement);

    Calibrator get(String name);

    boolean add(Calibrator calibrator);

    boolean remove(String name);
    boolean removeByMeasurementValue(String measurementValue);

    boolean set(Calibrator oldCalibrator, Calibrator newCalibrator);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean clear();

    boolean rewrite(List<Calibrator>calibrators);
    boolean importData(List<Calibrator>newCalibrators, List<Calibrator>calibratorsForChange);

    boolean isExists(Calibrator calibrator);
}
