package ui.mainScreen;

import converters.VariableConverter;
import model.Channel;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class InfoTable extends JPanel {
    public static final String NEXT_DATE = "Дата наступної перевірки";
    public static final String PATH = "Розташування";
    public static final String SENSOR = "Первинний вимірювальний пристрій";
    public static final String DEFAULT_NEXT_DATE = "XX.XX.XXXX";
    public static final String DASH = " - ";

    private JButton nextDateTitle, pathTitle, sensorTitle;
    private JButton nextDate, path, sensor;

    public InfoTable(){
        super(new GridBagLayout());

        this.createElements();
        this.build();
    }

    public void createElements() {
        this.nextDateTitle = new ButtonCell(true, NEXT_DATE);
        this.pathTitle = new ButtonCell(true, PATH);
        this.sensorTitle = new ButtonCell(true, SENSOR);

        this.nextDate = new ButtonCell(false, DEFAULT_NEXT_DATE);
        this.path = new ButtonCell(false, DASH);
        this.sensor = new ButtonCell(false, DASH);
    }

    public void build() {
        this.add(this.nextDateTitle, new Cell(0, 0));
        this.add(this.pathTitle, new Cell(1, 0));
        this.add(this.sensorTitle, new Cell(2, 0));
        this.add(this.nextDate, new Cell(0, 1));
        this.add(this.path, new Cell(1, 1));
        this.add(this.sensor, new Cell(2, 1));
    }

    public void updateInfo(Channel channel){
        if (channel == null){
            this.nextDate.setText(DEFAULT_NEXT_DATE);
            this.nextDate.setBackground(Color.WHITE);
            this.path.setText(DASH);
            this.sensor.setText(DASH);
        }else {
            this.nextDate.setText(VariableConverter.dateToString(channel.getNextDate()));
            this.nextDate.setBackground(setBackgroundColorFromDate(channel.getNextDate()));
            this.path.setText(channel.getFullPath());
            this.sensor.setText(channel.getSensor().getType());
        }
    }

    private Color setBackgroundColorFromDate(Calendar date){
        long d;
        if (date != null) {
            d = date.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        }else{
            d = -1L;
        }
        if (d <= 7776000000L && d >= 0L){
            return Color.YELLOW;
        }else if (d < 0L){
            return Color.RED;
        }else {
            return Color.WHITE;
        }
    }

    private static class Cell extends GridBagConstraints{

        protected Cell(int x, int y){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;

            this.gridx = x;
            this.gridy = y;
        }
    }
}