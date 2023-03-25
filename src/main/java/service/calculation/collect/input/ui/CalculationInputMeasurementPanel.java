package service.calculation.collect.input.ui;

import javax.annotation.Nullable;
import java.util.TreeMap;

public interface CalculationInputMeasurementPanel {
    @Nullable TreeMap<Double, Double> getInputs();
    @Nullable double[][] getMeasurementValues();
}
