package repository;

import model.Calibrator;
import model.Measurement;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface CalibratorRepository extends Repository<Calibrator>{
    String[]getAllNames(@Nonnull Measurement measurement);

    Calibrator get(String name);

    boolean removeByMeasurementValue(@Nonnull String measurementValue);

    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);

    boolean importData(@Nonnull Collection<Calibrator> newCalibrators,
                       @Nonnull Collection<Calibrator>calibratorsForChange);

    boolean isExists(@Nonnull Calibrator calibrator);
}
