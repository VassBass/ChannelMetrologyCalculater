package service.error_calculater;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.MeasurementTransformFactor;
import model.dto.Sensor;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import repository.RepositoryFactory;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MxParserErrorCalculater extends ErrorCalculater {
    private static final String CONVERTED_NUMBER_REGEX = "conv\\((.*?)\\)";
    private static final String FUNCTION_DEFINITION = "At(R,r,convR)";

    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    public MxParserErrorCalculater(@Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull Channel channel) {
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    @Override
    public double calculate(@Nonnull Calibrator errorFormulaHolder) {
        String formula = errorFormulaHolder.getErrorFormula().replaceAll("\\,", ".");
        if (isFormulaValid(formula)) {
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
            double factor = factorRepository.getBySource(errorFormulaHolder.getMeasurementValue())
                    .stream()
                    .filter(f -> f.getTransformTo().equals(channel.getMeasurementValue()))
                    .findAny()
                    .map(MeasurementTransformFactor::getTransformFactor)
                    .orElse(1D);

            String[] numsToConvert = StringUtils.substringsBetween(formula, "conv(", ")");
            if (Objects.nonNull(numsToConvert)) {
                for (String num : numsToConvert) {
                    if (StringHelper.isDouble(num)) {
                        double n = Double.parseDouble(num) * factor;
                        formula = formula.replaceFirst(CONVERTED_NUMBER_REGEX, String.valueOf(n));
                    }
                }
            }

            Function function = new Function(String.format("%s = %s",FUNCTION_DEFINITION, formula));
            Argument channelRangeArgument = new Argument(String.format("R = %s", channel.calculateRange()));
            Argument errorHolderRangeArgument = new Argument(String.format("r = %s", errorFormulaHolder.calculateRange()));
            double convertedErrorHolderRange = errorFormulaHolder.calculateRange() * factor;
            Argument convertedErrorHolderRangeArgument = new Argument(String.format("convR = %s", convertedErrorHolderRange));
            Expression expression = new Expression(FUNCTION_DEFINITION,
                    function,
                    channelRangeArgument, errorHolderRangeArgument, convertedErrorHolderRangeArgument);
            return expression.calculate();
        }
        return Double.NaN;
    }

    @Override
    public double calculate(Sensor errorFormulaHolder) {
        String formula = errorFormulaHolder.getErrorFormula().replaceAll("\\,", ".");
        if (isFormulaValid(formula)) {
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
            double factor = factorRepository.getBySource(errorFormulaHolder.getMeasurementValue())
                    .stream()
                    .filter(f -> f.getTransformTo().equals(channel.getMeasurementValue()))
                    .findAny()
                    .map(MeasurementTransformFactor::getTransformFactor)
                    .orElse(1D);

            String[] numsToConvert = StringUtils.substringsBetween(formula, "conv(", ")");
            if (Objects.nonNull(numsToConvert)) {
                for (String num : numsToConvert) {
                    if (StringHelper.isDouble(num)) {
                        double n = Double.parseDouble(num) * factor;
                        formula = formula.replaceFirst(CONVERTED_NUMBER_REGEX, String.valueOf(n));
                    }
                }
            }

            Function function = new Function(String.format("%s = %s",FUNCTION_DEFINITION, formula));
            Argument channelRangeArgument = new Argument(String.format("R = %s", channel.calculateRange()));
            Argument errorHolderRangeArgument = new Argument(String.format("r = %s", errorFormulaHolder.calculateRange()));
            double convertedErrorHolderRange = errorFormulaHolder.calculateRange() * factor;
            Argument convertedErrorHolderRangeArgument = new Argument(String.format("convR = %s", convertedErrorHolderRange));
            Expression expression = new Expression(FUNCTION_DEFINITION,
                    function,
                    channelRangeArgument, errorHolderRangeArgument, convertedErrorHolderRangeArgument);
            return expression.calculate();
        }
        return Double.NaN;
    }
}
