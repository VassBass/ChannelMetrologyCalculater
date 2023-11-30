package service.calculation.result.worker;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.sensor.SensorRepository;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class DefaultCalculationWorker extends CalculationWorker {
    public static final Logger logger = LoggerFactory.getLogger(DefaultCalculationWorker.class);

    private final RepositoryFactory repositoryFactory;

    public DefaultCalculationWorker(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public boolean calculate(Protocol protocol) {
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        Channel channel = protocol.getChannel();
        Sensor sensor = sensorRepository.get(channel.getCode());
        if (Objects.isNull(sensor)) {
            logger.warn(String.format("Can't find sensor with code \"%s\"", channel.getCode()));
            return false;
        }
        Calibrator calibrator = protocol.getCalibrator();

        MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, channel);
        double sensorError = errorCalculater.calculate(sensor);
        double calibratorError = errorCalculater.calculate(calibrator);

        //calculation
        Map<Double, double[]> absoluteErrors = calculateAbsoluteErrors(protocol.getOutput());
        double maxAbsoluteError = calculateMaxAbsoluteError(absoluteErrors);
        if (Double.isNaN(maxAbsoluteError)) {
            logger.warn("Max absolute error calculation error");
            return false;
        }
        double absoluteErrorWithEquipment = calculateAbsoluteErrorWithEquipment(maxAbsoluteError, sensorError, calibratorError);

        double relativeError = calculateRelativeErrorWithEquipment(absoluteErrorWithEquipment, channel.calculateRange());
        TreeMap<Double, Double> systematicErrors = calculateSystematicErrors(absoluteErrors);
        double standardIndeterminacyA = calculateStandardIndeterminacyA(absoluteErrors);
        double standardIndeterminacyB = calculateStandardIndeterminacyB(sensorError, calibratorError);
        double totalStandardIndeterminacy = calculateTotalStandardIndeterminacy(standardIndeterminacyA, standardIndeterminacyB);
        double extendedIndeterminacy = calculateExtendedIndeterminacy(totalStandardIndeterminacy, 2.0);

        //fill protocol
        protocol.setAbsoluteError(absoluteErrorWithEquipment);
        protocol.setRelativeError(relativeError);
        protocol.setSystematicErrors(systematicErrors);
        protocol.setExtendedIndeterminacy(extendedIndeterminacy);
        return true;
    }
}
