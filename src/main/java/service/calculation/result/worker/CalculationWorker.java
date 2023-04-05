package service.calculation.result.worker;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;

import java.util.*;

public abstract class CalculationWorker {
    public abstract boolean calculate(Protocol protocol);

    /**
     * Calculates absolute errors in every point of measurement
     * ΔXi = (Xo - Xi)
     * where:
     * ΔXi - absolute error for single point of measurement
     * Xo - output (measured value)
     * Xi - input (set-point value)
     * @param measurementValues map of inputs and outputs. Key = input, Value = outputs
     * @return map of inputs and absolute errors. Key = input, Value = absolute errors
     */
    protected Map<Double, double[]> calculateAbsoluteErrors(Map<Double, double[]> measurementValues) {
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

    /**
     * Calculates max absolute errors from map of absolute errors.
     * @param absoluteErrors map of inputs and absolute errors {@link #calculateAbsoluteErrors(Map)}. Key = input, Value = absolute errors
     * @return max value from map-values
     */
    protected double calculateMaxAbsoluteError(Map<Double, double[]> absoluteErrors) {
        List<Double> biggest = new ArrayList<>();
        for (double[] values : absoluteErrors.values()) {
            biggest.add(Arrays.stream(values).map(Math::abs).max().orElse(0.0));
        }
        return biggest.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
    }

    /**
     * Calculates absolute error of channel with errors of other equipment involved in control
     * Δch = √(ΔXmax² + Δsen² + Δcal²)
     * where:
     * Δch - absolute error of channel
     * ΔXmax - max absolute error
     * Δsen - absolute error of sensor
     * Δcal - absolute error of calibrator
     * @param maxAbsoluteError of all absolute errors {@link #calculateAbsoluteErrors(Map)}
     * @param sensorError {@link Sensor#errorFormula}
     * @param calibratorError {@link Calibrator#errorFormula}
     * @return value of absolute error with equipment errors
     * @see service.error_calculater.ErrorCalculater
     * @see MxParserErrorCalculater
     */
    protected double calculateAbsoluteErrorWithEquipment(double maxAbsoluteError, double sensorError, double calibratorError) {
        return Math.sqrt(Math.pow(maxAbsoluteError, 2) + Math.pow(sensorError, 2) + Math.pow(calibratorError, 2));
    }

    /**
     * Calculates relative error in channel range from value of max absolute error
     * ɣch = (Δch * 100) / Rch
     * where:
     * ɣch - relative error
     * Δch - max absolute error of channel
     * Rch - range of channel
     * @param absoluteErrorWithEquipment {@link #calculateMaxAbsoluteError(Map)}
     * @param channelRange {@link Channel#calculateRange()}
     * @return value of relative error in percent
     */
    protected double calculateRelativeError(double maxAbsoluteError, double channelRange) {
        return (maxAbsoluteError * 100) / channelRange;
    }

    /**
     * Calculates relative error in channel range from value of absolute error with errors of other equipment involved in control
     * ɣch = (Δch * 100) / Rch
     * where:
     * ɣch - relative error
     * Δch - absolute error of channel
     * Rch - range of channel
     * @param absoluteErrorWithEquipment {@link #calculateAbsoluteErrorWithEquipment(double, double, double)}
     * @param channelRange {@link Channel#calculateRange()}
     * @return value of relative error in percent
     */
    protected double calculateRelativeErrorWithEquipment(double absoluteErrorWithEquipment, double channelRange) {
        return (absoluteErrorWithEquipment * 100) / channelRange;
    }

    /**
     * Calculates systematic errors for every set-point
     * ΔS = ΣΔXi / n
     * where:
     * ΔS - systematic error for set-point
     * ΣΔXi - sum of absolute errors in corresponding set-points
     * n - number of measurements of corresponding set-point
     * @param absoluteErrors absolute errors in every set-point. {@link #calculateAbsoluteErrors(Map)} Key = input, Value = output
     * @return map of inputs and corresponding systematic errors. Key = input, Value = systematic error
     */
    protected TreeMap<Double, Double> calculateSystematicErrors(Map<Double, double[]> absoluteErrors) {
        TreeMap<Double, Double> result = new TreeMap<>();
        for (Map.Entry<Double, double[]> entry : absoluteErrors.entrySet()) {
            double sum = Arrays.stream(entry.getValue()).sum();
            double r = sum / (entry.getValue().length);
            result.put(entry.getKey(), r);
        }
        return result;
    }

    /**
     * Calculates standard indeterminacy type "A"
     * UA = √( (Σ(ΔXi²)) / (n * (n - 1)) )
     * where:
     * UA - standard indeterminacy type "A"
     * (Σ(ΔXi²)) - sum of squared absolute errors
     * n - number of measurement
     * @param absoluteErrors absolute errors in every set-point. {@link #calculateAbsoluteErrors(Map)} Key = input, Value = output
     * @return value of standard indeterminacy type "A"
     */
    protected double calculateStandardIndeterminacyA(Map<Double, double[]> absoluteErrors) {
        double sum = 0;
        double n = 0;
        for (Map.Entry<Double, double[]> entry : absoluteErrors.entrySet()) {
            sum += Arrays.stream(entry.getValue()).map(d -> Math.pow(d, 2)).sum();
            n += entry.getValue().length;
        }
        return Math.sqrt(sum / (n * (n - 1)));
    }

    /**
     * Calculates standard indeterminacy type "B"
     * UB = √(Δsen² + Δcal²)
     * where:
     * UB - standard indeterminacy type "B"
     * Δsen - absolute error of sensor
     * Δcal - absolute error of calibrator
     * @param errorSensor {@link Sensor#errorFormula}
     * @param errorCalibrator {@link Calibrator#errorFormula}
     * @return value of standard indeterminacy type "B"
     * @see service.error_calculater.ErrorCalculater
     * @see MxParserErrorCalculater
     */
    protected double calculateStandardIndeterminacyB(double errorSensor, double errorCalibrator) {
        return Math.sqrt(Math.pow(errorSensor, 2) + Math.pow(errorCalibrator, 2));
    }

    /**
     * Calculates total standard indeterminacy
     * UT = √(UA² + UB²)
     * where:
     * UT - total standard indeterminacy
     * UA - standard indeterminacy type "A"
     * UB - standard indeterminacy type "B"
     * @param standardIndeterminacyA {@link #calculateStandardIndeterminacyA(Map)}
     * @param standardIndeterminacyB {@link #calculateStandardIndeterminacyB(double, double)}
     * @return value of total standard indeterminacy
     */
    protected double calculateTotalStandardIndeterminacy(double standardIndeterminacyA, double standardIndeterminacyB) {
        return Math.sqrt(Math.pow(standardIndeterminacyA, 2) + Math.pow(standardIndeterminacyB, 2));
    }

    /**
     * Calculates extended indeterminacy
     * U = UT * k
     * where:
     * U - extended indeterminacy
     * UT - total standard indeterminacy
     * k - coverage ratio (as usual k = 2.0)
     * @param totalStandardIndeterminacy {@link #calculateTotalStandardIndeterminacy(double, double)}
     * @param coverageRatio <a href="http://eprints.kname.edu.ua/17585/1/27-37.pdf"></a>
     * @return value of total extended indeterminacy
     */
    protected double calculateExtendedIndeterminacy(double totalStandardIndeterminacy, double coverageRatio) {
        return totalStandardIndeterminacy * coverageRatio;
    }
}
