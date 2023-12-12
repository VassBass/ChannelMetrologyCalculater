package service.sensor_error.info.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.TitledTextField;
import service.error_calculater.MxParserErrorCalculater;
import service.sensor_error.info.ui.SensorErrorInfoErrorPanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class SwingSensorErrorInfoErrorPanel extends TitledTextField implements SensorErrorInfoErrorPanel {
    private static final String ERROR_FORMULA = "errorFormula";
    private static final String TOOLTIP = "tooltip";

    public SwingSensorErrorInfoErrorPanel() {
        super(20, Labels.getLabels(SwingSensorErrorInfoErrorPanel.class).get(ERROR_FORMULA), Color.BLACK);
        this.setToolTipText(Messages.getMessages(SwingSensorErrorInfoErrorPanel.class).get(TOOLTIP));
    }

    @Override
    public void setErrorFormula(@Nonnull String errorFormula) {
        this.setText(errorFormula);
    }

    @Nullable
    @Override
    public String getErrorFormula() {
        String formula = this.getText();
        if (MxParserErrorCalculater.isFormulaValid(formula)) {
            this.setTitleColor(Color.BLACK);
            return formula;
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }
}
