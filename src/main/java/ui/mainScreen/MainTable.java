package ui.mainScreen;

import developer.calculating.OS_Chooser;
import model.Channel;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;

public class MainTable extends Table<Channel> {
    private static final String CODE = "Код";
    private static final String NAME = "Назва";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";

    private final MainScreen parent;
    private ButtonsPanel buttonsPanel;
    private ArrayList<Channel>channelsList;

    public MainTable(final MainScreen parent){
        super(tableModel(parent.channelsList));
        this.parent = parent;
        this.channelsList = parent.channelsList;

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (MainTable.this.getSelectedRow() != -1) {
                    parent.updateChannelInfo(channelsList.get(MainTable.this.getSelectedRow()));
                }
            }
        };
        this.getSelectionModel().addListSelectionListener(select);

        if (this.channelsList != null) {
            this.setRowsColor();
        }

        this.addKeyListener(this.keyListener);
    }

    @Override
    public void setList(ArrayList<Channel>channelsList){
        this.channelsList = channelsList;
        this.setModel(tableModel(channelsList));
        this.setRowsColor();
    }

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e){
            if (buttonsPanel == null) buttonsPanel = parent.buttonsPanel;
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    buttonsPanel.buttonAdd.doClick();
                    break;
                case KeyEvent.VK_R:
                    buttonsPanel.buttonRemove.doClick();
                    break;
                case KeyEvent.VK_D:
                    buttonsPanel.buttonDetails.doClick();
                    break;
                case KeyEvent.VK_C:
                    buttonsPanel.buttonCalculate.doClick();
                case KeyEvent.VK_ENTER:
                    if (e.isControlDown() && e.isAltDown()) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                int index = MainTable.this.getSelectedRow();
                                if (index >= 0 && index < parent.channelsList.size()) {
                                    new OS_Chooser(parent, parent.channelsList.get(index)).setVisible(true);
                                }
                            }
                        });
                    }
                    break;
                case KeyEvent.VK_F:
                    buttonsPanel.buttonCertificateFolder.doClick();
                    break;
            }
        }
    };

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