package service.converter_tc.ui.swing;

import model.ui.TitledTextArea;
import service.converter_tc.ui.ResultPanel;

import static util.Symbol.*;

public class SwingResultPanel extends TitledTextArea implements ResultPanel {
    private static final String TITLE_TEXT = "Результтат";
    private static final  String TOOLTIP_TEXT = "<html>"
            + "Похибка при перетворенні температури в опір = " + PLUS_MINUS + "1 * 10" + UP_MINUS + UP_SEVEN + "(" + RESISTANCE + "/" + DEGREE_CELSIUS + ")"
            + "<br>"
            + "<br>Похибки при перетворенні опору в температуру:"
            + "<br>- при t менше 0 " + DEGREE_CELSIUS + " = 0.7 " + DEGREE_CELSIUS
            + "<br>- при t більше 0 " + DEGREE_CELSIUS + " = 0.02 " + DEGREE_CELSIUS
            + "</html>";

    public SwingResultPanel() {
        super(Integer.MAX_VALUE, 20, TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);
        this.setEditable(false);
    }

    @Override
    public void setResult(String result) {
        this.setText(result);
    }

    @Override
    public String getResult() {
        return this.getText();
    }
}
