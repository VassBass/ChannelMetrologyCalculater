package ui.calibratorsList;

import application.Application;
import converters.ConverterUI;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalibratorRemoveDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String MESSAGE = "Ви впевнені що хочете відалити даний калібратор? ";
    public static final String CANCEL = "Відміна";

    private final CalibratorsListDialog parent;
    private final JDialog current;

    private JLabel message;
    private JButton buttonCancel, buttonRemove;

    public CalibratorRemoveDialog(CalibratorsListDialog parent){
        super(parent, title(parent.mainTable.getSelectedRow()), true);
        this.current = this;
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private static String title(int indexOfCalibrator){
        try {
            return REMOVE
                    + " \""
                    + Application.context.calibratorsController.get(indexOfCalibrator).getName()
                    + "\"?";
        }catch (Exception ex){
            return REMOVE;
        }
    }

    private void createElements() {
        this.message = new JLabel(MESSAGE);
        this.message.setHorizontalAlignment(SwingConstants.CENTER);

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonRemove = new DefaultButton(REMOVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    private void build() {
        this.setSize(800,100);
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
            if (Application.isBusy(current)) return;
            dispose();
            int index = parent.mainTable.getSelectedRow();
            Application.context.calibratorsController.remove(index);
            parent.mainTable.update();
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0,2, 5));
            this.add(buttonCancel, new Cell(0,1,1));
            this.add(buttonRemove, new Cell(1,1,1));
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