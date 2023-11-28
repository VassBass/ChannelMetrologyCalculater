package service.calculation.input.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.ButtonCell;
import model.ui.DefaultButton;
import model.ui.IntegerTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.calculation.input.CalculationInputManager;
import service.calculation.input.CalculationInputValuesBuffer;
import service.calculation.input.ui.CalculationInputNumberFormatPanel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Map;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputNumberFormatPanel extends TitledPanel implements CalculationInputNumberFormatPanel {
    private static final String NUMBER_FORMATTING = "numberFormatting";
    private static final String DECIMAL_POINT = "decimalPoint";
    private static final String FOR_PERCENTS = "forPercents";
    private static final String FOR_NUMBERS = "forNumbers";

    private static final Map<String, String> labels = Labels.getLabels(SwingCalculationInputNumberFormatPanel.class);

    private final IntegerTextField valueDecimalPoint;
    private final IntegerTextField percentDecimalPoint;
    private final DefaultButton buttonConfirm;

    public SwingCalculationInputNumberFormatPanel(CalculationInputManager manager) {
        super(labels.get(NUMBER_FORMATTING), Color.BLACK);
        Map<String, String> rootLabels = Labels.getRootLabels();
        Map<String, String> messages = Messages.getMessages(SwingCalculationInputNumberFormatPanel.class);

        CalculationInputValuesBuffer buffer = CalculationInputValuesBuffer.getInstance();

        ButtonCell label = new ButtonCell(HEADER, labels.get(DECIMAL_POINT));
        ButtonCell percentLabel = new ButtonCell(SIMPLE, labels.get(FOR_PERCENTS));
        ButtonCell valueLabel = new ButtonCell(SIMPLE, labels.get(FOR_NUMBERS));

        valueDecimalPoint = new IntegerTextField(2, buffer.getValueDecimalPoint(), messages.get(DECIMAL_POINT));
        valueDecimalPoint.getDocument().addDocumentListener(changePoint);

        percentDecimalPoint = new IntegerTextField(2, buffer.getPercentDecimalPoint(), messages.get(FOR_PERCENTS));
        percentDecimalPoint.getDocument().addDocumentListener(changePoint);

        buttonConfirm = new DefaultButton(rootLabels.get(RootLabelName.APPLY));
        buttonConfirm.setEnabled(false);

        buttonConfirm.addActionListener(e -> {
            buttonConfirm.setEnabled(false);
            manager.setDecimalPoint(getPercentDecimalPoint(), getValueDecimalPoint());
        });

        this.add(label, new CellBuilder().x(0).y(0).height(2).build());
        this.add(percentLabel, new CellBuilder().x(1).y(0).height(1).build());
        this.add(valueLabel, new CellBuilder().x(1).y(1).height(1).build());
        this.add(percentDecimalPoint, new CellBuilder().x(2).y(0).height(1).build());
        this.add(valueDecimalPoint, new CellBuilder().x(2).y(1).height(1).build());
        this.add(buttonConfirm, new CellBuilder().x(0).y(2).height(1).width(3).build());
    }

    private final DocumentListener changePoint = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            buttonConfirm.setEnabled(true);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            buttonConfirm.setEnabled(true);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            buttonConfirm.setEnabled(true);
        }
    };

    @Override
    public int getValueDecimalPoint() {
        return Integer.parseInt(valueDecimalPoint.getText());
    }

    @Override
    public int getPercentDecimalPoint() {
        return Integer.parseInt(percentDecimalPoint.getText());
    }

    public void clickButtonLook() {
        buttonConfirm.doClick();
    }
}
