package service.calculation.persons.ui.swing;

import model.OS;
import model.ui.TitledComboBox;
import service.calculation.persons.CalculationPersonValuesBuffer;
import service.calculation.persons.ui.CalculationPersonsOSChooserPanel;

import java.awt.*;
import java.util.Arrays;

public class SwingCalculationPersonsOSChooserPanel extends TitledComboBox implements CalculationPersonsOSChooserPanel {
    private static final String TITLE_TEXT = "Сформувати протокол для ОС:";
    private static final String TOOLTIP_TEXT = "Вихідні файли протоколу для різних операційних систем формуються з різних шаблонів";
    private static final String WINDOWS_TEXT = "Windows";
    private static final String LINUX_TEXT = "Linux";

    public SwingCalculationPersonsOSChooserPanel() {
        super(false, TITLE_TEXT, Color.BLACK);
        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();

        this.setList(Arrays.asList(WINDOWS_TEXT, LINUX_TEXT));
        this.setToolTipText(TOOLTIP_TEXT);
        this.setSelectedItem(buffer.getOs() == OS.WINDOWS ? WINDOWS_TEXT : LINUX_TEXT);
    }

    @Override
    public OS getOS() {
        return this.getSelectedString().equals(WINDOWS_TEXT) ? OS.WINDOWS : OS.LINUX;
    }
}
