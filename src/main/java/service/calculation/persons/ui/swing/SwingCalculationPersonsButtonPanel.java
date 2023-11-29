package service.calculation.persons.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import javax.annotation.Nonnull;
import java.util.Map;

public class SwingCalculationPersonsButtonPanel extends DefaultPanel {

    public SwingCalculationPersonsButtonPanel(@Nonnull CalculationManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton printButton = new DefaultButton(labels.get(RootLabelName.PRINT));
        DefaultButton openButton = new DefaultButton(labels.get(RootLabelName.OPEN));
        DefaultButton cancelButton = new DefaultButton(labels.get(RootLabelName.CANCEL));
        DefaultButton doneButton = new DefaultButton(labels.get(RootLabelName.DONE));

        printButton.addActionListener(e -> manager.printProtocol());
        openButton.addActionListener(e -> manager.openProtocol());
        cancelButton.addActionListener(e -> manager.showInputDialog());
        doneButton.addActionListener(e -> manager.endCalculation());

        this.add(cancelButton, new CellBuilder().x(0).y(0).width(1).build());
        this.add(openButton, new CellBuilder().x(1).y(0).width(1).build());
        this.add(printButton, new CellBuilder().x(2).y(0).width(1).build());
        this.add(doneButton, new CellBuilder().x(0).y(1).width(3).build());
    }
}
