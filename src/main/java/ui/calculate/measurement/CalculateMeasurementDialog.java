package ui.calculate.measurement;

import backgroundTasks.CalculateChannel;
import constants.Key;
import model.Calibrator;
import converters.ConverterUI;
import model.Channel;
import constants.Strings;
import ui.calculate.measurement.complexElements.*;
import ui.calculate.start.CalculateStartDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class CalculateMeasurementDialog extends JDialog {
    private static String title(Channel channel){
        return "Розрахунок каналу \"" + channel.getName() + "\"";
    }
    public static final String BACK = "Назад";
    public static final String NEXT = "Далі";
    public static final String CLEAR = "Очистити";
    public static final String CALCULATE = "Розрахувати";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final JDialog current;

    private int measurementNumber = 1;

    private JLabel labelMeasurement;

    private JButton buttonBack;
    private JButton buttonNext;
    private JButton buttonCalculate;
    private JButton buttonClear;

    private MeasurementPanel[] measurementsPanels;
    private JPanel[] duplicateOfMeasurementsPanels;

    public CalculateMeasurementDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values){
        super(mainScreen, title(channel), true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.current = this;

        this.createElements();
        this.setValues();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.labelMeasurement = new JLabel(this.measurementString());

        this.measurementsPanels = new MeasurementPanel[5];
        this.duplicateOfMeasurementsPanels = new JPanel[5];

        this.buttonBack = new DefaultButton(BACK);
        this.buttonNext = new DefaultButton(NEXT);
        this.buttonClear = new DefaultButton(CLEAR);
        this.buttonCalculate = new DefaultButton(CALCULATE);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonBack.addActionListener(this.clickBack);
        this.buttonNext.addActionListener(this.clickNext);
        this.buttonClear.addActionListener(this.clickClear);
        this.buttonCalculate.addActionListener(this.clickCalculate);
    }

    private void build() {
        this.setSize(670,400);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private HashMap<Integer, Object> getValues(){
        this.values.put(Key.MEASUREMENT_1, this.measurementsPanels[0].getValues());

        if (this.measurementsPanels[1] != null){
            this.values.put(Key.MEASUREMENT_2, this.measurementsPanels[1].getValues());
        }

        if (this.measurementsPanels[2] != null){
            this.values.put(Key.MEASUREMENT_3, this.measurementsPanels[2].getValues());
        }

        if (this.measurementsPanels[3] != null){
            this.values.put(Key.MEASUREMENT_4, this.measurementsPanels[3].getValues());
        }

        if (this.measurementsPanels[4] != null){
            this.values.put(Key.MEASUREMENT_5, this.measurementsPanels[4].getValues());
        }
        return this.values;
    }

    private void setValues(){
        for (int x=1;x<6;x++){
            String key = "measurement" + x;
            double[]measurement = (double[]) values.getValue(key);
            if (measurement != null || x == 1) {
                this.createMeasurementPanel(x - 1, measurement);
            }
        }
    }

    private void createMeasurementPanel(int index, double[] values){
        switch (this.channel.getMeasurement().getNameConstant()){
            case TEMPERATURE:
                TemperaturePanel temperaturePanel = new TemperaturePanel(this.channel);
                this.measurementsPanels[index] = temperaturePanel;
                this.duplicateOfMeasurementsPanels[index] = temperaturePanel;
                break;
            case PRESSURE:
                PressurePanel pressurePanel = new PressurePanel(this.channel,(Calibrator) this.values.getValue(Value.CALIBRATOR));
                this.measurementsPanels[index] = pressurePanel;
                this.duplicateOfMeasurementsPanels[index] = pressurePanel;
                break;
            case CONSUMPTION:
                Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);
                if (calibrator.getName().equals(Strings.CALIBRATOR_ROSEMOUNT_8714DQ4)){
                    ConsumptionPanel_ROSEMOUNT consumptionPanel = new ConsumptionPanel_ROSEMOUNT(this.channel);
                    this.measurementsPanels[index] = consumptionPanel;
                    this.duplicateOfMeasurementsPanels[index] = consumptionPanel;
                }else {
                    ConsumptionPanel consumptionPanel = new ConsumptionPanel(this.channel);
                    this.measurementsPanels[index] = consumptionPanel;
                    this.duplicateOfMeasurementsPanels[index] = consumptionPanel;
                }
        }
        if (values != null){
            this.measurementsPanels[index].setValues(values);
        }
    }

    private void update(){
        this.labelMeasurement.setText(this.measurementString());
        this.setContentPane(new MainPanel());
        this.refresh();
    }

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private String measurementString(){
        return "Измерение №" + this.measurementNumber;
    }

    private final ActionListener clickBack = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable(){
                @Override
                public void run(){
                    buttonNext.setEnabled(true);
                    if (measurementNumber == 1){
                        dispose();
                        new CalculateStartDialog(mainScreen, channel, getValues()).setVisible(true);
                    }else {
                        measurementNumber--;

                        update();
                    }
                }
            });
        }
    };

    private final ActionListener clickNext = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable(){
                @Override
                public void run(){
                    measurementNumber++;
                    int index = measurementNumber - 1;
                    if (measurementsPanels[index] == null){
                        createMeasurementPanel(index, null);
                    }
                    buttonNext.setEnabled(measurementNumber != 5);
                    update();
                }
            });
        }

    };

    private final ActionListener clickClear = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable(){
                @Override
                public void run(){
                    buttonNext.setEnabled(true);
                    int index = measurementNumber;

                    if (index == 1){
                        createMeasurementPanel(0, null);
                        values.putValue(Value.MEASUREMENT_1, null);
                    }else {
                        measurementNumber--;
                        measurementsPanels[measurementNumber] = null;
                        duplicateOfMeasurementsPanels[measurementNumber] = null;
                        String m = "measurement" + index;
                        values.putValue(m, null);
                    }

                    String mes = "Розрахунок №" + index + " був видалений.";
                    JOptionPane.showMessageDialog(current, mes, Strings.CANCEL, JOptionPane.INFORMATION_MESSAGE);
                    update();
                }
            });
        }
    };

    private final ActionListener clickCalculate = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            dispose();
            new CalculateChannel(mainScreen, channel, getValues()).execute();
        }
    };

    private class MainPanel extends JPanel {

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(labelMeasurement, new Cell(0,0,new Insets(10,10,10,10)));
            this.add(duplicateOfMeasurementsPanels[measurementNumber-1], new Cell(0,1));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonClear);
            buttonsPanel.add(buttonBack);
            buttonsPanel.add(buttonNext);
            buttonsPanel.add(buttonCalculate);
            this.add(buttonsPanel, new Cell(0,2, new Insets(20,0,0,0)));
        }

        private class Cell extends GridBagConstraints {

            protected Cell(int x, int y){
                super();

                this.gridx = x;
                this.gridy = y;
            }

            protected Cell(int x, int y, Insets insets){
                super();

                this.insets = insets;
                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}