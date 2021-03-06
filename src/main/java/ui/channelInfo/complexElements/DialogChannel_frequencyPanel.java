package ui.channelInfo.complexElements;

import converters.VariableConverter;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DialogChannel_frequencyPanel extends JPanel {
    private static final String DEFAULT_FREQUENCY_VALUE = "2.00";
    private static final String NEXT_DATE = " Дата наступної перевірки: ";
    private static final String ZERO = "0.00";
    private static final String FREQUENCY_CONTROL = "Міжконтрольний інтервал";

    private final DialogChannel parent;

    private JTextField frequency;
    private JLabel nextDateLabel;
    private JLabel nextDate;

    public DialogChannel_frequencyPanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.frequency = new JTextField(DEFAULT_FREQUENCY_VALUE,4);
        this.nextDateLabel = new JLabel(NEXT_DATE);
        this.nextDate = new JLabel();
    }

    private void setReactions() {
        this.frequency.addFocusListener(this.changeFocusFromFrequency);
    }

    private void build() {
        this.setBackground(Color.WHITE);
        this.setNextDate(Calendar.getInstance());
        this.add(this.frequency);
        this.add(this.nextDateLabel);
        this.add(this.nextDate);
        TitledBorder border = BorderFactory.createTitledBorder(FREQUENCY_CONTROL);
        this.setBorder(border);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateFrequency(double frequency, Calendar date) {
        String frequencyString = VariableConverter.roundingDouble2(frequency, Locale.ENGLISH);
        this.frequency.setText(frequencyString);
        if (date == null){
            this.setNextDate(VariableConverter.stringToDate(this.parent.datePanel.getDate()));
        }else {
            this.setNextDate(date);
        }
    }

    private final FocusListener changeFocusFromFrequency = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            frequency.selectAll();
            parent.resetSpecialCharactersPanel();
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (frequency.getText().length()==0) {
                frequency.setText(DEFAULT_FREQUENCY_VALUE);
            }
            String forCheck = frequency.getText();
            String afterCheck = VariableConverter.doubleString(forCheck);
            if (afterCheck.equals(ZERO)) {
                frequency.setText(DEFAULT_FREQUENCY_VALUE);
            }else {
                frequency.setText(afterCheck);
            }

            setNextDate(VariableConverter.stringToDate(parent.datePanel.getDate()));
        }
    };

    public void setNextDate(Calendar date){
        if (date != null) {
            Calendar nextDate = new GregorianCalendar();
            nextDate.setTimeInMillis(date.getTimeInMillis() + ((long) (31536000000L * Double.parseDouble(this.frequency.getText()))));
            this.nextDate.setText(VariableConverter.dateToString(nextDate));
        }
    }

    public double getFrequency(){
        return Double.parseDouble(this.frequency.getText());
    }
}