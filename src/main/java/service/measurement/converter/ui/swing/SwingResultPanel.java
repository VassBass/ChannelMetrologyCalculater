package service.measurement.converter.ui.swing;

import model.ui.TitledTextArea;
import service.measurement.converter.ui.ResultPanel;

public class SwingResultPanel extends TitledTextArea implements ResultPanel {
    private static final String TITLE_TEXT = "Результтат";

    public SwingResultPanel() {
        super(Integer.MAX_VALUE, 50, TITLE_TEXT);
    }

    @Override
    public void appendResult(String sourceMeasurementValue, double sourceValue, String resultMeasurementValue, double resultValue) {
        String prefix = this.getText();
        String postfix = String.format("%s %s = %s %s", sourceMeasurementValue, sourceValue, resultMeasurementValue, resultValue);
        this.setText(prefix + postfix + '\n');
    }
}
