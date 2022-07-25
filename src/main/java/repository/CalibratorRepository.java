package repository;

import model.Calibrator;
import model.Measurement;

import java.util.List;

public interface CalibratorRepository extends Repository<Calibrator>{
    String[]getAllNames(Measurement measurement);

    Calibrator get(String name);

    boolean removeByMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean importData(List<Calibrator>newCalibrators, List<Calibrator>calibratorsForChange);

    boolean isExists(Calibrator calibrator);
}
