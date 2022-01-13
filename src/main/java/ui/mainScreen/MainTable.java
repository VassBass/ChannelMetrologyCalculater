package ui.mainScreen;

import model.Channel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

public class MainTable extends JTable {
    private static final String CODE = "Код";
    private static final String NAME = "Назва";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";

    private final JTable current;

    private ArrayList<Channel>channelsList;

    public MainTable(final MainScreen parent){
        super(tableModel(parent.channelsList));
        this.current = this;
        this.channelsList = parent.channelsList;

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (current.getSelectedRow() != -1) {
                    parent.updateChannelInfo(channelsList.get(current.getSelectedRow()));
                }
            }
        };
        this.getSelectionModel().addListSelectionListener(select);

        if (this.channelsList != null) {
            this.setRowsColor();
        }
    }

    public void setList(ArrayList<Channel>channelsList){
        this.channelsList = channelsList;
        this.setModel(tableModel(channelsList));
        this.setRowsColor();
    }

    private void setRowsColor(){
        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                try {
                    long nextDate = channelsList.get(row).getNextDate().getTimeInMillis();

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

    private static DefaultTableModel tableModel(ArrayList<Channel>channelList){
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