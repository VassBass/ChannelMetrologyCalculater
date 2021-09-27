package ui.channelInfo.complexElements;

import support.Converter;
import constants.Strings;
import ui.UI_Container;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DialogChannel_frequencyPanel extends JPanel implements UI_Container {
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


    @Override
    public void createElements() {
        this.frequency = new JTextField("1.00",4);
        this.nextDateLabel = new JLabel(" ".concat(Strings.NEXT_DATE).concat(": "));
        this.nextDate = new JLabel();
    }

    @Override
    public void setReactions() {
        this.frequency.addFocusListener(changeFocusFromFrequency);
    }

    @Override
    public void build() {
        this.setNextDate(Calendar.getInstance());
        this.add(frequency);
        this.add(nextDateLabel);
        this.add(nextDate);
    }

    public void update(double frequency, Calendar date) {
        String frequencyString = Converter.roundingDouble2(frequency, Locale.ENGLISH);
        this.frequency.setText(frequencyString);
        if (date == null){
            this.setNextDate(this.parent.datePanel.getDate());
        }else {
            this.setNextDate(date);
        }
    }

    public void update(Calendar date){
        if (date != null) {
            this.setNextDate(date);
        }
    }

    private final FocusListener changeFocusFromFrequency = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            frequency.selectAll();
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (frequency.getText().length()==0) {
                frequency.setText("1.00");
            }
            String forCheck = frequency.getText();
            String afterCheck = Converter.doubleString(forCheck);
            if (afterCheck.equals("0.00")) {
                frequency.setText("1.00");
            }else {
                frequency.setText(afterCheck);
            }

            update(parent.datePanel.getDate());
        }
    };

    private void setNextDate(Calendar date){
        Calendar nextDate = new GregorianCalendar();
        nextDate.setTimeInMillis(date.getTimeInMillis() + ((long) (31536000000L * Double.parseDouble(this.frequency.getText()))));
        this.nextDate.setText(Converter.dateToString(nextDate));
    }

    public double getFrequency(){
        return Double.parseDouble(this.frequency.getText());
    }
}
