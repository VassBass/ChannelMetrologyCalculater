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
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputNumberFormatPanel extends TitledPanel implements CalculationInputNumberFormatPanel {
    private static final String TITLE_TEXT = "Форматування чисел";
    private static final String LABEL_TEXT = "Чисел після крапки";
    private static final String PERCENT_LABEL_TEXT = "Для відсотків:";
    private static final String VALUE_LABEL_TEXT = "Для інших чисел:";
    private static final String VALUE_TOOLTIP_TEXT = "Кількість чисел після крапки у числах з плаваючою точкою";
    private static final String PERCENT_TOOLTIP_TEXT = "Кількість чисел після крапки у відсоткових значеннях";
    private static final String BUTTON_TEXT = "Подивитись";

    private final IntegerTextField valueDecimalPoint;
    private final IntegerTextField percentDecimalPoint;

    public SwingCalculationInputNumberFormatPanel(CalculationInputManager manager) {
        super(TITLE_TEXT, Color.BLACK);

        ButtonCell label = new ButtonCell(HEADER, LABEL_TEXT);
        ButtonCell percentLabel = new ButtonCell(SIMPLE, PERCENT_LABEL_TEXT);
        ButtonCell valueLabel = new ButtonCell(SIMPLE, VALUE_LABEL_TEXT);
        valueDecimalPoint = new IntegerTextField(2, 2, VALUE_TOOLTIP_TEXT);
        percentDecimalPoint = new IntegerTextField(2, 2, PERCENT_TOOLTIP_TEXT);
        DefaultButton buttonLook = new DefaultButton(BUTTON_TEXT);

        buttonLook.addActionListener(e -> manager.setDecimalPoint(getPercentDecimalPoint(), getValueDecimalPoint()));

        this.add(label, new CellBuilder().x(0).y(0).height(2).build());
        this.add(percentLabel, new CellBuilder().x(1).y(0).height(1).build());
        this.add(valueLabel, new CellBuilder().x(1).y(1).height(1).build());
        this.add(percentDecimalPoint, new CellBuilder().x(2).y(0).height(1).build());
        this.add(valueDecimalPoint, new CellBuilder().x(2).y(1).height(1).build());
        this.add(buttonLook, new CellBuilder().x(0).y(2).height(1).width(3).build());
    }

    @Override
    public int getValueDecimalPoint() {
        return Integer.parseInt(valueDecimalPoint.getText());
    }

    @Override
    public int getPercentDecimalPoint() {
        return Integer.parseInt(percentDecimalPoint.getText());
    }
}
