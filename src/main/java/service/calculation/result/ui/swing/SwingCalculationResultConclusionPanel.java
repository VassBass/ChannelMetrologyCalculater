package service.calculation.result.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import service.calculation.dto.Protocol;
import service.calculation.result.ui.CalculationResultConclusionPanel;

import java.awt.*;
import java.util.List;


public class SwingCalculationResultConclusionPanel extends DefaultPanel implements CalculationResultConclusionPanel {
    private static final String SUITABLE_TEXT = "Канал придатний";
    private static final String NOT_SUITABLE_TEXT = "Канал не придатний";

    private final ButtonCell result;
    private final DefaultComboBox conclusion;

    public SwingCalculationResultConclusionPanel(Protocol protocol) {
        super();

        String resultText = SUITABLE_TEXT;
        Color resultColorBackground = Color.GREEN;
        Color resultColorForeground = Color.BLACK;
        if (protocol.getChannel().getAllowableErrorPercent() < protocol.getRelativeError()) {
            resultText = NOT_SUITABLE_TEXT;
            resultColorBackground = Color.RED;
            resultColorForeground = Color.WHITE;
        }
        result = new ButtonCell(resultColorBackground, resultColorForeground, resultText);

        conclusion = new DefaultComboBox(true);
    }

    @Override
    public boolean isSuitable() {
        return result.getText().equalsIgnoreCase(SUITABLE_TEXT);
    }

    @Override
    public String getConclusion() {
        return conclusion.getSelectedItem();
    }

    @Override
    public void setConclusions(List<String> conclusions) {
        conclusion.setList(conclusions);
    }
}
