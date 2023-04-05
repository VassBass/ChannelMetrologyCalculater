package service.calculation.input;

import java.util.Objects;

public class CalculationInputValuesBuffer {
    private static volatile CalculationInputValuesBuffer instance;
    public static CalculationInputValuesBuffer getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (CalculationInputValuesBuffer.class) {
                if (Objects.isNull(instance)) instance = new CalculationInputValuesBuffer();
            }
        }
        return instance;
    }

    private int percentDecimalPoint;
    private int valueDecimalPoint;

    private CalculationInputValuesBuffer() {
        percentDecimalPoint = 2;
        valueDecimalPoint = 2;
    }

    public int getPercentDecimalPoint() {
        return percentDecimalPoint;
    }

    public void setPercentDecimalPoint(int percentDecimalPoint) {
        this.percentDecimalPoint = percentDecimalPoint;
    }

    public int getValueDecimalPoint() {
        return valueDecimalPoint;
    }

    public void setValueDecimalPoint(int valueDecimalPoint) {
        this.valueDecimalPoint = valueDecimalPoint;
    }
}
