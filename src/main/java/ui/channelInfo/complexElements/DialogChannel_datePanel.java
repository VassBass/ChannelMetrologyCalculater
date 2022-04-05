package ui.channelInfo.complexElements;

import constants.CalendarConstants;
import converters.VariableConverter;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogChannel_datePanel extends JPanel {
    private static final String DOT = ".";

    private final DialogChannel parent;

    private JTextField day;
    private JTextField month;
    private JTextField year;
    private JLabel dot1;
    private JLabel dot2;

    public DialogChannel_datePanel(DialogChannel parent){

        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        this.day = new JTextField(2);
        this.day.setHorizontalAlignment(SwingConstants.CENTER);
        this.dot1 = new JLabel(DOT);
        this.month = new JTextField(2);
        this.month.setHorizontalAlignment(SwingConstants.CENTER);
        this.dot2 = new JLabel(DOT);
        this.year = new JTextField(4);
        this.year.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setReactions(){
        this.day.addFocusListener(this.dayFocus);
        this.month.addFocusListener(this.monthFocus);
        this.year.addFocusListener(this.yearFocus);
    }

    private void build(){
        this.setDate(Calendar.getInstance());
        this.add(this.day);
        this.add(this.dot1);
        this.add(this.month);
        this.add(this.dot2);
        this.add(this.year);
    }

    public void setDate(Calendar date) {
        if (date != null) {
            this.setField(CalendarConstants.DAY, date.get(Calendar.DAY_OF_MONTH));
            this.setField(CalendarConstants.MONTH, date.get(Calendar.MONTH));
            this.setField(CalendarConstants.YEAR, date.get(Calendar.YEAR));
        }
    }

    private void setField(CalendarConstants field, int value) {
        switch (field) {
            case DAY:
                if (value<10) {
                    this.day.setText("0".concat(String.valueOf(value)));
                }else {
                    this.day.setText(String.valueOf(value));
                }
                break;
            case MONTH:
                int v = value+1;
                if (v<10) {
                    this.month.setText("0".concat(String.valueOf(v)));
                }else {
                    this.month.setText(String.valueOf(v));
                }
                break;
            case YEAR:
                year.setText(String.valueOf(value));
        }
    }

    public String getDate() {
        return this.day.getText() + "." + this.month.getText() + "." + this.year.getText();
    }

    private final FocusListener dayFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            day.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (day.getText().length() > 2 || day.getText().length() == 0) {
                day.setText("01");
            } else {
                char[] chars = day.getText().toCharArray();
                if (Character.isDigit(chars[0])) {
                    if (chars.length == 1) {
                        if (Character.getNumericValue(chars[0]) == 0) {
                            day.setText("01");
                        } else {
                            day.setText("0".concat(String.valueOf(chars[0])));
                        }
                    } else if (Character.isDigit(chars[1])) {
                        int dayInt = Integer.parseInt(String.valueOf(chars));
                        Calendar date = new GregorianCalendar(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()) - 1, 1);
                        if (dayInt != 0) {
                            if (dayInt > date.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                day.setText(String.valueOf(date.getActualMaximum(Calendar.DAY_OF_MONTH)));
                            }
                        } else {
                            day.setText("01");
                        }
                    } else {
                        day.setText("01");
                    }
                } else {
                    day.setText("01");
                }
            }

            parent.frequencyPanel.setNextDate(VariableConverter.stringToDate(getDate()));
        }
    };

    private final FocusListener monthFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            month.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (month.getText().length() > 2 || month.getText().length() == 0) {
                month.setText("01");
            } else {
                char[] chars = month.getText().toCharArray();
                if (Character.isDigit(chars[0])) {
                    if (chars.length == 1) {
                        if (Character.getNumericValue(chars[0]) == 0) {
                            month.setText("01");
                        } else {
                            month.setText("0".concat(String.valueOf(chars[0])));
                        }
                    } else if (Character.isDigit(chars[1])) {
                        int monthInt = Integer.parseInt(String.valueOf(chars));
                        if (monthInt == 0 || monthInt > 12) {
                            month.setText("01");
                        }
                    } else {
                        month.setText("01");
                    }
                } else {
                    month.setText("01");
                }
            }

            parent.frequencyPanel.setNextDate(VariableConverter.stringToDate(getDate()));
        }
    };

    private final FocusListener yearFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            year.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (year.getText().length() == 2 || year.getText().length() == 4) {
                char[] chars = year.getText().toCharArray();
                if (chars.length == 2) {
                    if (Character.isDigit(chars[0]) && Character.isDigit(chars[1])) {
                        if (Integer.parseInt(String.valueOf(chars)) == 0) {
                            year.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                        } else {
                            year.setText("20".concat(String.valueOf(chars)));
                        }
                    } else {
                        year.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                    }
                } else {
                    if (Character.isDigit(chars[0]) && Character.isDigit(chars[1]) && Character.isDigit(chars[2]) && Character.isDigit(chars[3])) {
                        int yearInt = Integer.parseInt(String.valueOf(chars));
                        if (yearInt < 1990 || yearInt > 2100) {
                            year.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                        }
                    } else {
                        year.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                    }
                }
            } else {
                year.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            }

            parent.frequencyPanel.setNextDate(VariableConverter.stringToDate(getDate()));
        }
    };
}