package ui.calibratorsList;

import application.Application;
import constants.CalibratorType;
import converters.ConverterUI;
import model.Calibrator;
import ui.calibratorsList.calibratorInfo.CalibratorInfoDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalibratorsListDialog extends JDialog {
    public static final String CALIBRATORS_LIST = "Список калібраторів";
    public static final String ADD = "Додати";
    public static final String REMOVE = "Видалити";
    public static final String DETAILS = "Детальніше";
    public static final String CANCEL = "Відміна";
    public static final String NOT_REMOVED_CALIBRATOR_MESSAGE = "Даний калібратор не може бути видалений зі списку";
    public static final String ERROR = "Помилка";

    private final MainScreen mainScreen;
    private final CalibratorsListDialog current;

    public CalibratorsListTable mainTable;
    private JButton buttonAdd, buttonRemove, buttonDetails, buttonCancel;

    public CalibratorsListDialog(MainScreen mainScreen){
        super(mainScreen, CALIBRATORS_LIST, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.mainTable = new CalibratorsListTable();

        this.buttonAdd = new DefaultButton(ADD);
        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonDetails = new DefaultButton(DETAILS);
        this.buttonCancel = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonAdd.addActionListener(this.clickAdd);
    }

    private void build() {
        this.setSize(800,500);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

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
            if (mainTable.getSelectedRow() != -1) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int index = mainTable.getSelectedRow();
                        if (index != -1) {
                            Calibrator calibrator = Application.context.calibratorsController.get(index);
                            if (calibrator.getName().equals(CalibratorType.FLUKE718_30G)
                            || calibrator.getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)) {
                                JOptionPane.showMessageDialog(current, NOT_REMOVED_CALIBRATOR_MESSAGE, ERROR, JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            new CalibratorRemoveDialog(current).setVisible(true);
                        }
                    }
                });
            }
        }
    };

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final int index = mainTable.getSelectedRow();
            if (index != -1){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Calibrator calibrator = Application.context.calibratorsController.get(index);
                        new CalibratorInfoDialog(current, calibrator).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CalibratorInfoDialog(current, null).setVisible(true);
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