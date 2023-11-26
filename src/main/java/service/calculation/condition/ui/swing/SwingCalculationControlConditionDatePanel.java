package service.calculation.condition.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultLabel;
import model.ui.IntegerTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.calculation.condition.CalculationControlConditionValuesBuffer;
import service.calculation.condition.ui.CalculationControlConditionDatePanel;
import util.DateHelper;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SwingCalculationControlConditionDatePanel extends TitledPanel implements CalculationControlConditionDatePanel {
    private static final String DATE_FORMAT = "%s.%s.%s";

    private final IntegerTextField dayField;
    private final IntegerTextField monthField;
    private final IntegerTextField yearField;

    public SwingCalculationControlConditionDatePanel() {
        super(Labels.getRootLabels().get(RootLabelName.CHECK_DATE), Color.BLACK);
        CalculationControlConditionValuesBuffer buffer = CalculationControlConditionValuesBuffer.getInstance();

        dayField = new IntegerTextField(2);
        monthField = new IntegerTextField(2);
        yearField = new IntegerTextField(4);

        this.setDate(buffer.getDate());

        dayField.setFocusListener(dayMonthFocusListener);
        monthField.setFocusListener(dayMonthFocusListener);
        yearField.setFocusListener(yearFocusListener);

        this.add(dayField, new CellBuilder().fill(CellBuilder.NONE).x(0).build());
        this.add(new DefaultLabel(Labels.DOT), new CellBuilder().fill(CellBuilder.NONE).x(1).build());
        this.add(monthField, new CellBuilder().fill(CellBuilder.NONE).x(2).build());
        this.add(new DefaultLabel(Labels.DOT), new CellBuilder().fill(CellBuilder.NONE).x(3).build());
        this.add(yearField, new CellBuilder().fill(CellBuilder.NONE).x(4).build());
    }

    @Override
    public void setDate(String date) {
        if (DateHelper.isDateValid(date)) {
            String[] d = DateHelper.getSplittedDate(date);
            dayField.setText(d[0]);
            monthField.setText(d[1]);
            yearField.setText(d[2]);

            dayField.setDefaultValue(Integer.parseInt(dayField.getText()));
            monthField.setDefaultValue(Integer.parseInt(monthField.getText()));
            yearField.setDefaultValue(Integer.parseInt(yearField.getText()));
        }
    }

    @Nullable
    @Override
    public String getDate() {
        String date = String.format(DATE_FORMAT, dayField.getText(), monthField.getText(), yearField.getText());
        if (DateHelper.isDateValid(date)) {
            this.setTitleColor(Color.BLACK);
            return date;
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }

    private final FocusListener dayMonthFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField) e.getSource()).selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField textField = (JTextField) e.getSource();
            String t = textField.getText();
            if (t.length() == 1) t = Labels.ZERRO + t;
            textField.setText(t);
        }
    };

    private final FocusListener yearFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField) e.getSource()).selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField textField = (JTextField) e.getSource();
            String t = textField.getText();
            if (t.length() == 1) t = Labels.TWO_HUNDRED + t;
            if (t.length() == 2) t = Labels.TWENTY + t;
            if (t.length() == 3) t = Labels.TWO + t;

            textField.setText(t);
        }
    };
}
