package service.measurement.info.ui;

import model.dto.MeasurementTransformFactor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface MeasurementInfoFactorsPanel {
    void setFactorOutputList(List<MeasurementTransformFactor> factorList);
    @Nullable Map<String, Double> getFactorList();
    void setFactorInput(String input);
}
