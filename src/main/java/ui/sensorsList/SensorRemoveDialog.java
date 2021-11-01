package ui.sensorsList;

import backgroundTasks.RemoveSensor;
import constants.Strings;
import converters.ConverterUI;
import support.Lists;
import ui.UI_Container;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SensorRemoveDialog extends JDialog implements UI_Container {
    private final SensorsListDialog parent;

    private JLabel message1, message2, message3;
    private JButton buttonCancel, buttonRemove;

    public SensorRemoveDialog(SensorsListDialog parent){
        super(parent, title(parent.mainTable.getSelectedRow()), true);
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private static String title(int indexOfSensor){
        try {
            return Strings.REMOVE
                    + " \""
                    + Objects.requireNonNull(Lists.sensors()).get(indexOfSensor).getName()
                    + "\"?";
        }catch (Exception ex){
            return Strings.REMOVE;
        }
    }

    @Override
    public void createElements() {
        String m1 = "Ви впевнені що хочете відалити даний ПВП? ";
        this.message1 = new JLabel(m1);
        this.message1.setHorizontalAlignment(SwingConstants.CENTER);

        String m2 = "Після його видалення, разом з ним будуть видалені всі канали, які використовують данний ПВП.";
        this.message2 = new JLabel(m2);
        this.message2.setHorizontalAlignment(SwingConstants.CENTER);

        String m3 = "ПОРАДА: Перед виконанням данної дії виконайте екпорт каналів, для збереження резервної копії каналів.";
        this.message3 = new JLabel(m3);
        this.message3.setHorizontalAlignment(SwingConstants.CENTER);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(new Color(51,51,51));
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(Color.RED.darker());
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushCancel);
        this.buttonRemove.addChangeListener(pushRemove);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonRemove.addActionListener(clickRemove);
    }

    @Override
    public void build() {
        this.setSize(800,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushCancel = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (buttonCancel.getModel().isPressed()){
                buttonCancel.setBackground(new Color(51,51,51).darker());
            }else {
                buttonCancel.setBackground(new Color(51,51,51));
            }
        }
    };

    private final ChangeListener pushRemove = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (buttonRemove.getModel().isPressed()){
                buttonRemove.setBackground(Color.red.darker().darker());
            }else {
                buttonRemove.setBackground(Color.red.darker());
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
            dispose();
            new RemoveSensor(parent).execute();
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message1, new Cell(0,0,2));
            this.add(message2, new Cell(0,1,2));
            this.add(message3, new Cell(0,2,2, 5));
            this.add(buttonCancel, new Cell(0,3,1));
            this.add(buttonRemove, new Cell(1,3,1));
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

            protected Cell(int x, int y, int width, int marginBottom){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                this.insets = new Insets(0,0, marginBottom, 0);
            }
        }
    }
}