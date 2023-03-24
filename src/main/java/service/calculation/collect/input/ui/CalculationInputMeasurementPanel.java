package service.calculation.collect.input.ui;

import model.dto.ControlPoints;

public interface CalculationInputMeasurementPanel {
    void setControlPoints(ControlPoints controlPoints);

    double[] getInputInPercent();
    double[] getInputInValue();
    double[] getMeasurement1Values();
    double[] getMeasurement2Values();
    double[] getMeasurement3Values();
    double[] getMeasurement4Values();
    double[] getMeasurement5Values();

    void setInputInPercent(double[] values);
    void setInputInValue(double[] values);
    void setMeasurement1Values(double[] values);
    void setMeasurement2Values(double[] values);
    void setMeasurement3Values(double[] values);
    void setMeasurement4Values(double[] values);
    void setMeasurement5Values(double[] values);
}
