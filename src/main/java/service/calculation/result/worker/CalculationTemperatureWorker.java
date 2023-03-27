package service.calculation.result.worker;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.sensor.SensorRepository;
import service.calculation.dto.Protocol;
import service.error_calculater.ErrorCalculater;
import service.error_calculater.MxParserErrorCalculater;

import javax.annotation.Nonnull;
import java.util.*;

public class CalculationTemperatureWorker implements CalculationWorker {
    private static final Logger logger = LoggerFactory.getLogger(CalculationTemperatureWorker.class);

    private final RepositoryFactory repositoryFactory;

    public CalculationTemperatureWorker(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public boolean calculate(Protocol protocol) {
        //init
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
        double absoluteErrorWithEquipment = calculateAbsoluteErrorWithEquipment(maxAbsoluteError, sensorError, calibratorError);
        Map<Double, Double> systematicErrors = calculateSystematicErrors(absoluteErrors);
    }

    private Map<Double, double[]> calculateAbsoluteErrors(Map<Double, double[]> measurementValues) {
        TreeMap<Double, double[]> result = new TreeMap<>();
        for (Map.Entry<Double, double[]> entry : measurementValues.entrySet()) {
            double input = entry.getKey();
            double[] output = entry.getValue();
            List<Double> errors = new ArrayList<>();
            for (double val : output) {
                errors.add(val - input);
            }
            result.put(input, errors.stream().mapToDouble(Double::doubleValue).toArray());
        }
        return result;
    }

    private double calculateMaxAbsoluteError(Map<Double, double[]> absoluteErrors) {
        List<Double> biggest = new ArrayList<>();
        for (double[] values : absoluteErrors.values()) {
            biggest.add(Arrays.stream(values).map(Math::abs).max().orElse(0.0));
        }
        return biggest.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
    }

    private double calculateAbsoluteErrorWithEquipment(double maxAbsoluteError, double sensorError, double calibratorError) {
        return Math.sqrt(Math.pow(maxAbsoluteError, 2) + Math.pow(sensorError, 2) + Math.pow(calibratorError, 2));
    }

    private Map<Double, Double> calculateSystematicErrors(Map<Double, double[]> absoluteErrors) {
        Map<Double, Double> result = new TreeMap<>();
        for (Map.Entry<Double, double[]> entry : absoluteErrors.entrySet()) {
            double sum = Arrays.stream(entry.getValue()).sum();
            double r = sum / (absoluteErrors.size() * 2);
            result.put(entry.getKey(), r);
        }
        return result;
    }

    private double calculateStandardIndeterminacyA(Map<Double, double[]> errorsAbsolute) {

    }
}
