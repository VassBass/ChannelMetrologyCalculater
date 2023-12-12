package service.measurement.converter.ui.swing;

import localization.Labels;
import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.measurement.converter.ui.MeasurementValuePanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.List;

public class SwingSourceMeasurementValuePanel extends DefaultPanel implements MeasurementValuePanel {

    private final DefaultTextField value;
    private final DefaultComboBox measurementValue;

    public SwingSourceMeasurementValuePanel() {
        super();

        value = new DefaultTextField(10);
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        measurementValue = new DefaultComboBox(false);

        this.add(value, new CellBuilder().x(0).build());
        this.add(measurementValue, new CellBuilder().x(1).build());
    }

    @Override
    public void setMeasurementValuesList(@Nonnull List<String> measurementValuesList) {
        measurementValue.setList(measurementValuesList);
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getSelectedString();
    }

    @Override
    public double getValue() {
        String val = value.getText().replaceAll(Labels.COMMA, Labels.DOT);
        return StringHelper.isDouble(val) ? Double.parseDouble(val) : Double.NaN;
    }
}
