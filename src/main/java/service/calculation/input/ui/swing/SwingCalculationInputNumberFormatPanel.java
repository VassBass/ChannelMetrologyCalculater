package service.calculation.input.ui.swing;

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

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputNumberFormatPanel extends TitledPanel implements CalculationInputNumberFormatPanel {
    private static final String TITLE_TEXT = "Форматування чисел";
    private static final String LABEL_TEXT = "Чисел після крапки";
    private static final String PERCENT_LABEL_TEXT = "Для відсотків:";
    private static final String VALUE_LABEL_TEXT = "Для інших чисел:";
    private static final String VALUE_TOOLTIP_TEXT = "Кількість чисел після крапки у числах з плаваючою точкою";
    private static final String PERCENT_TOOLTIP_TEXT = "Кількість чисел після крапки у відсоткових значеннях";
    private static final String BUTTON_TEXT = "Застосувати";

    private final IntegerTextField valueDecimalPoint;
    private final IntegerTextField percentDecimalPoint;
    private final DefaultButton buttonConfirm;

    public SwingCalculationInputNumberFormatPanel(CalculationInputManager manager) {
        super(TITLE_TEXT, Color.BLACK);
        CalculationInputValuesBuffer buffer = CalculationInputValuesBuffer.getInstance();

        ButtonCell label = new ButtonCell(HEADER, LABEL_TEXT);
        ButtonCell percentLabel = new ButtonCell(SIMPLE, PERCENT_LABEL_TEXT);
        ButtonCell valueLabel = new ButtonCell(SIMPLE, VALUE_LABEL_TEXT);

        valueDecimalPoint = new IntegerTextField(2, buffer.getValueDecimalPoint(), VALUE_TOOLTIP_TEXT);
        valueDecimalPoint.getDocument().addDocumentListener(changePoint);

        percentDecimalPoint = new IntegerTextField(2, buffer.getPercentDecimalPoint(), PERCENT_TOOLTIP_TEXT);
        percentDecimalPoint.getDocument().addDocumentListener(changePoint);

        buttonConfirm = new DefaultButton(BUTTON_TEXT);
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
