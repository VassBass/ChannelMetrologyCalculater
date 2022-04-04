package ui.calculate.verification;

import calculation.Calculation;
import constants.Key;
import converters.ConverterUI;
import model.Channel;
import model.Measurement;
import ui.calculate.measurement.CalculateMeasurementDialog;
import ui.calculate.performers.CalculatePerformersDialog;
import ui.calculate.reference.CalculateReferenceDialog;
import ui.calculate.verification.complexElements.ConsumptionPanel;
import ui.calculate.verification.complexElements.PressurePanel;
import ui.calculate.verification.complexElements.TemperaturePanel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class CalculateVerificationDialog extends JDialog {
    private static final String TITLE = "Результати розрахунку";
    private static final String BACK = "Назад";
    private static final String NEXT = "Далі";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private JPanel resultPanel;

    private JButton buttonBack, buttonNext;

    public CalculateVerificationDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super(mainScreen, TITLE, true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        String measurementName = this.channel.getMeasurement().getName();
        if (measurementName.equals(Measurement.TEMPERATURE)){
            this.resultPanel = new TemperaturePanel(this.channel, this.values, this.calculation);
        }else if (measurementName.equals(Measurement.PRESSURE)){
            this.resultPanel = new PressurePanel(this.channel, this.values, this.calculation);
        }else if (measurementName.equals(Measurement.CONSUMPTION)){
            this.resultPanel = new ConsumptionPanel(this.channel, this.values, this.calculation);
        }

        this.buttonBack = new DefaultButton(BACK);
        this.buttonNext = new DefaultButton(NEXT);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonBack.addActionListener(clickBack);
        this.buttonNext.addActionListener(clickNext);
    }

    private void build() {
        this.setSize(850,600);
        this.setResizable(false);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickBack = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable(){
                @Override
                public void run(){
                    dispose();
                    new CalculateMeasurementDialog(mainScreen, channel, values).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickNext = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dispose();
                    if (calculation.goodChannel()) {
                        values.remove(Key.CHANNEL_REFERENCE);
                        if (calculation.closeToFalse()){
                            values.put(Key.CALCULATION_CLOSE_TO_FALSE, resultPanel.getName());
                        }
                        new CalculatePerformersDialog(mainScreen, channel, values, calculation).setVisible(true);
                    }else{
                        try {
                            values.put(Key.CHANNEL_IS_GOOD, resultPanel.getName());
                        }catch (Exception ignored){}
                        new CalculateReferenceDialog(mainScreen, channel, values, calculation).setVisible(true);
                    }
                }
            });
        }
    };

    private class MainPanel extends JPanel {

        public MainPanel(){
            super(new GridBagLayout());

            JScrollPane scroll = new JScrollPane(resultPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setPreferredSize(new Dimension(800,500));
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            this.add(scroll, new Cell(0,0));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonBack);
            buttonsPanel.add(buttonNext);
            this.add(buttonsPanel, new Cell(0,1));
        }

        private class Cell extends GridBagConstraints {

            public Cell(int x, int y){
                super();

                this.fill = BOTH;

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}