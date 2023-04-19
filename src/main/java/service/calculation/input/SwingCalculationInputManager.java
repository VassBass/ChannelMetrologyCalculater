package service.calculation.input;

import service.calculation.input.ui.SwingCalculationInputContext;
import service.calculation.input.ui.swing.SwingCalculationInputMeasurementPanel;

public class SwingCalculationInputManager implements CalculationInputManager {

    private final SwingCalculationInputContext context;

    public SwingCalculationInputManager(SwingCalculationInputContext context) {
        this.context = context;
    }

    @Override
    public void setDecimalPoint(int percentPoints, int valuePoints) {
        SwingCalculationInputMeasurementPanel measurementPanel = context.getElement(SwingCalculationInputMeasurementPanel.class);
        measurementPanel.setDecimalPoint(percentPoints, valuePoints);
    }
}
