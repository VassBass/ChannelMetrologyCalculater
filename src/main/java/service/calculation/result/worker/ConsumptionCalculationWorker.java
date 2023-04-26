package service.calculation.result.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;

import javax.annotation.Nonnull;

public class ConsumptionCalculationWorker extends DefaultCalculationWorker {
    public static final Logger logger = LoggerFactory.getLogger(ConsumptionCalculationWorker.class);

    public ConsumptionCalculationWorker(@Nonnull RepositoryFactory repositoryFactory) {
        super(repositoryFactory);
    }

    @Override
    protected double calculateAbsoluteErrorWithEquipment(double maxAbsoluteError, double sensorError, double calibratorError) {
        return Math.sqrt(Math.pow(maxAbsoluteError, 2) + Math.pow(calibratorError, 2));
    }

    @Override
    protected double calculateStandardIndeterminacyB(double errorSensor, double errorCalibrator) {
        return Math.abs(errorCalibrator);
    }
}
