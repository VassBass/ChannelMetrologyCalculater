package service.calculation.input.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultButton;
import model.ui.IntegerTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.calculation.input.CalculationInputManager;
import service.calculation.input.ui.CalculationInputNumberFormatPanel;

import java.awt.*;

import static model.ui.ButtonCell.HEADER;

public class SwingCalculationInputNumberFormatPanel extends TitledPanel implements CalculationInputNumberFormatPanel {
    private static final String TITLE_TEXT = "Форматування чисел";
    private static final String LABEL_TEXT = "Чисел після крапки";
    private static final String TOOLTIP_TEXT = "Кількість чисел після крапки у числах з плаваючою точкою";
    private static final String BUTTON_TEXT = "Подивитись";

    private final IntegerTextField value;

    public SwingCalculationInputNumberFormatPanel(CalculationInputManager manager) {
        super(TITLE_TEXT, Color.BLACK);

        ButtonCell label = new ButtonCell(HEADER, LABEL_TEXT);
        value = new IntegerTextField(2, 2, TOOLTIP_TEXT);
        DefaultButton buttonLook = new DefaultButton(BUTTON_TEXT);

        buttonLook.addActionListener(e -> manager.setDecimalPoint(getDecimalPoint()));

        this.add(label, new CellBuilder().x(0).build());
        this.add(value, new CellBuilder().x(1).build());
        this.add(buttonLook, new CellBuilder().x(2).build());
    }

    @Override
    public int getDecimalPoint() {
        return Integer.parseInt(value.getText());
    }
}
