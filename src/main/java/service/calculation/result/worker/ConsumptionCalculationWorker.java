package service.calculation.result.worker;

import model.dto.Calibrator;
import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.TreeMap;

public class ConsumptionCalculationWorker extends CalculationWorker {
    public static final Logger logger = LoggerFactory.getLogger(ConsumptionCalculationWorker.class);

    private final RepositoryFactory repositoryFactory;

    public ConsumptionCalculationWorker(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public boolean calculate(Protocol protocol) {
        Channel channel = protocol.getChannel();
        Calibrator calibrator = protocol.getCalibrator();

        MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, channel);
        double calibratorError = errorCalculater.calculate(calibrator);

        //calculation
        Map<Double, double[]> absoluteErrors = calculateAbsoluteErrors(protocol.getOutput());
        double maxAbsoluteError = calculateMaxAbsoluteError(absoluteErrors);
        if (Double.isNaN(maxAbsoluteError)) {
            logger.warn("Max absolute error calculation error");
            return false;
        }

        double relativeError = calculateRelativeError(maxAbsoluteError, channel.calculateRange());
        TreeMap<Double, Double> systematicErrors = calculateSystematicErrors(absoluteErrors);
        double standardIndeterminacyA = calculateStandardIndeterminacyA(absoluteErrors);
        double standardIndeterminacyB = calculateStandardIndeterminacyB(Double.NaN, calibratorError);
        double totalStandardIndeterminacy = calculateTotalStandardIndeterminacy(standardIndeterminacyA, standardIndeterminacyB);
        double extendedIndeterminacy = calculateExtendedIndeterminacy(totalStandardIndeterminacy, 2.0);

        //fill protocol
        protocol.setAbsoluteError(maxAbsoluteError);
        protocol.setRelativeError(relativeError);
        protocol.setSystematicErrors(systematicErrors);
        protocol.setExtendedIndeterminacy(extendedIndeterminacy);
        return true;
    }

    @Override
    protected double calculateStandardIndeterminacyB(double errorSensor, double errorCalibrator) {
        return Math.sqrt(Math.pow(errorCalibrator, 2));
    }
}
