package ui.sensorsList;

import constants.Strings;
import converters.ConverterUI;
import support.Lists;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SensorsListDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final SensorsListDialog current;

    public SensorsListTable mainTable;
    private JButton buttonAdd, buttonRemove, buttonDetails, buttonCancel;

    private final Color buttonsColor = new Color(51,51,51);

    public SensorsListDialog(MainScreen mainScreen){
        super(mainScreen, Strings.SENSORS_LIST, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.mainTable = new SensorsListTable();

        this.buttonAdd = new JButton(Strings.ADD);
        this.buttonAdd.setBackground(buttonsColor);
        this.buttonAdd.setForeground(Color.white);
        this.buttonAdd.setFocusPainted(false);
        this.buttonAdd.setContentAreaFilled(false);
        this.buttonAdd.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(buttonsColor);
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);

        this.buttonDetails = new JButton(Strings.DETAILS);
        this.buttonDetails.setBackground(buttonsColor);
        this.buttonDetails.setForeground(Color.white);
        this.buttonDetails.setFocusPainted(false);
        this.buttonDetails.setContentAreaFilled(false);
        this.buttonDetails.setOpaque(true);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonRemove.addChangeListener(pushButton);
        this.buttonDetails.addChangeListener(pushButton);
        this.buttonAdd.addChangeListener(pushButton);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonRemove.addActionListener(clickRemove);
    }

    @Override
    public void build() {
        this.setSize(500,500);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    public void updateMain(){
        this.mainScreen.update(Lists.channels(), false, null, null);
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

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SensorRemoveDialog(current).setVisible(true);
                }
            });
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(new JScrollPane(mainTable), new Cell(0,1,4,0.9));
            this.add(buttonCancel, new Cell(0,2,1,0.1));
            this.add(buttonRemove, new Cell(1,2,1,0.1));
            this.add(buttonDetails, new Cell(2,2,1,0.1));
            this.add(buttonAdd, new Cell(3,2,1,0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, double weight){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                this.weighty = weight;
            }
        }
    }
}
