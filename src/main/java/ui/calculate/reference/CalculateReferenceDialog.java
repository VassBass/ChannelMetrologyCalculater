package ui.calculate.reference;

import calculation.Calculation;
import constants.Key;
import converters.VariableConverter;
import model.Channel;
import ui.calculate.performers.CalculatePerformersDialog;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import static ui.UI_Constants.POINT_CENTER;

public class CalculateReferenceDialog extends JDialog {
    private static final String TITLE = "Канал непридатний";
    private static final String MESSAGE = "Згідно розрахунків канал визнано непридатним. Введіть номер довідки:";
    private static final String BACK = "Назад (Alt + Backspace)";
    private static final String NEXT = "Далі (Alt + Enter)";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private JLabel message;

    private JTextField number;

    private JButton buttonNext, buttonBack;

    public CalculateReferenceDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
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
        this.message = new JLabel(MESSAGE);

        this.number = new JTextField(5);
        this.number.setHorizontalAlignment(SwingConstants.CENTER);

        this.buttonBack = new DefaultButton(BACK);
        this.buttonNext = new DefaultButton(NEXT);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.number.addFocusListener(this.focusNumber);
        this.number.getDocument().addDocumentListener(changeNumber);

        this.buttonBack.addActionListener(this.clickBack);
        this.buttonNext.addActionListener(this.clickNext);

        this.number.addKeyListener(keyListener);
        this.buttonBack.addKeyListener(keyListener);
        this.buttonNext.addKeyListener(keyListener);
    }

    private void build() {
        this.setSize(600,200);
        this.setLocation(POINT_CENTER(this.mainScreen, this));

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
            if (number.getText().length() > 0) {
                values.put(Key.CHANNEL_REFERENCE, number.getText());
                dispose();
                new CalculatePerformersDialog(mainScreen, channel, values, calculation).setVisible(true);
            } else buttonNext.setEnabled(false);
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isAltDown()) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:
                        if (buttonNext.isEnabled()) buttonNext.doClick();
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        buttonBack.doClick();
                        break;
                }
            }
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