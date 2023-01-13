package ui.mainScreen.channelTable;

import model.Channel;
import service.MainScreenEventListener;
import ui.event.EventDataSource;
import ui.event.SingleEventDataSource;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.List;

import static ui.mainScreen.MainScreen.KEY_CHANNEL;

public class ChannelTable extends JTable {
    private static final String TEXT_CODE = "Код";
    private static final String TEXT_NAME = "Назва";
    private static final String TEXT_TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TEXT_TECHNOLOGY_NUMBER = "Технологічний номер";

    private List<Channel>channelsList;

    private final MainScreenEventListener eventListener;

    public ChannelTable(MainScreenEventListener eventListener,
                        final List<Channel> channelList){
        super(tableModel(channelList));
        this.eventListener = eventListener;
        this.channelsList = channelList;

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (this.channelsList != null) this.setRowsColor();

        this.getSelectionModel().addListSelectionListener(
                eventListener.selectChannel(new SingleEventDataSource<>(KEY_CHANNEL, getSelectedChannel())));
        this.addKeyListener(this.keyListener);
    }

    public void setList(List<Channel>channelsList){
        this.channelsList = channelsList;
        this.setModel(tableModel(channelsList));
        this.setRowsColor();
    }

    @Nullable
    public Channel getSelectedChannel() {
        int selectedIndex = this.getSelectedRow();
        return selectedIndex < 0 || selectedIndex >= channelsList.size() ?
                null :
                channelsList.get(selectedIndex);
    }

    private final KeyListener keyListener;
    {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        eventListener.clickAddButton(EventDataSource.empty)
                                .actionPerformed(null);
                        break;
                    case KeyEvent.VK_R:
                        eventListener.clickRemoveButton(new SingleEventDataSource<>(KEY_CHANNEL, getSelectedChannel()))
                                .actionPerformed(null);
                        break;
                    case KeyEvent.VK_D:
                        eventListener.clickInfoButton(new SingleEventDataSource<>(KEY_CHANNEL, getSelectedChannel()))
                                        .actionPerformed(null);
                        break;
                    case KeyEvent.VK_C:
                        eventListener.clickCalculateButton(new SingleEventDataSource<>(KEY_CHANNEL, getSelectedChannel()))
                                        .actionPerformed(null);
                        break;
                    case KeyEvent.VK_ENTER:
                        eventListener.clickChooseOS(new SingleEventDataSource<>(KEY_CHANNEL, getSelectedChannel()))
                                        .actionPerformed(null);
                        break;
                    case KeyEvent.VK_F:
                        eventListener.clickOpenFolderButton(EventDataSource.empty)
                                        .actionPerformed(null);
                        break;
                }
            }
        };
    }

    private void setRowsColor(){
        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                try {
                    long nextDate = channelsList.get(row)._getNextDate().getTimeInMillis();

                    long toNextControl = nextDate - Calendar.getInstance().getTimeInMillis();
                    long days90 = 7776000000L;

                    if (toNextControl <= days90 && toNextControl >= 0L){
                        setBackground(Color.yellow);
                        setForeground(Color.black);
                    }else if (toNextControl < 0L){
                        setBackground(Color.red);
                        setForeground(Color.white);
                    }else {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }
                }catch (NullPointerException ex){
                    if (isRowSelected(row)){
                        setBackground(Color.blue);
                    }else {
                        setBackground(Color.red);
                    }
                    setForeground(Color.white);
                    return this;
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