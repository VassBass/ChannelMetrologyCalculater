package ui.measurementsList;

import model.Channel;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.util.ArrayList;

import static ui.UI_Constants.POINT_CENTER;

public class MeasurementsListDialog extends JDialog {

    private static final String MEASUREMENT_VALUES = "Вимірювальні величини";

    private final MainScreen mainScreen;

    private ListPanel listPanel;
    private ValuesPanel valuesPanel;

    public MeasurementsListDialog(MainScreen mainScreen){
        super(mainScreen, MEASUREMENT_VALUES, true);
        this.mainScreen = mainScreen;

        createElements();
        build();
        setReactions();
    }

    private void createElements(){
        this.listPanel = new ListPanel(this);
        this.valuesPanel = new ValuesPanel(this);
    }

    private void build(){
        this.setSize(800,550);
        this.setLocation(POINT_CENTER(this.mainScreen, this));

        JPanel mainPanel = new JPanel();
        mainPanel.add(this.listPanel);
        mainPanel.add(this.valuesPanel);

        this.setContentPane(mainPanel);
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String getSelectedMeasurementName(){
        return this.listPanel.getSelectedMeasurementName();
    }

    public String getSelectedMeasurementValue(){
        return this.listPanel.getSelectedMeasurementValue();
    }

    public void updateMeasurementsList(String measurementName){
        this.listPanel.updateMeasurementList(measurementName);
    }

    public void updateMain(ArrayList<Channel>channels){
        this.mainScreen.setChannelsList(channels);
    }

    public void setEnabledInListPanel_btnRemove(boolean enabled){
        this.listPanel.setEnabled_btnRemove(enabled);
    }

    public void updateValuesPanel(String measurementValue){
        this.valuesPanel.updateValues(measurementValue);
    }
}
