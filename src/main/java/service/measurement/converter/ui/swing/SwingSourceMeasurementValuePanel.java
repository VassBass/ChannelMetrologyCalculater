package service.measurement.converter.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.converter.ui.MeasurementValuePanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class SwingSourceMeasurementValuePanel extends DefaultPanel implements MeasurementValuePanel {

    private final DefaultTextField value;
    private final DefaultComboBox measurementValue;

    public SwingSourceMeasurementValuePanel() {
        super();

        value = new DefaultTextField(10);
        measurementValue = new DefaultComboBox(false);
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
        String val = value.getText();
        return StringHelper.isDouble(val) ? Double.parseDouble(val) : Double.NaN;
    }
}
