package service.calculation.condition.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

public class SwingCalculationControlConditionButtonsPanel extends DefaultPanel {
    private static final String POSITIVE_BUTTON_TEXT = "Почати";
    private static final String NEGATIVE_BUTTON_TEXT = "Відміна";

    private final DefaultButton negativeButton;

    public SwingCalculationControlConditionButtonsPanel(CalculationManager manager) {
        super();

        DefaultButton positiveButton = new DefaultButton(POSITIVE_BUTTON_TEXT);
        negativeButton = new DefaultButton(NEGATIVE_BUTTON_TEXT);

        this.add(negativeButton, new CellBuilder().x(0).build());
        this.add(positiveButton, new CellBuilder().x(1).build());

        positiveButton.addActionListener(e -> manager.showInputDialog());
        negativeButton.addActionListener(e -> manager.disposeCalculation());
    }

    public void clickNegativeButton() {
        negativeButton.doClick();
    }
}
