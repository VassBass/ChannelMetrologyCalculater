package service.calculation.input.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultCheckBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.calculation.input.ui.CalculationInputAlarmPanel;
import util.StringHelper;

import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputAlarmPanel extends DefaultPanel implements CalculationInputAlarmPanel {
    private static final String TITLE = "Перевірка сигналізації";
    private static final String TOOLTIP_TEXT = "Величина при якій спрацювала сигналізація";

    private double buffer;

    private final DefaultCheckBox title;
    private final DefaultTextField value;

    public SwingCalculationInputAlarmPanel(String measurementValue) {
        super();

        title = new DefaultCheckBox(TITLE);
        title.setSelected(false);

        value = new DefaultTextField(4, "-", TOOLTIP_TEXT);
        value.setEnabled(false);

        ButtonCell val = new ButtonCell(SIMPLE, measurementValue);

        title.addItemListener(e -> {
            if (title.isSelected()) {
                value.setEnabled(true);
                value.setText(String.valueOf(buffer));
            } else {
                value.setText("-");
                value.setEnabled(false);
            }
        });

        this.add(title, new CellBuilder().x(0).build());
        this.add(value, new CellBuilder().x(1).build());
        this.add(val, new CellBuilder().x(2).build());
    }

    @Override
    public void setAlarmValue(double value) {
        if (isEnabled()) {
            this.value.setText(String.valueOf(value));
        } else {
            buffer = value;
        }
    }

    @Override
    public double getAlarmValue() {
        if (isEnabled()) {
            String val = value.getText();
            if (StringHelper.isDouble(val)) return Double.parseDouble(val);
        }
        return Double.NaN;
    }

    @Override
    public boolean isEnabled() {
        return title.isSelected();
    }
}