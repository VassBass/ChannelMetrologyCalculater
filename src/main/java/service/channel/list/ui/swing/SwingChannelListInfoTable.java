package service.channel.list.ui.swing;

import model.dto.Channel;
import model.ui.ButtonCell;
import model.ui.builder.CellBuilder;
import service.channel.list.ChannelListService;
import service.channel.list.ui.ChannelListInfoTable;
import util.DateHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingChannelListInfoTable extends JPanel implements ChannelListInfoTable {
    private static final String COLUMN_NEXT_DATE = "Дата наступної перевірки";
    private static final String COLUMN_PATH = "Розташування";
    private static final String COLUMN_SENSOR = "Первинний вимірювальний пристрій";
    private static final String DEFAULT_NEXT_DATE = "XX.XX.XXXX";
    private static final String DASH = " - ";
    private static final String EXTRAORDINARY = "Позачерговий";

    private final ChannelListService service;

    private final ButtonCell nextDate;
    private final ButtonCell path;
    private final ButtonCell sensor;

    public SwingChannelListInfoTable(ChannelListService service){
        super(new GridBagLayout());
        this.service = service;

        ButtonCell nextDateHeader = new ButtonCell(HEADER, COLUMN_NEXT_DATE);
        ButtonCell pathHeader = new ButtonCell(HEADER, COLUMN_PATH);
        ButtonCell sensorHeader = new ButtonCell(HEADER, COLUMN_SENSOR);

        nextDate = new ButtonCell(SIMPLE, DEFAULT_NEXT_DATE);
        path = new ButtonCell(SIMPLE, DASH);
        sensor = new ButtonCell(SIMPLE, DASH);

        this.add(nextDateHeader, new CellBuilder().coordinates(0,0).build());
        this.add(pathHeader, new CellBuilder().coordinates(1,0).build());
        this.add(sensorHeader, new CellBuilder().coordinates(2,0).build());
        this.add(nextDate, new CellBuilder().coordinates(0,1).build());
        this.add(path, new CellBuilder().coordinates(1,1).build());
        this.add(sensor, new CellBuilder().coordinates(2,1).build());
    }

    @Override
    public void updateInfo(Channel channel){
        if (channel == null){
            nextDate.setText(DEFAULT_NEXT_DATE);
            nextDate.setBackground(Color.WHITE);
            path.setText(DASH);
            sensor.setText(DASH);
        }else {
            Calendar nextDateCal = service.getDateOfNextCheck(channel);
            String nextDateText = nextDateCal == null ?
                    EXTRAORDINARY :
                    DateHelper.dateToString(nextDateCal);
            nextDate.setText(nextDateText);
            nextDate.setBackground(setBackgroundColorFromDate(nextDateCal));
            path.setText(service.getFullPath(channel));
            sensor.setText(service.getSensor(channel).getType());
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