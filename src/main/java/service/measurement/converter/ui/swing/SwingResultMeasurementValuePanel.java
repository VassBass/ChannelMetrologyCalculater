package service.measurement.converter.ui.swing;

import model.ui.DefaultComboBox;
import service.measurement.converter.ui.MeasurementValuePanel;

import javax.annotation.Nonnull;
import java.util.List;

public class SwingResultMeasurementValuePanel extends DefaultComboBox implements MeasurementValuePanel {
    public SwingResultMeasurementValuePanel() {
        super(false);
    }

    @Override
    public void setMeasurementValuesList(@Nonnull List<String> measurementValuesList) {
        this.setList(measurementValuesList);
    }

    @Override
    public String getMeasurementValue() {
        return this.getSelectedString();
    }

    @Override
    public double getValue() {
        return Double.NaN;
    }
}
