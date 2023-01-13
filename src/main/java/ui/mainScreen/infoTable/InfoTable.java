package ui.mainScreen.infoTable;

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
    protected static final String TEXT_DEFAULT_NEXT_DATE = "XX.XX.XXXX";
    protected static final String TEXT_DASH = " - ";

    protected final ButtonCell nextDate;
    protected final ButtonCell path;
    protected final ButtonCell sensor;

    public InfoTable(){
        super(new GridBagLayout());

        ButtonCell nextDateTitle = new ButtonCell(true, TEXT_NEXT_DATE);
        ButtonCell pathTitle = new ButtonCell(true, TEXT_PATH);
        ButtonCell sensorTitle = new ButtonCell(true, TEXT_SENSOR);

        nextDate = new ButtonCell(false, TEXT_DEFAULT_NEXT_DATE);
        path = new ButtonCell(false, TEXT_DASH);
        sensor = new ButtonCell(false, TEXT_DASH);

        this.add(nextDateTitle, new CellBuilder().coordinates(0,0).create());
        this.add(pathTitle, new CellBuilder().coordinates(1,0).create());
        this.add(sensorTitle, new CellBuilder().coordinates(2,0).create());
        this.add(nextDate, new CellBuilder().coordinates(0,1).create());
        this.add(path, new CellBuilder().coordinates(1,1).create());
        this.add(sensor, new CellBuilder().coordinates(2,1).create());
    }

    public void updateInfo(Channel channel){
        if (channel == null){
            nextDate.setText(TEXT_DEFAULT_NEXT_DATE);
            nextDate.setBackground(Color.WHITE);
            path.setText(TEXT_DASH);
            sensor.setText(TEXT_DASH);
        }else {
            nextDate.setText(VariableConverter.dateToString(channel._getNextDate()));
            nextDate.setBackground(setBackgroundColorFromDate(channel._getNextDate()));
            path.setText(channel._getFullPath());
            sensor.setText(channel.getSensor().getType());
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