package service.calculation.input.ui.swing;

import localization.Labels;
import model.ui.ButtonCell;
import model.ui.DefaultCheckBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.calculation.input.ui.CalculationInputAlarmPanel;
import util.StringHelper;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;

import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputAlarmPanel extends DefaultPanel implements CalculationInputAlarmPanel {
    private static final String ALARM_CHECK = "alarmCheck";
    private static final String ALARM_ON_VALUE = "alarmOnValue";

    private double buffer;

    private final DefaultCheckBox title;
    private final DefaultTextField value;

    public SwingCalculationInputAlarmPanel(String measurementValue) {
        super();
        Map<String, String> labels = Labels.getLabels(SwingCalculationInputAlarmPanel.class);

        title = new DefaultCheckBox(labels.get(ALARM_CHECK));
        title.setSelected(false);

        value = new DefaultTextField(4, Labels.DASH, labels.get(ALARM_ON_VALUE));
        value.setEnabled(false);

        ButtonCell val = new ButtonCell(SIMPLE, measurementValue);

        value.setFocusListener(focusListener);
        title.addItemListener(e -> {
            if (title.isSelected()) {
                value.setEnabled(true);
                value.setText(String.valueOf(buffer));
            } else {
                value.setText(Labels.DASH);
                value.setEnabled(false);
            }
        });

        this.add(title, new CellBuilder().x(0).build());
        this.add(value, new CellBuilder().x(1).build());
        this.add(val, new CellBuilder().x(2).build());
    }

    @Override
    public double getAlarmValue() {
        if (isEnabled()) {
            String val = value.getText().replaceAll(Labels.COMMA, Labels.DOT);
            if (StringHelper.isDouble(val)) return Double.parseDouble(val);
        }
        return Double.NaN;
    }

    @Override
    public boolean isEnabled() {
        return title.isSelected();
    }

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
            String text = source.getText().replaceAll(Labels.COMMA, Labels.DOT);
            if (StringHelper.isDouble(text)) buffer = Double.parseDouble(text);
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String text = source.getText().replaceAll(Labels.COMMA, Labels.DOT);
            if (StringHelper.isDouble(text)) {
                buffer = Double.parseDouble(text);
            }
            source.setText(String.valueOf(buffer));
        }
    };
}
