package service.calculation.persons.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationManager;

import javax.annotation.Nonnull;

public class SwingCalculationPersonsButtonPanel extends DefaultPanel {
    private static final String PRINT_TEXT = "Друкувати";
    private static final String OPEN_TEXT = "Відкрити";
    private static final String CANCEL_TEXT = "Відмінити";
    private static final String DONE_TEXT = "Готово";

    public SwingCalculationPersonsButtonPanel(@Nonnull CalculationManager manager) {
        super();

        DefaultButton printButton = new DefaultButton(PRINT_TEXT);
        DefaultButton openButton = new DefaultButton(OPEN_TEXT);
        DefaultButton cancelButton = new DefaultButton(CANCEL_TEXT);
        DefaultButton doneButton = new DefaultButton(DONE_TEXT);

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
