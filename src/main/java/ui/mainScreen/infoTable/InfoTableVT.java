package ui.mainScreen.infoTable;

import converters.VariableConverter;
import model.Channel;

import java.awt.*;
import java.util.Calendar;

public class InfoTableVT extends InfoTable {
    private final InfoTable connect;

    protected InfoTableVT(InfoTable connect) {
        super();
        this.connect = connect;
    }

    protected void updateCells(Channel channel) {
        if (channel == null){
            connect.nextDate.setText(TEXT_DEFAULT_NEXT_DATE);
            connect.nextDate.setBackground(Color.WHITE);
            connect.path.setText(TEXT_DASH);
            connect.sensor.setText(TEXT_DASH);
        }else {
            connect.nextDate.setText(VariableConverter.dateToString(channel._getNextDate()));
            connect.nextDate.setBackground(setBackgroundColorFromDate(channel._getNextDate()));
            connect.path.setText(channel._getFullPath());
            connect.sensor.setText(channel.getSensor().getType());
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
