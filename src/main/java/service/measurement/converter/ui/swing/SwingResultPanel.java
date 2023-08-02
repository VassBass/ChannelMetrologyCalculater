package service.measurement.converter.ui.swing;

import model.ui.TitledTextArea;
import service.measurement.converter.ui.ResultPanel;

public class SwingResultPanel extends TitledTextArea implements ResultPanel {
    private static final String TITLE_TEXT = "Результтат";

    private final StringBuilder buffer;

    public SwingResultPanel() {
        super(Integer.MAX_VALUE, 50, TITLE_TEXT);
        buffer = new StringBuilder();
    }

    @Override
    public void appendResult(String sourceMeasurementValue, double sourceValue, String resultMeasurementValue, double resultValue) {
        buffer.append(this.getText());
        buffer.append(String.format("%s %s = %s %s%n", sourceMeasurementValue, sourceValue, resultMeasurementValue, resultValue));
        this.setText(buffer.toString());
    }
}
