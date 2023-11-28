package service.calculation.input.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationInputButtonsPanel extends DefaultPanel {

    private final DefaultButton buttonClose;

    public SwingCalculationInputButtonsPanel(CalculationManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton buttonCalculate = new DefaultButton(labels.get(RootLabelName.CALCULATE));
        DefaultButton buttonBack = new DefaultButton(labels.get(RootLabelName.BACK));
        buttonClose = new DefaultButton(labels.get(RootLabelName.CLOSE));

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
