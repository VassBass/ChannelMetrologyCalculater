package service.calculation.input.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationInputButtonsPanel extends DefaultPanel {

    private final DefaultButton buttonClose;

    public SwingCalculationInputButtonsPanel(CalculationManager manager) {
        super();
        Labels labels = Labels.getInstance();

        DefaultButton buttonCalculate = new DefaultButton(labels.calculate);
        DefaultButton buttonBack = new DefaultButton(labels.back);
        buttonClose = new DefaultButton(labels.close);

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
