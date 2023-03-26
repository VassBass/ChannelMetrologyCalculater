package service.calculation.collect.input.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationInputButtonsPanel extends DefaultPanel {
    private static final String BUTTON_CALCULATE_TEXT = "Розрахувати";
    private static final String BUTTON_BACK_TEXT = "Назад";
    private static final String BUTTON_CLOSE_TEXT = "Закрити";

    private final DefaultButton buttonClose;

    public SwingCalculationInputButtonsPanel(CalculationManager manager) {
        super();

        DefaultButton buttonCalculate = new DefaultButton(BUTTON_CALCULATE_TEXT);
        DefaultButton buttonBack = new DefaultButton(BUTTON_BACK_TEXT);
        buttonClose = new DefaultButton(BUTTON_CLOSE_TEXT);

        buttonCalculate.addActionListener(e -> manager.showResultDialog());
        buttonBack.addActionListener(e -> manager.showConditionDialog());
        buttonClose.addActionListener(e -> manager.disposeCalculation());

        this.add(buttonBack, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonClose, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonCalculate, new CellBuilder().fill(NONE).x(2).build());
    }

    public void clickCloseButton() {
        buttonClose.doClick();
    }
}
