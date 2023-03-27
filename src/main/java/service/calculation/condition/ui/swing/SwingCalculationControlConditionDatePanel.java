package service.calculation.condition.ui.swing;

import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.calculation.condition.ui.CalculationControlConditionDatePanel;
import util.DateHelper;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SwingCalculationControlConditionDatePanel extends TitledPanel implements CalculationControlConditionDatePanel {
    private static final String TITLE = "Дата перевірки";

    private final DefaultTextField dayField;
    private final DefaultTextField monthField;
    private final DefaultTextField yearField;

    public SwingCalculationControlConditionDatePanel() {
        super(TITLE, Color.BLACK);

        dayField = new DefaultTextField(2);
        monthField = new DefaultTextField(2);
        yearField = new DefaultTextField(4);

        dayField.setFocusListener(focusGained);
        monthField.setFocusListener(focusGained);
        yearField.setFocusListener(focusGained);

        this.add(dayField, new CellBuilder().fill(CellBuilder.NONE).x(0).build());
        this.add(new DefaultLabel("."), new CellBuilder().fill(CellBuilder.NONE).x(1).build());
        this.add(monthField, new CellBuilder().fill(CellBuilder.NONE).x(2).build());
        this.add(new DefaultLabel("."), new CellBuilder().fill(CellBuilder.NONE).x(3).build());
        this.add(yearField, new CellBuilder().fill(CellBuilder.NONE).x(4).build());
    }

    @Override
    public void setDate(String date) {
        if (DateHelper.isDateValid(date)) {
            String[] d = DateHelper.getSplittedDate(date);
            dayField.setText(d[0]);
            monthField.setText(d[1]);
            yearField.setText(d[2]);
        }
    }

    @Nullable
    @Override
    public String getDate() {
        String date = String.format("%s.%s.%s", dayField.getText(), monthField.getText(), yearField.getText());
        if (DateHelper.isDateValid(date)) {
            this.setTitleColor(Color.BLACK);
            return date;
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }

    private final FocusAdapter focusGained = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
        }
    };
}
