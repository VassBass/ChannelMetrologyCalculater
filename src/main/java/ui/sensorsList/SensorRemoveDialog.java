package ui.sensorsList;

import backgroundTasks.RemoveSensor;
import converters.ConverterUI;
import model.Sensor;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SensorRemoveDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String MESSAGE1 = "Ви впевнені що хочете відалити даний ПВП? ";
    private static final String MESSAGE2 = "Після його видалення, разом з ним будуть видалені всі канали, які використовують данний ПВП.";
    private static final String ADVISE = "ПОРАДА: Перед виконанням данної дії виконайте екпорт каналів, для збереження резервної копії каналів.";
    private static final String CANCEL = "Відміна";

    private final SensorsListDialog parent;

    private JLabel message1, message2, message3;
    private JButton buttonCancel, buttonRemove;

    public SensorRemoveDialog(SensorsListDialog parent){
        super(parent, title(parent.getSensor()), true);
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private static String title(Sensor sensor){
        if (sensor == null) {
            return REMOVE;
        }else {
            return REMOVE + " \"" + sensor.getName() + "\"?";
        }
    }

    private void createElements() {
        this.message1 = new JLabel(MESSAGE1);
        this.message1.setHorizontalAlignment(SwingConstants.CENTER);

        this.message2 = new JLabel(MESSAGE2);
        this.message2.setHorizontalAlignment(SwingConstants.CENTER);

        this.message3 = new JLabel(ADVISE);
        this.message3.setHorizontalAlignment(SwingConstants.CENTER);

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonRemove = new DefaultButton(REMOVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    private void build() {
        this.setSize(800,150);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

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
            new RemoveSensor(parent).start();
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