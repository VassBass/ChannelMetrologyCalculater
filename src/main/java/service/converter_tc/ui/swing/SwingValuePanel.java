package service.converter_tc.ui.swing;

import model.dto.Measurement;
import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.converter_tc.ConverterTcManager;
import service.converter_tc.ui.ValuePanel;
import util.StringHelper;

import java.awt.*;
import java.util.Arrays;

import static model.ui.builder.CellBuilder.NONE;

public class SwingValuePanel extends TitledPanel implements ValuePanel {
    private static final String TITLE_TEXT = "Вхідна величина";

    private final DefaultTextField numValue;
    private final DefaultComboBox measurementValue;

    public SwingValuePanel(ConverterTcManager manager) {
        super(TITLE_TEXT, Color.BLACK);

        numValue = new DefaultTextField(5);
        measurementValue = new DefaultComboBox(false);
        measurementValue.setList(Arrays.asList(Measurement.OM, Measurement.DEGREE_CELSIUS));
        measurementValue.addItemListener(e -> manager.changingInputMeasurementValue());

        this.add(numValue, new CellBuilder().x(0).fill(NONE).build());
        this.add(measurementValue, new CellBuilder().x(1).fill(NONE).build());
    }

    @Override
    public double getNumericValue() {
        String val = numValue.getText().replaceAll(",", ".");
        if (StringHelper.isDouble(val)) {
            this.setTitleColor(Color.BLACK);
            return Double.parseDouble(val);
        } else {
            this.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getSelectedString();
    }
}
