package ui.mainScreen.channelTable;

import model.Channel;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ChannelTableVT extends ChannelTable {
    private final ChannelTable connect;

    protected ChannelTableVT(ChannelTable connect) {
        super(null, new ArrayList<>());
        this.connect = connect;
    }

    protected void updateChannelsList(List<Channel> channelsList){
        connect.channelsList = channelsList;
        connect.setModel(tableModel(channelsList));
        this.setRowsColor();
    }

    private DefaultTableModel tableModel(List<Channel>channelList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {TEXT_CODE, TEXT_NAME, TEXT_TYPE_OF_MEASUREMENT, TEXT_TECHNOLOGY_NUMBER};
        model.setColumnIdentifiers(columnsHeader);

        for (Channel channel : channelList) {
            String[] data = new String[4];
            data[0] = channel.getCode();
            data[1] = channel.getName();
            data[2] = channel.getMeasurement().getName();
            data[3] = channel.getTechnologyNumber();

            model.addRow(data);
        }

        return model;
    }
}
