package service.calculation.result.ui.swing;

import localization.label.Labels;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationResultButtonsPanel extends DefaultPanel {

    public SwingCalculationResultButtonsPanel(CalculationManager manager) {
        super();
        Labels labels = Labels.getInstance();

        DefaultButton backButton = new DefaultButton(labels.back);
        DefaultButton cancelButton = new DefaultButton(labels.cancel);
        DefaultButton nextButton = new DefaultButton(labels.further);

        backButton.addActionListener(e -> manager.showInputDialog());
        cancelButton.addActionListener(e -> manager.disposeCalculation());
        nextButton.addActionListener(e -> manager.showPersonDialog());

        this.add(backButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(cancelButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(nextButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
