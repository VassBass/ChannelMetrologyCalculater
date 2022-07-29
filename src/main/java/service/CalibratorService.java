package service;

import model.Calibrator;
import model.Measurement;

import java.util.Collection;

public interface CalibratorService extends Service<Calibrator> {
    String[] getAllNames(Measurement measurement);

    boolean removeByMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    Calibrator get(String name);

    boolean importData(Collection<Calibrator> newCalibrators, Collection<Calibrator>calibratorsForChange);
    boolean resetToDefault();

    boolean isExists(Calibrator calibrator);
}
