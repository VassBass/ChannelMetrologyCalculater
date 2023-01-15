package ui.channelInfo.panel;

import constants.CalendarConstants;
import converters.VariableConverter;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PanelDate extends JPanel {
    private static final String SEPARATOR = ".";
    private static final String THIS_DATE = "Дата останньої перевірки";

    private final DialogChannel parent;

    private final JTextField day;
    private final JTextField month;
    private final JTextField year;

    public PanelDate(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        day = new DayTextField();
        month = new MonthTextField();
        year = new YearTextField();

        this.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(THIS_DATE);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);

        this.add(day, new Cell(0,0));
        this.add(new JLabel(SEPARATOR), new Cell(1,0));
        this.add(month, new Cell(2,0));
        this.add(new JLabel(SEPARATOR), new Cell(3,0));
        this.add(year, new Cell(4,0));
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateDate(@Nonnull Calendar date) {
        this.setField(CalendarConstants.DAY, date.get(Calendar.DAY_OF_MONTH));
        this.setField(CalendarConstants.MONTH, date.get(Calendar.MONTH));
        this.setField(CalendarConstants.YEAR, date.get(Calendar.YEAR));
    }

    private void setField(@Nonnull CalendarConstants field,@Nonnegative int value) {
        switch (field) {
            case DAY:
                day.setText(value < 10 ? "0" + value : String.valueOf(value));
                break;
            case MONTH:
                int m = value+1;
                month.setText(m < 10 ? "0" + m : String.valueOf(m));
                break;
            case YEAR:
                year.setText(String.valueOf(value));
        }
    }

    public String getDate() {
        return day.getText() + SEPARATOR + month.getText() + SEPARATOR + year.getText();
    }

    /**
     * Text field for day of month
     */
    private class DayTextField extends JTextField {

        private DayTextField(){
            super(2);
            this.setHorizontalAlignment(SwingConstants.CENTER);

            this.addFocusListener(dayFocus);
        }

        @SuppressWarnings({"FieldCanBeLocal", "MagicConstant"})
        private final FocusListener dayFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                day.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
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
                            } else day.setText("01");
                        } else day.setText("01");
                    } else day.setText("01");
                }

                parent.panelFrequency.updateNextDate(VariableConverter.stringToDate(getDate()));
            }
        };
    }

    /**
     * Text field for month of the year
     */
    private class MonthTextField extends JTextField {

        private MonthTextField(){
            super(2);
            this.setHorizontalAlignment(SwingConstants.CENTER);

            this.addFocusListener(monthFocus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener monthFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                month.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
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
                        } else month.setText("01");
                    } else month.setText("01");
                }

                parent.panelFrequency.updateNextDate(VariableConverter.stringToDate(getDate()));
            }
        };
    }

    /**
     * Text field for year
     */
    private class YearTextField extends JTextField {

        private YearTextField(){
            super(4);
            this.setHorizontalAlignment(SwingConstants.CENTER);

            this.addFocusListener(yearFocus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener yearFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                year.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
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

                parent.panelFrequency.updateNextDate(VariableConverter.stringToDate(getDate()));
            }
        };
    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.fill = BOTH;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}