package service.calculation.persons.ui.swing;

import localization.Labels;
import localization.Messages;
import model.OS;
import model.ui.TitledComboBox;
import service.calculation.persons.CalculationPersonValuesBuffer;
import service.calculation.persons.ui.CalculationPersonsOSChooserPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class SwingCalculationPersonsOSChooserPanel extends TitledComboBox implements CalculationPersonsOSChooserPanel {
    private static final String FORM_PROTOCOL_FOR_OS = "formProtocolForOS";
    private static final String FORM_PROTOCOL_FOR_OS_TOOLTIP = "formProtocolForOSTooltip";

    private static final Map<String, String> messages = Messages.getMessages(SwingCalculationPersonsOSChooserPanel.class);

    public SwingCalculationPersonsOSChooserPanel() {
        super(false, messages.get(FORM_PROTOCOL_FOR_OS) + Labels.COLON, Color.BLACK);
        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();

        this.setList(Arrays.asList(OS.WINDOWS.getName(), OS.LINUX.getName()));
        this.setToolTipText(messages.get(FORM_PROTOCOL_FOR_OS_TOOLTIP));
        this.setSelectedItem(buffer.getOs().getName());
    }

    @Override
    public OS getOS() {
        return this.getSelectedString().equals(OS.WINDOWS.getName()) ? OS.WINDOWS : OS.LINUX;
    }
}
