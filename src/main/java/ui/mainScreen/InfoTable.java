package ui.mainScreen;

import converters.VariableConverter;
import model.Channel;
import ui.model.ButtonCell;
import ui.model.CellBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class InfoTable extends JPanel {
    private static final String TEXT_NEXT_DATE = "Дата наступної перевірки";
    private static final String TEXT_PATH = "Розташування";
    private static final String TEXT_SENSOR = "Первинний вимірювальний пристрій";
    private static final String TEXT_DEFAULT_NEXT_DATE = "XX.XX.XXXX";
    private static final String TEXT_DASH = " - ";

    private final JButton nextDate;
    private final JButton path;
    private final JButton sensor;

    public InfoTable(){
        super(new GridBagLayout());

        JButton nextDateTitle = new ButtonCell(true, TEXT_NEXT_DATE);
        JButton pathTitle = new ButtonCell(true, TEXT_PATH);
        JButton sensorTitle = new ButtonCell(true, TEXT_SENSOR);

        this.nextDate = new ButtonCell(false, TEXT_DEFAULT_NEXT_DATE);
        this.path = new ButtonCell(false, TEXT_DASH);
        this.sensor = new ButtonCell(false, TEXT_DASH);

        this.add(nextDateTitle, new CellBuilder().coordinates(0,0).create());
        this.add(pathTitle, new CellBuilder().coordinates(1,0).create());
        this.add(sensorTitle, new CellBuilder().coordinates(2,0).create());
        this.add(nextDate, new CellBuilder().coordinates(0,1).create());
        this.add(path, new CellBuilder().coordinates(1,1).create());
        this.add(sensor, new CellBuilder().coordinates(2,1).create());
    }

    public void updateInfo(Channel channel){
        if (channel == null){
            this.nextDate.setText(TEXT_DEFAULT_NEXT_DATE);
            this.nextDate.setBackground(Color.WHITE);
            this.path.setText(TEXT_DASH);
            this.sensor.setText(TEXT_DASH);
        }else {
            this.nextDate.setText(VariableConverter.dateToString(channel._getNextDate()));
            this.nextDate.setBackground(setBackgroundColorFromDate(channel._getNextDate()));
            this.path.setText(channel._getFullPath());
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
}