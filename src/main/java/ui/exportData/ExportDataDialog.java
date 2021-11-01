package ui.exportData;

import backgroundTasks.ExportData;
import constants.Strings;
import converters.ConverterUI;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportDataDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;

    private JButton buttonExportChannels;
    private JButton buttonExportSensors;
    private JButton buttonExportCalibrators;
    private JButton buttonExportAllData;
    private JButton buttonCancel;

    private final Color buttonExportAllColor = new Color(51,51,51);

    public ExportDataDialog(MainScreen mainScreen){
        super(mainScreen, Strings.EXPORT, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonExportChannels = new JButton(Strings.EXPORT_CHANNELS);
        this.buttonExportChannels.setBackground(Color.WHITE);
        this.buttonExportChannels.setFocusPainted(false);
        this.buttonExportChannels.setContentAreaFilled(false);
        this.buttonExportChannels.setOpaque(true);

        this.buttonExportSensors = new JButton(Strings.EXPORT_SENSORS);
        this.buttonExportSensors.setBackground(Color.WHITE);
        this.buttonExportSensors.setFocusPainted(false);
        this.buttonExportSensors.setContentAreaFilled(false);
        this.buttonExportSensors.setOpaque(true);

        this.buttonExportCalibrators = new JButton(Strings.EXPORT_CALIBRATORS);
        this.buttonExportCalibrators.setBackground(Color.WHITE);
        this.buttonExportCalibrators.setFocusPainted(false);
        this.buttonExportCalibrators.setContentAreaFilled(false);
        this.buttonExportCalibrators.setOpaque(true);

        this.buttonExportAllData = new JButton(Strings.EXPORT);
        this.buttonExportAllData.setBackground(buttonExportAllColor);
        this.buttonExportAllData.setForeground(Color.WHITE);
        this.buttonExportAllData.setFocusPainted(false);
        this.buttonExportAllData.setContentAreaFilled(false);
        this.buttonExportAllData.setOpaque(true);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(Color.BLACK);
        this.buttonCancel.setForeground(Color.WHITE);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonExportChannels.addChangeListener(this.pushButton);
        this.buttonExportSensors.addChangeListener(this.pushButton);
        this.buttonExportCalibrators.addChangeListener(this.pushButton);
        this.buttonExportAllData.addChangeListener(this.pushAll);
        this.buttonCancel.addChangeListener(this.pushCancel);

        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonExportAllData.addActionListener(this.clickExportAll);
    }

    @Override
    public void build() {
        this.setSize(240,160);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

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

    private final ChangeListener pushAll = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonExportAllColor.darker());
            }else {
                button.setBackground(buttonExportAllColor);
            }
        }
    };

    private final ChangeListener pushCancel = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(Color.BLACK.brighter());
            }else {
                button.setBackground(Color.BLACK);
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickExportAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new ExportData(mainScreen).execute();
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(buttonExportChannels, new Cell(0,0));
            this.add(buttonExportSensors, new Cell(0,1));
            this.add(buttonExportCalibrators, new Cell(0,2));
            this.add(buttonExportAllData, new Cell(0,3));
            this.add(buttonCancel, new Cell(0,4));
        }

        private class Cell extends GridBagConstraints{

            protected Cell(int x, int y){
                super();

                this.weightx = 1.0;
                this.fill = BOTH;

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
