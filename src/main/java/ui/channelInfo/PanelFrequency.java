package ui.channelInfo;

import converters.VariableConverter;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PanelFrequency extends JPanel {
    private static final String NEXT_DATE = " Дата наступної перевірки: ";
    private static final String FREQUENCY_CONTROL = "Міжконтрольний інтервал";

    private final DialogChannel parent;

    private final JTextField frequency;
    private final JLabel nextDate;

    PanelFrequency(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        frequency = new FrequencyTextField();
        nextDate = new JLabel();

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createTitledBorder(FREQUENCY_CONTROL));

        this.updateNextDate(Calendar.getInstance());

        this.add(frequency);
        this.add(new JLabel(NEXT_DATE));
        this.add(nextDate);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateFrequency(@Nonnegative double frequency) {
        String frequencyString = VariableConverter.roundingDouble2(frequency, Locale.ENGLISH);
        this.frequency.setText(frequencyString);
        this.updateNextDate(VariableConverter.stringToDate(this.parent.panelData.getDate()));
    }

    public void updateNextDate(@Nonnull Calendar date){
        Calendar nextDate = new GregorianCalendar();
        nextDate.setTimeInMillis(date.getTimeInMillis() + ((long) (31536000000L * Double.parseDouble(this.frequency.getText()))));
        this.nextDate.setText(VariableConverter.dateToString(nextDate));
    }

    public double getFrequency(){
        return Double.parseDouble(this.frequency.getText());
    }

    /**
     * TextField for frequency of channel check
     */
    private class FrequencyTextField extends JTextField {
        private static final String DEFAULT_FREQUENCY_VALUE = "2.00";
        private static final String ZERO = "0.00";

        private FrequencyTextField(){
            super(DEFAULT_FREQUENCY_VALUE, 4);

            this.addFocusListener(changeFocus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener changeFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                frequency.selectAll();
                parent.specialCharactersPanel.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (frequency.getText().length() == 0) {
                    frequency.setText(DEFAULT_FREQUENCY_VALUE);
                }
                String forCheck = frequency.getText();
                String afterCheck = VariableConverter.doubleString(forCheck);
                if (afterCheck.equals(ZERO)) {
                    frequency.setText(DEFAULT_FREQUENCY_VALUE);
                }else {
                    frequency.setText(afterCheck);
                }

                updateNextDate(VariableConverter.stringToDate(parent.panelData.getDate()));
            }
        };
    }
}
