package service.calculation.result.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.protocol.Protocol;
import service.calculation.result.ui.CalculationResultConclusionPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;


public class SwingCalculationResultConclusionPanel extends DefaultPanel implements CalculationResultConclusionPanel {
    private static final String SUITABLE = "suitable";
    private static final String NOT_SUITABLE = "notSuitable";
    private static final String CONCLUSION_SETUP = "conclusionSetup";
    private static final String CONCLUSION_RANGE_SETUP = "conclusionRangeSetup";
    private static final String CONCLUSION_LIKE_INDICATOR = "conclusionLikeIndicator";
    private static final String CONCLUSION_ALARM = "conclusionAlarm";

    private final ButtonCell result;
    private final DefaultComboBox conclusion;

    private final Map<String, String> labels;
    private final Map<String, String> messages;

    public SwingCalculationResultConclusionPanel(Protocol protocol) {
        super();
        labels = Labels.getLabels(SwingCalculationResultConclusionPanel.class);
        messages = Messages.getMessages(SwingCalculationResultConclusionPanel.class);

        String resultText = labels.get(SUITABLE);
        Color resultColorBackground = Color.GREEN;
        Color resultColorForeground = Color.BLACK;
        if (protocol.getChannel().getAllowableErrorPercent() < protocol.getRelativeError()) {
            resultText = labels.get(NOT_SUITABLE);
            resultColorBackground = Color.RED;
            resultColorForeground = Color.WHITE;
        }
        result = new ButtonCell(resultColorBackground, resultColorForeground, resultText);
        conclusion = new DefaultComboBox(true);
        conclusion.setList(createDefaultConclusions(protocol));

        this.add(result, new CellBuilder().y(0).build());
        this.add(conclusion, new CellBuilder().y(1).build());
    }

    @Override
    public boolean isSuitable() {
        return result.getText().equalsIgnoreCase(labels.get(SUITABLE));
    }

    @Override
    public String getConclusion() {
        return conclusion.getSelectedString();
    }

    public List<String> createDefaultConclusions(Protocol protocol) {
        List<String> conclusions = new ArrayList<>();
        conclusions.add(EMPTY);
        if (!Double.isNaN(protocol.getAlarm())) {
            String alarmText = String.format(messages.get(CONCLUSION_ALARM) + "%s %s",
                    protocol.getAlarm(), protocol.getChannel().getMeasurementValue());
            conclusions.add(alarmText);
        }
        if (protocol.getChannel().getAllowableErrorPercent() < protocol.getRelativeError()) {
            conclusions.add(messages.get(CONCLUSION_LIKE_INDICATOR));
        }
        conclusions.add(messages.get(CONCLUSION_SETUP));
        conclusions.add(messages.get(CONCLUSION_RANGE_SETUP));
        return conclusions;
    }
}
