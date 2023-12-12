package service.channel.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.Channel;
import model.dto.Sensor;
import model.ui.ButtonCell;
import model.ui.builder.CellBuilder;
import service.channel.list.ChannelListService;
import service.channel.list.ui.ChannelListInfoTable;
import util.DateHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Map;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingChannelListInfoTable extends JPanel implements ChannelListInfoTable {
    private static final String DEFAULT_NEXT_DATE = "XX.XX.XXXX";
    private static final String DASH = Labels.SPACE + Labels.DASH + Labels.SPACE;

    private final Map<String, String> labels;

    private final ChannelListService service;

    private final ButtonCell nextDate;
    private final ButtonCell path;
    private final ButtonCell sensor;

    public SwingChannelListInfoTable(ChannelListService service){
        super(new GridBagLayout());
        this.labels = Labels.getRootLabels();
        this.service = service;

        ButtonCell nextDateHeader = new ButtonCell(HEADER, labels.get(RootLabelName.NEXT_CHECK_DATE));
        ButtonCell pathHeader = new ButtonCell(HEADER, labels.get(RootLabelName.LOCATION));
        ButtonCell sensorHeader = new ButtonCell(HEADER, labels.get(RootLabelName.SENSOR_LONG));

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
                    labels.get(RootLabelName.EXTRAORDINARY) :
                    DateHelper.dateToString(nextDateCal);
            nextDate.setText(nextDateText);
            nextDate.setBackground(setBackgroundColorFromDate(nextDateCal));
            path.setText(service.getFullPath(channel));

            Sensor sensor = service.getSensor(channel);
            if (sensor != null) this.sensor.setText(sensor.getType());
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