package service.repository.repos.calibrator;

import model.dto.Calibrator;
import model.dto.Measurement;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface CalibratorRepository {
    Collection<Calibrator> getAll();
    String[]getAllNames(@Nonnull Measurement measurement);
    Calibrator get(String name);

    boolean add(@Nonnull Calibrator calibrator);

    boolean remove(@Nonnull Calibrator calibrator);
    boolean removeByMeasurementValue(@Nonnull String measurementValue);
    boolean clear();

    boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Calibrator> calibrators);

    boolean importData(@Nonnull Collection<Calibrator> newCalibrators,
                       @Nonnull Collection<Calibrator>calibratorsForChange);

    boolean isExists(@Nonnull Calibrator calibrator);
}
