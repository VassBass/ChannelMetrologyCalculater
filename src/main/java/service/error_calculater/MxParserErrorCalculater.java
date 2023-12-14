package service.error_calculater;

import localization.Labels;
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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MxParserErrorCalculater {
    private static final String CONVERTED_NUMBER_REGEX = "conv\\((.*?)\\)";
    private static final String NOT_NUMBERS_REGEX = "conv\\(.*?\\)|R|r|conv";
    private static final String DEGREE0_REGEX = "⁰";
    private static final String DEGREE1_REGEX = "¹";
    private static final String DEGREE2_REGEX = "²";
    private static final String DEGREE3_REGEX = "³";
    private static final String DEGREE4_REGEX = "⁴";
    private static final String DEGREE5_REGEX = "⁵";
    private static final String DEGREE6_REGEX = "⁶";
    private static final String DEGREE7_REGEX = "⁷";
    private static final String DEGREE8_REGEX = "⁸";
    private static final String DEGREE9_REGEX = "⁹";
    private static final String SQRT_REGEX = "√";
    private static final String UNNECESSARY_DEGREE_SYMBOLS_REGEX = "(?<=\\^(?-)\\d)\\^";

    private static final String FUNCTION_DEFINITION = "At(R,r,convR)";

    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    public MxParserErrorCalculater(@Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull Channel channel) {
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    public static boolean isFormulaValid(String formula) {
        if (Objects.isNull(formula) || formula.isEmpty()) return false;

        String f = formula.replaceAll(NOT_NUMBERS_REGEX, Labels.ONE);
        Expression expression = new Expression(preparedString(f));
        return !Double.isNaN(expression.calculate());
    }

    public double calculate(@Nonnull Calibrator errorFormulaHolder) {
        String formula = preparedString(errorFormulaHolder.getErrorFormula());
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

    public double calculate(Sensor errorFormulaHolder) {
        String formula = preparedString(errorFormulaHolder.getErrorFormula());
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

    private static String preparedString(String f) {
        return f.replaceAll(Labels.COMMA, Labels.DOT)
                .replaceAll(DEGREE0_REGEX, "^0")
                .replaceAll(DEGREE1_REGEX, "^1")
                .replaceAll(DEGREE2_REGEX, "^2")
                .replaceAll(DEGREE3_REGEX, "^3")
                .replaceAll(DEGREE4_REGEX, "^4")
                .replaceAll(DEGREE5_REGEX, "^5")
                .replaceAll(DEGREE6_REGEX, "^6")
                .replaceAll(DEGREE7_REGEX, "^7")
                .replaceAll(DEGREE8_REGEX, "^8")
                .replaceAll(DEGREE9_REGEX, "^9")
                .replaceAll(UNNECESSARY_DEGREE_SYMBOLS_REGEX, EMPTY)
                .replaceAll(SQRT_REGEX, "sqrt");
    }
}
