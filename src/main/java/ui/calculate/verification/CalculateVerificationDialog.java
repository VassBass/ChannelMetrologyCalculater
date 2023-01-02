package ui.calculate.verification;

import calculation.Calculation;
import constants.Key;
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
import java.awt.event.*;
import java.util.HashMap;

import static ui.UI_Constants.POINT_CENTER;

public class CalculateVerificationDialog extends JDialog {
    private static final String TITLE = "Результати розрахунку";
    private static final String BACK = "Назад (Alt + Backspace)";
    private static final String NEXT = "Далі (Alt + Enter)";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private JPanel resultPanel;
    private MainPanel mainPanel;

    private JButton buttonBack, buttonNext;

    public CalculateVerificationDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super(mainScreen, TITLE, true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.build();
        this.setReactions();
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

        this.buttonBack.addKeyListener(keyListener);
        this.buttonNext.addKeyListener(keyListener);
        this.resultPanel.addKeyListener(keyListener);
    }

    private void build() {
        this.setSize(850,600);
        this.setResizable(false);
        this.setLocation(POINT_CENTER(this.mainScreen, this));

        this.mainPanel = new MainPanel();
        this.setContentPane(this.mainPanel);
    }

    private final ActionListener clickBack = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(() -> {
                dispose();
                new CalculateMeasurementDialog(mainScreen, channel, values).setVisible(true);
            });
        }
    };

    private final ActionListener clickNext = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> {
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
            });
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_DOWN:
                    if (e.isAltDown()){
                        mainPanel.scrollTo(MainPanel.DOWN_FULL);
                    }else {
                        mainPanel.scrollTo(MainPanel.DOWN);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (e.isAltDown()){
                        mainPanel.scrollTo(MainPanel.UP_FULL);
                    }else {
                        mainPanel.scrollTo(MainPanel.UP);
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (e.isAltDown()) buttonNext.doClick();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    if (e.isAltDown()) buttonBack.doClick();
                    break;
            }
        }
    };

    private class MainPanel extends JPanel {
        private final JScrollPane scroll;

        public static final int DOWN = 0;
        public static final int UP = 1;
        public static final int DOWN_FULL = 2;
        public static final int UP_FULL = 3;

        public MainPanel(){
            super(new GridBagLayout());

            scroll = new JScrollPane(resultPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setPreferredSize(new Dimension(800,500));
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            this.add(scroll, new Cell(0,0));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonBack);
            buttonsPanel.add(buttonNext);
            this.add(buttonsPanel, new Cell(0,1));
        }

        public void scrollTo(int to){
            Point point = scroll.getViewport().getViewPosition();
            int maxExtent = scroll.getViewport().getView().getHeight() - scroll.getViewport().getHeight();
            switch (to){
                case DOWN:
                    if (point.y < maxExtent) point.y += 16;
                    break;
                case UP:
                    if (point.y > 0) point.y -= 16;
                    break;
                case DOWN_FULL:
                    point.y = maxExtent;
                    break;
                case UP_FULL:
                    point.y = 0;
                    break;
            }
            scroll.getViewport().setViewPosition(point);
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