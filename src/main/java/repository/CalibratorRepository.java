package repository;

import model.Calibrator;
import model.Measurement;

import java.util.Collection;

public interface CalibratorRepository extends Repository<Calibrator>{
    String[]getAllNames(Measurement measurement);

    Calibrator get(String name);

    boolean removeByMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean importData(Collection<Calibrator> newCalibrators, Collection<Calibrator>calibratorsForChange);

    boolean isExists(Calibrator calibrator);
}
