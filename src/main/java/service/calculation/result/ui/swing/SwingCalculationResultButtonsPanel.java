package service.calculation.result.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalculationResultButtonsPanel extends DefaultPanel {

    public SwingCalculationResultButtonsPanel(CalculationManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton backButton = new DefaultButton(labels.get(RootLabelName.BACK));
        DefaultButton cancelButton = new DefaultButton(labels.get(RootLabelName.CANCEL));
        DefaultButton nextButton = new DefaultButton(labels.get(RootLabelName.FURTHER));

        backButton.addActionListener(e -> manager.showInputDialog());
        cancelButton.addActionListener(e -> manager.disposeCalculation());
        nextButton.addActionListener(e -> manager.showPersonDialog());

        this.add(backButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(cancelButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(nextButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
