package ui.methodInfo;

import calculation.Method;
import constants.Strings;
import constants.Value;
import converters.ConverterUI;
import support.Settings;
import ui.UI_Container;
import ui.main.MainScreen;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MethodInfoDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final Method method;

    private JTextField userName;
    private JButton buttonCancel, buttonSave;

    private final Color buttonsColor = new Color(51,51,51);

    public MethodInfoDialog(MainScreen mainScreen, Method method){
        super(mainScreen, Strings.METHODS, true);
        this.mainScreen = mainScreen;
        this.method = method;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.userName = new JTextField(10);
        this.userName.setHorizontalAlignment(SwingConstants.CENTER);
        String name = "";
        switch (this.method){
            case MKMX_5300_01_18:
                name = Settings.getSettingValue(Value.NAME_MKMX_5300_01);
                break;
            case MKMX_5300_02_18:
                name = Settings.getSettingValue(Value.NAME_MKMX_5300_02);
                break;
        }
        this.userName.setToolTipText(Strings.OLD_NAME + " : " + name);
        this.userName.setText(name);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonSave = new JButton(Strings.SAVE);
        this.buttonSave.setBackground(buttonsColor);
        this.buttonSave.setForeground(Color.white);
        this.buttonSave.setFocusPainted(false);
        this.buttonSave.setContentAreaFilled(false);
        this.buttonSave.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonSave.addChangeListener(pushButton);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonSave.addActionListener(clickSave);
    }

    @Override
    public void build() {
        this.setSize(500,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (method){
                case MKMX_5300_01_18:
                    Settings.setSettingValue(Value.NAME_MKMX_5300_01, userName.getText());
                    break;
                case MKMX_5300_02_18:
                    Settings.setSettingValue(Value.NAME_MKMX_5300_02, userName.getText());
                    break;
            }
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
