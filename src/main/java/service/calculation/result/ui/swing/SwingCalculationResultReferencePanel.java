package service.calculation.result.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.calculation.result.ui.CalculationResultReferencePanel;

import static model.ui.ButtonCell.HEADER;

public class SwingCalculationResultReferencePanel extends DefaultPanel implements CalculationResultReferencePanel {
    private static final String REFERENCE_NUMBER = "referenceNumber";

    private final DefaultTextField referenceNumber;

    public SwingCalculationResultReferencePanel() {
        super();

        ButtonCell label = new ButtonCell(HEADER, Labels.getLabels(SwingCalculationResultReferencePanel.class).get(REFERENCE_NUMBER));
        referenceNumber = new DefaultTextField(10, Messages.getMessages(SwingCalculationResultReferencePanel.class).get(REFERENCE_NUMBER));

        this.add(label, new CellBuilder().x(0).build());
        this.add(referenceNumber, new CellBuilder().x(1).build());
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber.getText();
    }
}
