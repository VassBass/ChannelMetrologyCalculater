package service.converter_tc.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.TitledTextArea;
import service.converter_tc.ui.ResultPanel;

import static util.Symbol.*;

public class SwingResultPanel extends TitledTextArea implements ResultPanel {
    private static final  String ERROR_TOOLTIP = "errorTooltip";

    public SwingResultPanel() {
        super(Integer.MAX_VALUE, 20, Labels.getRootLabels().get(RootLabelName.RESULT));
        this.setToolTipText(Messages.getMessages(SwingResultPanel.class).get(ERROR_TOOLTIP));
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
