package ui.calculate.measurement;

import backgroundTasks.CalculateChannel;
import constants.Key;
import converters.ConverterUI;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import ui.calculate.measurement.complexElements.*;
import ui.calculate.start.CalculateStartDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class CalculateMeasurementDialog extends JDialog {
    private static String title(Channel channel){
        return "Розрахунок каналу \"" + channel.getName() + "\"";
    }
    private String measurementString(){
        int mNum = this.measurementNumber + 1;
        return "Розрахунок №" + mNum;
    }
    private String removeMessage(int removedMeasurementNumber){
        return "Розрахунок №" + removedMeasurementNumber + " був видалений.";
    }
    private static final String BACK = "Назад (Alt + Left Arrow)";
    private static final String NEXT = "Далі (Alt + Right Arrow)";
    private static final String CLEAR = "Очистити (Alt + Backspace)";
    private static final String CALCULATE = "Розрахувати (Alt + Enter)";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;

    private int measurementNumber = 0;

    private JLabel labelMeasurement;

    private JButton buttonBack;
    private JButton buttonNext;
    private JButton buttonCalculate;
    private JButton buttonClear;

    private MeasurementPanel[] measurementsPanels;

    public CalculateMeasurementDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values){
        super(mainScreen, title(channel), true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;

        this.createElements();
        this.setValues();
        this.build();
        this.setReactions();
    }

    private void createElements() {
        this.labelMeasurement = new JLabel(this.measurementString());

        this.measurementsPanels = new MeasurementPanel[5];

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

        this.buttonBack.addKeyListener(this.keyListener);
        this.buttonNext.addKeyListener(this.keyListener);
        this.buttonClear.addKeyListener(this.keyListener);
        this.buttonCalculate.addKeyListener(this.keyListener);
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
        }else {
            this.values.put(Key.MEASUREMENT_2, this.measurementsPanels[0].getValues());
        }

        if (this.measurementsPanels[2] != null){
            this.values.put(Key.MEASUREMENT_3, this.measurementsPanels[2].getValues());
        }else {
            this.values.put(Key.MEASUREMENT_3, this.measurementsPanels[0].getValues());
        }

        if (this.measurementsPanels[3] != null){
            this.values.put(Key.MEASUREMENT_4, this.measurementsPanels[3].getValues());
        }else if (this.measurementsPanels[1] != null){
            this.values.put(Key.MEASUREMENT_4, this.measurementsPanels[1].getValues());
        }else {
            this.values.put(Key.MEASUREMENT_4, this.measurementsPanels[0].getValues());
        }

        if (this.measurementsPanels[4] != null){
            this.values.put(Key.MEASUREMENT_5, this.measurementsPanels[4].getValues());
        }else if (this.measurementsPanels[2] != null){
            this.values.put(Key.MEASUREMENT_5, this.measurementsPanels[2].getValues());
        }else {
            this.values.put(Key.MEASUREMENT_5, this.measurementsPanels[0].getValues());
        }

        this.values.put(Key.CONTROL_POINTS, this.measurementsPanels[0].getControlPointsValues());

        return this.values;
    }

    private void setValues(){
        for (int x=0;x<5;x++){
            double[]measurement = (double[]) this.values.get(x);
            if (x == 0 || measurement != null){
                this.createMeasurementPanel(x, measurement);
            }
        }
    }

    private void createMeasurementPanel(int index, double[] values){
        String measurementName = this.channel._getMeasurement().getName();
        if (measurementName.equals(Measurement.TEMPERATURE)){
            TemperaturePanel temperaturePanel = new TemperaturePanel(this.channel);
            this.measurementsPanels[index] = temperaturePanel;
        }else if (measurementName.equals(Measurement.PRESSURE)){
            PressurePanel pressurePanel = new PressurePanel(this.channel,(Calibrator) this.values.get(Key.CALIBRATOR));
            this.measurementsPanels[index] = pressurePanel;
        }else if (measurementName.equals(Measurement.CONSUMPTION)){
            Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);
            if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)){
                ConsumptionPanel_ROSEMOUNT consumptionPanel = new ConsumptionPanel_ROSEMOUNT(this.channel);
                this.measurementsPanels[index] = consumptionPanel;
            }else {
                ConsumptionPanel consumptionPanel = new ConsumptionPanel(this.channel);
                this.measurementsPanels[index] = consumptionPanel;
            }
        }
        if (values != null){
            this.measurementsPanels[index].setValues(values);
        }

        this.measurementsPanels[index].addKeyListener(keyListener);
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

    private final ActionListener clickBack = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable(){
                @Override
                public void run(){
                    buttonNext.setEnabled(true);
                    if (measurementNumber == 0){
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
                    if (measurementsPanels[measurementNumber] == null){
                        createMeasurementPanel(measurementNumber, null);
                    }
                    buttonNext.setEnabled(measurementNumber != 4);
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
                    String message = removeMessage(measurementNumber + 1);
                    values.put(measurementNumber, null);
                    if (measurementNumber == 0){
                        createMeasurementPanel(0, null);
                    }else {
                        measurementsPanels[measurementNumber] = null;
                        measurementNumber--;
                    }
                    JOptionPane.showMessageDialog(CalculateMeasurementDialog.this, message, CANCEL, JOptionPane.INFORMATION_MESSAGE);
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

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isAltDown()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        buttonClear.doClick();
                        break;
                    case KeyEvent.VK_ENTER:
                        buttonCalculate.doClick();
                        break;
                    case KeyEvent.VK_LEFT:
                        buttonBack.doClick();
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (buttonNext.isEnabled()) buttonNext.doClick();
                        break;
                }
            }
        }
    };

    private class MainPanel extends JPanel {

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(labelMeasurement, new Cell(0,0,new Insets(10,10,10,10)));
            this.add(measurementsPanels[measurementNumber], new Cell(0,1));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
            JPanel topPanel = new JPanel();
            JPanel bottomPanel = new JPanel();
            topPanel.add(buttonBack);
            topPanel.add(buttonNext);
            bottomPanel.add(buttonClear);
            bottomPanel.add(buttonCalculate);
            buttonsPanel.add(topPanel);
            buttonsPanel.add(bottomPanel);
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