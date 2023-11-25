package service.calculation.condition.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

public class SwingCalculationControlConditionButtonsPanel extends DefaultPanel {
    private final DefaultButton negativeButton;

    public SwingCalculationControlConditionButtonsPanel(CalculationManager manager) {
        super();
        Labels labels = Labels.getInstance();

        DefaultButton positiveButton = new DefaultButton(labels.start);
        negativeButton = new DefaultButton(labels.cancel);

        this.add(negativeButton, new CellBuilder().x(0).build());
        this.add(positiveButton, new CellBuilder().x(1).build());

        positiveButton.addActionListener(e -> manager.showInputDialog());
        negativeButton.addActionListener(e -> manager.disposeCalculation());
    }

    public void clickNegativeButton() {
        negativeButton.doClick();
    }
}
