package service.channel_list.ui.swing;

import model.dto.Channel;
import service.channel_list.ChannelListManager;
import service.channel_list.ChannelListService;
import service.channel_list.ui.ChannelListTable;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.List;

public class SwingChannelListTable extends JTable implements ChannelListTable {
    private static final String COLUMN_CODE = "Код";
    private static final String COLUMN_NAME = "Назва";
    private static final String COLUMN_TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String COLUMN_TECHNOLOGY_NUMBER = "Технологічний номер";

    private final ChannelListManager manager;
    private final ChannelListService service;

    public SwingChannelListTable(ChannelListManager manager, ChannelListService service){
        super();
        this.manager = manager;
        this.service = service;

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.getSelectionModel().addListSelectionListener(e ->
                manager.channelSelected(getSelectedChannelCode()));
        this.addKeyListener(this.keyListener);
    }

    @Override
    public void setChannelList(List<Channel>list){
        this.setModel(tableModel(list));
        this.setRowsColor();
    }

    @Override
    @Nullable
    public String getSelectedChannelCode() {
        int row = this.getSelectedRow();
        return row < 0 ?
                null :
                this.getModel().getValueAt(row, 0).toString();
    }

    private final KeyListener keyListener;
    {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        manager.addChannel();
                        break;
                    case KeyEvent.VK_R:
                        manager.removeChannel();
                        break;
                    case KeyEvent.VK_D:
                        manager.showChannelInfo();
                        break;
                    case KeyEvent.VK_C:
                        manager.calculateChannel();
                        break;
                    case KeyEvent.VK_ENTER:
                        manager.chooseOSBeforeChannelCalculate();
                        break;
                    case KeyEvent.VK_F:
                        manager.openChannelCertificateFolder();
                        break;
                }
            }
        };
    }

    private void setRowsColor(){
        Collection<String> codesOfExpiredChannels = service.getCodesOfExpiredChannels();
        Collection<String> codesOfChannelsCloseToExpired = service.getCodesOfChannelsCloseToExpired();

        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String code = SwingChannelListTable.this.getModel().getValueAt(row,0).toString();
                if (codesOfExpiredChannels.contains(code)) {
                    setBackground(Color.RED);
                    setForeground(Color.WHITE);
                } else if (codesOfChannelsCloseToExpired.contains(code)) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }

                if (isRowSelected(row)){
                    setBackground(Color.blue);
                    setForeground(Color.white);
                }
                return this;
            }
        });
    }

    private static DefaultTableModel tableModel(List<Channel>channelList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                COLUMN_CODE,
                COLUMN_TYPE_OF_MEASUREMENT,
                COLUMN_NAME,
                COLUMN_TECHNOLOGY_NUMBER
        };
        model.setColumnIdentifiers(columnsHeader);

        for (Channel channel : channelList) {
            String[] data = new String[4];
            data[0] = channel.getCode();
            data[1] = channel.getMeasurementName();
            data[2] = channel.getName();
            data[3] = channel.getTechnologyNumber();
            model.addRow(data);
        }

        return model;
    }
}
