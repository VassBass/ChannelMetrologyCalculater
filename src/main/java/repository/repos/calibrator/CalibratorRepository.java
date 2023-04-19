package repository.repos.calibrator;

import model.dto.Calibrator;
import model.dto.Measurement;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface CalibratorRepository {
    Collection<Calibrator> getAll();
    String[]getAllNamesByMeasurementName(@Nonnull String measurementName);
    Calibrator get(String name);

    boolean add(@Nonnull Calibrator calibrator);

    boolean removeByName(@Nonnull String name);
    boolean removeByMeasurementValue(@Nonnull String value);
    boolean clear();

    boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Calibrator> calibrators);

    boolean importData(@Nonnull Collection<Calibrator> newCalibrators,
                       @Nonnull Collection<Calibrator>calibratorsForChange);

    boolean isExists(@Nonnull String calibratorName);
    boolean isExist(@Nonnull String oldCalibratorName, @Nonnull String newCalibratorName);
}
