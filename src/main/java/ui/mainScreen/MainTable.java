package ui.mainScreen;

import model.Channel;
import ui.event.EventManager;
import ui.event.EventSource;
import ui.mainScreen.buttonsPanel.ButtonsPanel;
import ui.model.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.List;

import static ui.event.EventManager.*;
import static ui.event.eventManagers.mainScreen.MainScreenEventManager.*;

public class MainTable extends Table<Channel> {
    private static final String CODE = "Код";
    private static final String NAME = "Назва";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";

    private ButtonsPanel buttonsPanel;
    private List<Channel>channelsList;

    private final EventSource eventSource;
    private final EventManager eventManager = EventManager.getInstance();

    public MainTable(final EventSource eventSource, final List<Channel> channelList){
        super(tableModel(channelList));
        this.channelsList = channelList;
        this.eventSource = eventSource;

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (this.channelsList != null) this.setRowsColor();

        this.getSelectionModel().addListSelectionListener(e -> eventManager.runEvent(eventSource, SELECT_TABLE_ROW));
        this.addKeyListener(this.keyListener);
    }

    @Override
    public void setList(List<Channel>channelsList){
        this.channelsList = channelsList;
        this.setModel(tableModel(channelsList));
        this.setRowsColor();
    }

    private final KeyListener keyListener;
    {
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (buttonsPanel == null) buttonsPanel = MainScreen.getInstance().buttonsPanel;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        eventManager.runEvent(eventSource, CLICK_ADD_BUTTON);
                        break;
                    case KeyEvent.VK_R:
                        eventManager.runEvent(eventSource, CLICK_REMOVE_BUTTON);
                        break;
                    case KeyEvent.VK_D:
                        eventManager.runEvent(eventSource, CLICK_INFO_BUTTON);
                        break;
                    case KeyEvent.VK_C:
                        eventManager.runEvent(eventSource, CLICK_CALCULATE_BUTTON);
                        break;
                    case KeyEvent.VK_ENTER:
                        eventManager.runEvent(eventSource, CLICK_CHOOSE_OS);
                        break;
                    case KeyEvent.VK_F:
                        eventManager.runEvent(eventSource, CLICK_FOLDER_BUTTON);
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

        String[]columnsHeader = new String[] {CODE, NAME, TYPE_OF_MEASUREMENT, TECHNOLOGY_NUMBER};
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