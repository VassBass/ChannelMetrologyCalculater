package service.calculation.persons.ui.swing;

import localization.label.Labels;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import javax.annotation.Nonnull;

public class SwingCalculationPersonsButtonPanel extends DefaultPanel {

    public SwingCalculationPersonsButtonPanel(@Nonnull CalculationManager manager) {
        super();
        Labels labels = Labels.getInstance();

        DefaultButton printButton = new DefaultButton(labels.print);
        DefaultButton openButton = new DefaultButton(labels.open);
        DefaultButton cancelButton = new DefaultButton(labels.cancel);
        DefaultButton doneButton = new DefaultButton(labels.done);

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
