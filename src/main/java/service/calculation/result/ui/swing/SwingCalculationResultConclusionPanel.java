package service.calculation.result.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.protocol.Protocol;
import service.calculation.result.ui.CalculationResultConclusionPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;


public class SwingCalculationResultConclusionPanel extends DefaultPanel implements CalculationResultConclusionPanel {
    private static final String SUITABLE_TEXT = "Канал придатний";
    private static final String NOT_SUITABLE_TEXT = "Канал не придатний";
    private static final String CONCLUSION_SETUP = "Порада: налаштувати вимірювальний канал";
    private static final String CONCLUSION_RANGE_SETUP =
            "Порада: для кращих показів налаштуйте вимірювальний канал на вказаний діапазон вимірювання";
    private static final String ALARM_CONCLUSION_PREFIX = "Сигналізація спрацювала при заданні = ";

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
        conclusion.setList(createDefaultConclusions(protocol));

        this.add(result, new CellBuilder().y(0).build());
        this.add(conclusion, new CellBuilder().y(1).build());
    }

    @Override
    public boolean isSuitable() {
        return result.getText().equalsIgnoreCase(SUITABLE_TEXT);
    }

    @Override
    public String getConclusion() {
        return conclusion.getSelectedString();
    }

    public List<String> createDefaultConclusions(Protocol protocol) {
        List<String> conclusions = new ArrayList<>();
        conclusions.add(EMPTY);
        if (!Double.isNaN(protocol.getAlarm())) {
            String alarmText = String.format(ALARM_CONCLUSION_PREFIX + "%s %s",
                    protocol.getAlarm(), protocol.getChannel().getMeasurementValue());
            conclusions.add(alarmText);
        }
        conclusions.add(CONCLUSION_SETUP);
        conclusions.add(CONCLUSION_RANGE_SETUP);
        return conclusions;
    }
}
