package service.measurement_transformer.tc.ui.swing;

import model.ui.TitledTextField;
import service.measurement_transformer.tc.ui.ResultPanel;
import util.StringHelper;

import static util.StringHelper.FOR_LAST_ZERO;

public class SwingResultPanel extends TitledTextField implements ResultPanel {
    private static final String TITLE_TEXT = "Результтат";

    public SwingResultPanel() {
        super(20, TITLE_TEXT);
        this.setEditable(false);
    }

    @Override
    public void setResult(double result) {
        this.setText(StringHelper.roundingDouble(result, FOR_LAST_ZERO));
    }
}
