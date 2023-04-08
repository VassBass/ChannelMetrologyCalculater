package service.calibrator.info;

import repository.RepositoryFactory;
import service.calibrator.info.ui.CalibratorInfoContext;

import javax.annotation.Nonnull;

public class SwingCalibratorInfoManager implements CalibratorInfoManager {
    private final RepositoryFactory repositoryFactory;
    private final CalibratorInfoContext context;

    public SwingCalibratorInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull CalibratorInfoContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    @Override
    public void changingMeasurementValue() {

    }

    @Override
    public void copyTypeToNameField() {

    }

    @Override
    public void saveCalibrator() {

    }
}
