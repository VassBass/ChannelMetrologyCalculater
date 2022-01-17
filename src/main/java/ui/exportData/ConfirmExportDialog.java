package ui.exportData;

import backgroundTasks.data_export.*;
import constants.Strings;
import converters.ConverterUI;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmExportDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final int exportData;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    private final Color buttonColor = new Color(51,51,51);

    public ConfirmExportDialog(MainScreen mainScreen, int exportData){
        super(mainScreen, Strings.EXPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        switch (this.exportData){
            case 0://AllData
                this.message = new JLabel("Експортувати всі данні?");
                break;
            case 1://Channels
                this.message = new JLabel("Експортувати всі вимірювальні канали?");
                break;
            case 2://Sensors
                this.message = new JLabel("Експортувати всі ПВП?");
                break;
            case 3://Calibrators
                this.message = new JLabel("Експортувати всі калібратори?");
                break;
            case 4://Departments
                this.message = new JLabel("Експортувати всі цехи?");
                break;
            case 5://Areas
                this.message = new JLabel("Експортувати всі ділянки?");
                break;
            case 6://Processes
                this.message = new JLabel("Експортувати всі лінії, секції і т.п.?");
                break;
            case 7://Installations
                this.message = new JLabel("Експортувати всі установки?");
                break;
            case 8://Path elements
                this.message = new JLabel("Експортувати всі елементи розташування каналів?");
                break;
            case 9://Persons
                this.message = new JLabel("Експортувати всх працівників?");
                break;
        }
        this.message.setHorizontalAlignment(SwingConstants.CENTER);

        this.positiveButton = new JButton(Strings.EXPORT);
        this.positiveButton.setBackground(this.buttonColor);
        this.positiveButton.setForeground(Color.WHITE);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(this.buttonColor);
        this.negativeButton.setForeground(Color.WHITE);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.negativeButton.addChangeListener(pushButton);
        this.positiveButton.addChangeListener(pushButton);

        this.negativeButton.addActionListener(clickCancel);
        this.positiveButton.addActionListener(clickExport);
    }

    @Override
    public void build() {
        this.setSize(400,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));
        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(buttonColor.darker());
            }else {
                button.setBackground(buttonColor);
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickExport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            switch (exportData){
                case 0://All data
                    break;
                case 1://Channels
                    new ExportChannels(mainScreen).execute();
                    break;
                case 2://Sensors
                    new ExportSensors(mainScreen).execute();
                    break;
                case 3://Calibrators
                    new ExportCalibrators(mainScreen).execute();
                    break;
                case 4://Departments
                    new ExportDepartments(mainScreen).execute();
                    break;
                case 5://Areas
                    new ExportAreas(mainScreen).execute();
                    break;
                case 6://Processes
                    new ExportProcesses(mainScreen).execute();
                    break;
                case 7://Installations
                    new ExportInstallations(mainScreen).execute();
                    break;
                case 8://Path elements
                    new ExportPathElements(mainScreen).execute();
                    break;
                case 9://Persons
                    new ExportPersons(mainScreen).execute();
                    break;
            }
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0,2));
            this.add(negativeButton, new Cell(0,1,1));
            this.add(positiveButton, new Cell(1,1,1));
        }

        private class Cell extends GridBagConstraints{

            protected Cell(int x, int y, int width){
                super();

                this.weightx = 1.0;
                this.fill = BOTH;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                if (width == 2){
                    this.insets = new Insets(0,0,10,0);
                }
            }
        }
    }
}
