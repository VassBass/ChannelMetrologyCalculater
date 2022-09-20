package ui.importData.compareChannels.complexElements;

import model.Channel;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ChangedChannelsTable extends Table<Channel> {
    private static final String NAME = "Назва";

    public ChangedChannelsTable(final CompareChannelsDialog parent,
                               final ArrayList<Channel> channelsForChange, final ArrayList<Channel>changedChannels){
        super(tableModel(channelsForChange));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = ChangedChannelsTable.this.getSelectedRow();
                if (index != -1) {
                    Channel newChannel = channelsForChange.get(index);
                    Channel oldChannel = changedChannels.get(index);
                    parent.cancelSelection(parent.NEW_SENSORS_TABLE);
                    parent.showNewChannelInfo(newChannel);
                    parent.showOldChannelInfo(oldChannel);
                }
            }
        };
        this.getSelectionModel().addListSelectionListener(select);
        this.setCenterAlignment();
    }

    private void setCenterAlignment(){
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int x=0;x<this.getColumnCount();x++){
            this.getColumnModel().getColumn(x).setCellRenderer(centerRender);
        }
    }

    @Override
    public void setList(List<Channel> channelList){
        this.setModel(tableModel(channelList));
        this.setCenterAlignment();
    }

    private static DefaultTableModel tableModel(List<Channel> channelList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {NAME};
        model.setColumnIdentifiers(columnsHeader);

        for (Channel channel : channelList) {
            String[] data = new String[1];
            data[0] = channel.getName();
            model.addRow(data);
        }

        return model;
    }
}