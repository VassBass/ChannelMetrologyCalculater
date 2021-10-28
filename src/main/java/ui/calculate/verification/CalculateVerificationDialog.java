package ui.calculate.verification;

import measurements.calculation.Calculation;
import constants.Value;
import converters.ConverterUI;
import support.Channel;
import constants.Strings;
import support.Values;
import ui.UI_Container;
import ui.calculate.measurement.CalculateMeasurementDialog;
import ui.calculate.performers.CalculatePerformersDialog;
import ui.calculate.reference.CalculateReferenceDialog;
import ui.calculate.verification.complexElements.PressurePanel;
import ui.calculate.verification.complexElements.TemperaturePanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateVerificationDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;
    private final Calculation calculation;

    private JPanel resultPanel;

    private JButton buttonBack, buttonNext;

    public CalculateVerificationDialog(MainScreen mainScreen, Channel channel, Values values, Calculation calculation){
        super(mainScreen, "Результати розрахунку", true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        switch (this.channel.getMeasurement().getNameConstant()){
            case TEMPERATURE:
                this.resultPanel = new TemperaturePanel(this.channel, this.values, this.calculation);
                break;
            case PRESSURE:
                this.resultPanel = new PressurePanel(this.channel, this.values, this.calculation);
                break;
        }

        this.buttonBack = new JButton(Strings.BACK);
        this.buttonBack.setBackground(Color.white);
        this.buttonBack.setFocusPainted(false);
        this.buttonBack.setContentAreaFilled(false);
        this.buttonBack.setOpaque(true);

        this.buttonNext = new JButton(Strings.NEXT);
        this.buttonNext.setBackground(Color.white);
        this.buttonNext.setFocusPainted(false);
        this.buttonNext.setContentAreaFilled(false);
        this.buttonNext.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonBack.addChangeListener(pushButton);
        this.buttonNext.addChangeListener(pushButton);

        this.buttonBack.addActionListener(clickBack);
        this.buttonNext.addActionListener(clickNext);
    }

    @Override
    public void build() {
        this.setSize(850,600);
        this.setResizable(false);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(Color.white.darker());
            }else {
                button.setBackground(Color.white);
            }
        }
    };

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
                        values.removeValue(Value.CHANNEL_REFERENCE);
                        if (calculation.closeToFalse()){
                            values.putValue(Value.CALCULATION_CLOSE_TO_FALSE, resultPanel.getName());
                        }
                        new CalculatePerformersDialog(mainScreen, channel, values, calculation).setVisible(true);
                    }else{
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
