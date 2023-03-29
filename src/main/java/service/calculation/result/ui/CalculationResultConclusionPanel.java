package service.calculation.result.ui;

import java.util.List;

public interface CalculationResultConclusionPanel {
    boolean isSuitable();
    String getConclusion();
    void setConclusions(List<String> conclusions);
}
