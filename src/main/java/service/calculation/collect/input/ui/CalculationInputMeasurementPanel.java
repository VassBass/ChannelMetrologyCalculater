package service.calculation.collect.input.ui;

import java.util.TreeMap;

public interface CalculationInputMeasurementPanel {
    TreeMap<Double, Double> getInputs();
    double[][] getMeasurementValues();

    void setInputs(TreeMap<Double, Double> inputs);
    void setMeasurementValues(double[][] values);
}
