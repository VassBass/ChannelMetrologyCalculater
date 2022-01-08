package ui.calculate.reference;

import measurements.calculation.Calculation;
import constants.Value;
import converters.ConverterUI;
import converters.VariableConverter;
import model.Channel;
import constants.Strings;
import support.Values;
import ui.calculate.performers.CalculatePerformersDialog;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CalculateReferenceDialog extends JDialog {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;
    private final Calculation calculation;

    private JLabel message;

    private JTextField number;

    private JButton buttonNext, buttonBack;

    public CalculateReferenceDialog(MainScreen mainScreen, Channel channel, Values values, Calculation calculation){
        super(mainScreen, "Канал непридатний", true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public void createElements() {
        String s = "Згідно розрахунків канал визнано непридатним. Введіть номер довідки:";
        this.message = new JLabel(s);

        this.number = new JTextField(5);
        this.number.addFocusListener(focusNumber);
        this.number.setHorizontalAlignment(SwingConstants.CENTER);
        this.number.getDocument().addDocumentListener(changeNumber);

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
        this.buttonNext.setEnabled(false);
    }

    public void setReactions() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.buttonBack.addChangeListener(this.pushButton);
        this.buttonNext.addChangeListener(this.pushButton);

        this.buttonBack.addActionListener(this.clickBack);
        this.buttonNext.addActionListener(this.clickNext);
    }

    public void build() {
        this.setSize(600,200);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final FocusListener focusNumber = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            number.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            String s = VariableConverter.intString(number.getText());
            number.setText(s);
        }
    };

    private final DocumentListener changeNumber = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            String s = VariableConverter.intString(number.getText());
            buttonNext.setEnabled(Integer.parseInt(s) != 0);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String s = VariableConverter.intString(number.getText());
            buttonNext.setEnabled(Integer.parseInt(s) != 0);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            String s = VariableConverter.intString(number.getText());
            buttonNext.setEnabled(Integer.parseInt(s) != 0);
        }
    };

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

    private final ActionListener clickBack = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new CalculateVerificationDialog(mainScreen, channel, values, calculation).setVisible(true);
        }
    };

    private final ActionListener clickNext = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            values.putValue(Value.CHANNEL_REFERENCE, number.getText());
            dispose();
            new CalculatePerformersDialog(mainScreen, channel, values, calculation).setVisible(true);
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0));
            this.add(number, new Cell(0,1));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonBack);
            buttonsPanel.add(buttonNext);
            this.add(buttonsPanel, new Cell(0,2));
        }

        private class Cell extends GridBagConstraints{

            protected Cell(int x, int y){
                super();

                this.insets = new Insets(10,0,10,0);

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
