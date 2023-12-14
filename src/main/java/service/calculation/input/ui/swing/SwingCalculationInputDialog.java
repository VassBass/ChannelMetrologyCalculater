package service.calculation.input.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.input.CalculationInputValuesBuffer;
import service.calculation.input.ui.SwingCalculationInputContext;
import service.calculation.protocol.Protocol;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingCalculationInputDialog extends DefaultDialog implements CalculationCollectDialog {
    private static final String ALARM_VALUE_NOT_VALID = "alarmValueNotValid";
    private static final String SET_VALUES_NOT_VALID = "setValuesNotValid";
    private static final String GET_VALUES_NOT_VALID = "getValuesNotValid";

    private static final Map<String, String> labels = Labels.getRootLabels();
    private final Map<String, String> messages;

    private final SwingCalculationInputAlarmPanel alarmPanel;
    private final SwingCalculationInputMeasurementPanel measurementPanel;
    private final SwingCalculationInputNumberFormatPanel numberFormatPanel;

    public SwingCalculationInputDialog(@Nonnull ApplicationScreen applicationScreen,
                                       @Nonnull CalculationConfigHolder configHolder,
                                       @Nonnull SwingCalculationInputContext context) {
        super(applicationScreen, labels.get(RootLabelName.INPUT_DATA));
        messages = Messages.getMessages(SwingCalculationInputDialog.class);

        DefaultPanel panel = new DefaultPanel();

        alarmPanel = context.getElement(SwingCalculationInputAlarmPanel.class);
        measurementPanel = context.getElement(SwingCalculationInputMeasurementPanel.class);
        SwingCalculationInputButtonsPanel buttonsPanel = context.getElement(SwingCalculationInputButtonsPanel.class);
        numberFormatPanel = context.getElement(SwingCalculationInputNumberFormatPanel.class);

        panel.add(alarmPanel, new CellBuilder().x(0).y(0).fill(HORIZONTAL).width(1).build());
        panel.add(numberFormatPanel, new CellBuilder().x(1).y(0).width(1).build());
        panel.add(measurementPanel, new CellBuilder().x(0).y(1).width(2).build());
        panel.add(buttonsPanel, new CellBuilder().x(0).y(2).width(2).build());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                buttonsPanel.clickCloseButton();
            }
        });
        numberFormatPanel.clickButtonLook();

        int width = configHolder.getInputDialogWidth();
        int height = configHolder.getInputDialogHeight();
        int inputSize = Objects.nonNull(measurementPanel.getInputs()) ? measurementPanel.getInputs().size() : 3;
        for (int i = 3; i < inputSize; i++) height += 45;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public boolean fillProtocol(Protocol protocol) {
        if (alarmPanel.isEnabled()) {
            double alarm = alarmPanel.getAlarmValue();
            if (Double.isNaN(alarm)) {
                JOptionPane.showMessageDialog(
                        this,
                        messages.get(ALARM_VALUE_NOT_VALID),
                        labels.get(RootLabelName.INPUT_NOT_VALID),
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
            protocol.setAlarm(alarm);
        }

        TreeMap<Double, Double> input = measurementPanel.getInputs();
        if (Objects.isNull(input)) {
            JOptionPane.showMessageDialog(
                    this,
                    messages.get(SET_VALUES_NOT_VALID),
                    labels.get(RootLabelName.INPUT_NOT_VALID),
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        TreeMap<Double, double[]> output = measurementPanel.getMeasurementValues();
        if (Objects.isNull(output)) {
            JOptionPane.showMessageDialog(
                    this,
                    messages.get(GET_VALUES_NOT_VALID),
                    labels.get(RootLabelName.INPUT_NOT_VALID),
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        protocol.setInput(input);
        protocol.setOutput(output);
        protocol.setValuesDecimalPoint(numberFormatPanel.getValueDecimalPoint());
        protocol.setPercentsDecimalPoint(numberFormatPanel.getPercentDecimalPoint());

        CalculationInputValuesBuffer buffer = CalculationInputValuesBuffer.getInstance();
        buffer.setValueDecimalPoint(numberFormatPanel.getValueDecimalPoint());
        buffer.setPercentDecimalPoint(numberFormatPanel.getPercentDecimalPoint());

        return true;
    }
}
