package service.measurement.converter.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.TitledTextArea;
import service.measurement.converter.ui.ResultPanel;

public class SwingResultPanel extends TitledTextArea implements ResultPanel {
    private final StringBuilder buffer;

    public SwingResultPanel() {
        super(Integer.MAX_VALUE, 50, Labels.getRootLabels().get(RootLabelName.RESULT));
        buffer = new StringBuilder();
        this.setEditable(false);
    }

    @Override
    public void appendResult(String sourceMeasurementValue, double sourceValue, String resultMeasurementValue, double resultValue) {
        buffer.append(String.format("%s %s = %s %s%n", sourceValue, sourceMeasurementValue, resultValue, resultMeasurementValue));
        this.setText(buffer.toString());
    }
}
