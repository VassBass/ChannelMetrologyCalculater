package ui.importData.compareChannels.complexElements;

import model.Channel;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class NewChannelsTable extends Table<Channel> {
    private static final String NAME = "Назва";

    public NewChannelsTable(final CompareChannelsDialog parent, final List<Channel> channels){
        super(tableModel(channels));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = e -> {
            if (NewChannelsTable.this.getSelectedRow() != -1) {
                parent.cancelSelection(parent.CHANGED_SENSORS_TABLE);
                parent.hideOldChannelInfo();
                parent.showNewChannelInfo(channels.get(NewChannelsTable.this.getSelectedRow()));
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