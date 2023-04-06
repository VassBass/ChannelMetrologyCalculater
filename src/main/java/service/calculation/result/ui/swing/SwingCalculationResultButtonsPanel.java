package service.calculation.result.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationResultButtonsPanel extends DefaultPanel {
    private static final String NEXT_BUTTON_TEXT = "Далі";
    private static final String CANCEL_BUTTON_TEXT = "Закрити";
    private static final String BACK_BUTTON_TEXT = "Назад";

    public SwingCalculationResultButtonsPanel(CalculationManager manager) {
        super();

        DefaultButton backButton = new DefaultButton(BACK_BUTTON_TEXT);
        DefaultButton cancelButton = new DefaultButton(CANCEL_BUTTON_TEXT);
        DefaultButton nextButton = new DefaultButton(NEXT_BUTTON_TEXT);

        backButton.addActionListener(e -> manager.showInputDialog());
        cancelButton.addActionListener(e -> manager.disposeCalculation());
        nextButton.addActionListener(e -> manager.showPersonDialog());

        this.add(backButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(cancelButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(nextButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
