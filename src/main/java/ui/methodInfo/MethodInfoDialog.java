package ui.methodInfo;

import application.Application;
import constants.MeasurementConstants;
import converters.ConverterUI;
import support.Settings;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MethodInfoDialog extends JDialog {
    private static final String METHODS = "Методи розрахунку";
    private static final String OLD_NAME = "Стара назва : ";
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";

    private final MainScreen mainScreen;
    private final JDialog current;
    private final MeasurementConstants measurement;

    private JTextField userName;
    private JButton buttonCancel, buttonSave;

    public MethodInfoDialog(MainScreen mainScreen, MeasurementConstants measurement){
        super(mainScreen, METHODS, true);
        this.current = this;
        this.mainScreen = mainScreen;
        this.measurement = measurement;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.userName = new JTextField(10);
        this.userName.setHorizontalAlignment(SwingConstants.CENTER);
        String name = Settings.getSettingValue(this.measurement.getValue());
        this.userName.setToolTipText(OLD_NAME + name);
        this.userName.setText(name);

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);
    }

    private void build() {
        this.setSize(500,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(current)) return;
            Settings.setSettingValue(measurement.getValue(), userName.getText());
            dispose();
            mainScreen.refreshMenu();
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(userName, new Cell(0,0,2));
            this.add(buttonCancel, new Cell(0,1,1));
            this.add(buttonSave, new Cell(1,1,1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}