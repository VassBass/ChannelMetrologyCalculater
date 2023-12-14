package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoErrorFormulaPanel;
import service.error_calculater.MxParserErrorCalculater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class SwingCalibratorInfoErrorFormulaPanel extends TitledTextField implements CalibratorInfoErrorFormulaPanel {
    private static final String ERROR_FORMULA = "errorFormula";
    private static final String ERROR_FORMULA_TOOLTIP = "errorFormulaTooltip";

    public SwingCalibratorInfoErrorFormulaPanel() {
        super(20, Labels.getLabels(SwingCalibratorInfoErrorFormulaPanel.class).get(ERROR_FORMULA));
        this.setToolTipText(Messages.getMessages(SwingCalibratorInfoErrorFormulaPanel.class).get(ERROR_FORMULA_TOOLTIP));
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

    @Override
    public void setErrorFormula(@Nonnull String value) {
        this.setText(value);
    }
}
