package ui.mainScreen;

import constants.Strings;
import converters.VariableConverter;
import model.Channel;
import ui.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class InfoTable extends JPanel {

    private JButton nextDateTitle, pathTitle, sensorTitle;
    private JButton nextDate, path, sensor;

    public InfoTable(){
        super(new GridBagLayout());

        this.createElements();
        this.build();
    }

    public void createElements() {
        this.nextDateTitle = new ButtonCell(true, Strings.NEXT_DATE);
        this.pathTitle = new ButtonCell(true, Strings.PATH);
        this.sensorTitle = new ButtonCell(true, Strings.SENSOR);

        this.nextDate = new ButtonCell(false,"XX.XX.XXXX");
        this.path = new ButtonCell(false," - ");
        this.sensor = new ButtonCell(false, " - ");
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
            this.nextDate.setText("XX.XX.XXXX");
            this.nextDate.setBackground(Color.white);
            this.path.setText(" - ");
            this.sensor.setText(" - ");
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
            return Color.yellow;
        }else if (d < 0L){
            return Color.red;
        }else {
            return Color.white;
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
