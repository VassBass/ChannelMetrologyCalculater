package ui.importData.compareChannels;

import backgroundTasks.SaveImportedChannels;
import model.Channel;
import model.Sensor;
import ui.importData.compareChannels.complexElements.ChangedChannelsTable;
import ui.importData.compareChannels.complexElements.ChannelInfoWindow;
import ui.importData.compareChannels.complexElements.NewChannelsTable;
import ui.mainScreen.MainScreen;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.model.DialogLoading;
import ui.model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static ui.UI_Constants.*;

public class CompareChannelsDialog extends JDialog {
    private static final String IMPORT = "Імпорт каналів";
    private static final String IMPORTED_SENSOR = "Імпортуємий канал";
    private static final String OLD_SENSOR = "Канал з поточного списку";
    private static final String NEW_SENSORS = "Нові канали";
    private static final String SENSORS_FOR_CHANGE = "Канали на заміну";
    private static final String REMOVE = "Видалити";
    private static final String CONFIRM = "Підтвердити";

    public final boolean NEW_SENSORS_TABLE = true;
    public final boolean CHANGED_SENSORS_TABLE = false;

    private final List<Channel> newChannels, channelsForChange, changedChannels;
    private final List<Sensor>newSensors, sensorsToChange;

    private JWindow newChannelInfo, oldChannelInfo;

    private ButtonCell titleNewChannels, titleChannelsForChange;
    private Table<Channel> newChannelsTable, changedChannelsTable;
    private JButton removeFromNew, removeFromChanges, btnConfirmNew, btnConfirmChanges;

    public CompareChannelsDialog(List<Channel>newChannelsList,List<Channel>channelsForChange, List<Channel>changedChannelsList,
                                List<Sensor>newSensors, List<Sensor>sensorsToChange){
        super(MainScreen.getInstance(), IMPORT, true);
        this.newChannels = newChannelsList;
        this.channelsForChange = channelsForChange;
        this.changedChannels = changedChannelsList;
        this.newSensors = newSensors;
        this.sensorsToChange = sensorsToChange;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        this.titleNewChannels = new ButtonCell(true, NEW_SENSORS);
        this.titleChannelsForChange = new ButtonCell(true, SENSORS_FOR_CHANGE);
        this.newChannelsTable = new NewChannelsTable(CompareChannelsDialog.this, this.newChannels);
        this.changedChannelsTable = new ChangedChannelsTable(CompareChannelsDialog.this,this.channelsForChange, this.changedChannels);
        this.removeFromNew = new DefaultButton(REMOVE);
        this.removeFromNew.setEnabled(false);
        this.removeFromChanges = new DefaultButton(REMOVE);
        this.removeFromChanges.setEnabled(false);
        this.btnConfirmNew = new DefaultButton(CONFIRM);
        this.btnConfirmChanges = new DefaultButton(CONFIRM);

        if (this.newChannels.isEmpty()){
            this.newChannelsTable.setEnabled(false);
            this.removeFromNew.setEnabled(false);
            this.btnConfirmNew.setEnabled(false);
        }
        if (this.channelsForChange.isEmpty()){
            this.changedChannelsTable.setEnabled(false);
            this.removeFromChanges.setEnabled(false);
            this.btnConfirmChanges.setEnabled(false);
        }
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);
        this.addComponentListener(this.moveWindow);

        this.removeFromNew.addActionListener(this.clickRemoveFromNew);
        this.removeFromChanges.addActionListener(this.clickRemoveFromChanged);
        this.btnConfirmNew.addActionListener(this.clickConfirmNew);
        this.btnConfirmChanges.addActionListener(this.clickConfirmChanges);
    }

    private void build(){
        this.setSize(400,400);
        this.setResizable(false);
        this.setLocation(POINT_CENTER(MainScreen.getInstance(), this));
        this.setContentPane(new MainPanel());
    }

    public void showNewChannelInfo(Channel channel){
        if (this.newChannelInfo != null) this.newChannelInfo.dispose();

        this.newChannelInfo = new ChannelInfoWindow(IMPORTED_SENSOR,CompareChannelsDialog.this,channel);
        this.newChannelInfo.setLocation(LEFT_FROM_PARENT(CompareChannelsDialog.this, this.newChannelInfo));
        this.newChannelInfo.setVisible(true);
    }

    private void hideNewChannelInfo(){
        if (this.newChannelInfo != null) this.newChannelInfo.dispose();
    }

    public void showOldChannelInfo(Channel channel){
        if (this.oldChannelInfo != null) this.oldChannelInfo.dispose();

        this.oldChannelInfo = new ChannelInfoWindow(OLD_SENSOR,CompareChannelsDialog.this,channel);
        this.oldChannelInfo.setLocation(RIGHT_FROM_PARENT(CompareChannelsDialog.this));
        this.oldChannelInfo.setVisible(true);
    }

    public void hideOldChannelInfo(){
        if (this.oldChannelInfo != null) this.oldChannelInfo.dispose();
    }

    public void cancelSelection(boolean table){
        if (table == NEW_SENSORS_TABLE){
            this.newChannelsTable.clearSelection();
            this.removeFromNew.setEnabled(false);
            if (this.changedChannelsTable.getSelectedRow() >= 0) this.removeFromChanges.setEnabled(true);
        }else {
            this.changedChannelsTable.clearSelection();
            this.removeFromChanges.setEnabled(false);
            if (this.newChannelsTable.getSelectedRow() >= 0) this.removeFromNew.setEnabled(true);
        }
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            int result = JOptionPane.showConfirmDialog(CompareChannelsDialog.this,
                    "Припинити імпорт?", IMPORT, JOptionPane.OK_CANCEL_OPTION);
            if (result == 0){
                dispose();
            }else {
                setVisible(true);
            }
        }
    };

    private final ComponentListener moveWindow = new ComponentAdapter() {
        @Override
        public void componentMoved(ComponentEvent e) {
            if (newChannelInfo != null && newChannelInfo.isVisible()){
                newChannelInfo.setLocation(LEFT_FROM_PARENT(CompareChannelsDialog.this, newChannelInfo));
            }
            if (oldChannelInfo != null && oldChannelInfo.isVisible()){
                oldChannelInfo.setLocation(RIGHT_FROM_PARENT(CompareChannelsDialog.this));
            }
        }
    };

    private final ActionListener clickRemoveFromNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = newChannelsTable.getSelectedRow();
            Sensor sensor = newChannels.get(index).getSensor();
            newChannels.remove(index);
            hideNewChannelInfo();
            cancelSelection(NEW_SENSORS_TABLE);
            newChannelsTable.setList(newChannels);
            new RemoveSensor(sensor).execute();
        }
    };

    private final ActionListener clickRemoveFromChanged = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = changedChannelsTable.getSelectedRow();
            Sensor sensor = channelsForChange.get(index).getSensor();
            channelsForChange.remove(index);
            changedChannels.remove(index);
            hideNewChannelInfo();
            hideOldChannelInfo();
            cancelSelection(CHANGED_SENSORS_TABLE);
            changedChannelsTable.setList(channelsForChange);
            new RemoveSensor(sensor).execute();
        }
    };

    private final ActionListener clickConfirmNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmChanges.isEnabled()){
                if (newChannelsTable.getSelectedRow() >= 0) {
                    hideNewChannelInfo();
                    cancelSelection(NEW_SENSORS_TABLE);
                }
                removeFromNew.setEnabled(false);
                btnConfirmNew.setEnabled(false);
                newChannelsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedChannels(newChannels, channelsForChange, newSensors, sensorsToChange).execute();
            }
        }
    };

    private final ActionListener clickConfirmChanges = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmNew.isEnabled()){
                if (changedChannelsTable.getSelectedRow() >= 0) {
                    hideNewChannelInfo();
                    hideOldChannelInfo();
                    cancelSelection(CHANGED_SENSORS_TABLE);
                }
                removeFromChanges.setEnabled(false);
                btnConfirmChanges.setEnabled(false);
                changedChannelsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedChannels(newChannels, channelsForChange, newSensors,sensorsToChange).execute();
            }
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(titleNewChannels, new Cell(0,0,2,false));
            this.add(new JScrollPane(newChannelsTable), new Cell(0,1,2,true));
            this.add(removeFromNew, new Cell(0,5,1,false));
            this.add(btnConfirmNew, new Cell(1,5,1,false));
            this.add(titleChannelsForChange, new Cell(0,6,2,false));
            this.add(new JScrollPane(changedChannelsTable), new Cell(0,7,2,true));
            this.add(removeFromChanges, new Cell(0,11,1,false));
            this.add(btnConfirmChanges, new Cell(1,11,1,false));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int x, int y, int width, boolean table){
                super();
                this.fill = BOTH;
                this.weightx = 1.0;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                if (table){
                    this.weighty = 0.4;
                }else {
                    this.weighty = 0.05;
                }
            }
        }
    }

    private class RemoveSensor extends SwingWorker<Void, Void>{
        private final Sensor sensor;
        private final DialogLoading loadDialog;

        protected RemoveSensor(Sensor sensor){
            this.sensor = sensor;
            this.loadDialog = new DialogLoading(CompareChannelsDialog.this);
            EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        }

        @Override
        protected Void doInBackground() throws Exception {
            ArrayList<Channel>channels = new ArrayList<>(newChannels);
            channels.addAll(channelsForChange);
            for (Channel channel : channels){
                if (channel.getSensor().getName().equals(sensor.getName())){
                    return null;
                }
            }

            ArrayList<Sensor>sensors = new ArrayList<>(newSensors);
            sensors.addAll(sensorsToChange);
            for (int x=0;x<sensors.size();x++){
                if (sensors.get(x).getName().equals(this.sensor.getName())){
                    if (x >= newSensors.size()){
                        int s = sensors.size();
                        int n = newSensors.size();
                        int index = (s-n-1) - (s - x);
                        sensorsToChange.remove(index);
                    }else {
                        newSensors.remove(x);
                    }
                    break;
                }
            }
            return null;
        }

        @Override
        protected void done() {
            this.loadDialog.dispose();
        }
    }
}