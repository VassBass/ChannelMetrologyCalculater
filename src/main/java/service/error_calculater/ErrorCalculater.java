package service.error_calculater;

import model.dto.Calibrator;
import model.dto.Sensor;

import java.util.Objects;

public abstract class ErrorCalculater {
    private static final String SUITABLE_SYMBOLS_REGEX = "\\)|\\(|\\d|\\.|\\s|\\+|-|r|R|conv|\\*|\\/";

    public static boolean isFormulaValid(String formula) {
        if (Objects.isNull(formula) || formula.isEmpty()) return false;
        return formula
                .replaceAll("\\,", ".")
                .replaceAll(SUITABLE_SYMBOLS_REGEX, "")
                .isEmpty();
    }

    abstract double calculate(Calibrator errorFormulaHolder);
    abstract double calculate(Sensor errorFormulaHolder);
}
