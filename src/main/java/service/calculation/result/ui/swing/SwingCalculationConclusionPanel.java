package service.calculation.result.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import service.calculation.dto.Protocol;
import service.calculation.result.ui.CalculationConclusionPanel;

public class SwingCalculationConclusionPanel extends DefaultPanel implements CalculationConclusionPanel {

    private final ButtonCell result;
    private final DefaultComboBox conclusion;

    public SwingCalculationConclusionPanel(Protocol protocol) {
        super();
    }

    @Override
    public boolean getResult() {
        return false;
    }

    @Override
    public String getConclusion() {
        return null;
    }
}
