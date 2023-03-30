package service.calculation.result.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.calculation.result.ui.CalculationResultReferencePanel;

import static model.ui.ButtonCell.HEADER;

public class SwingCalculationResultReferencePanel extends DefaultPanel implements CalculationResultReferencePanel {
    private static final String LABEL_TEXT = "Номер довідки";
    private static final String TOOLTIP_TEXT = "Номер довідки про непридатність каналу";

    private final DefaultTextField referenceNumber;

    public SwingCalculationResultReferencePanel() {
        super();

        ButtonCell label = new ButtonCell(HEADER, LABEL_TEXT);
        referenceNumber = new DefaultTextField(10, TOOLTIP_TEXT);

        this.add(label, new CellBuilder().x(0).build());
        this.add(referenceNumber, new CellBuilder().x(1).build());
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber.getText();
    }
}
