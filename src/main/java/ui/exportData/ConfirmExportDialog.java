package ui.exportData;

import application.Application;
import backgroundTasks.data_export.*;
import converters.ConverterUI;
import model.Model;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmExportDialog extends JDialog {
    private static final String EXPORT = "Експорт";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;
    private final Model exportData;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    public ConfirmExportDialog(MainScreen mainScreen, Model exportData){
        super(mainScreen, EXPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        switch (this.exportData){
            case CHANNEL://Channels
                this.message = new JLabel("Експортувати всі вимірювальні канали?");
                break;
            case SENSOR://Sensors
                this.message = new JLabel("Експортувати всі ПВП?");
                break;
            case CALIBRATOR://Calibrators
                this.message = new JLabel("Експортувати всі калібратори?");
                break;
            case DEPARTMENT://Departments
                this.message = new JLabel("Експортувати всі цехи?");
                break;
            case AREA://Areas
                this.message = new JLabel("Експортувати всі ділянки?");
                break;
            case PROCESS://Processes
                this.message = new JLabel("Експортувати всі лінії, секції і т.п.?");
                break;
            case INSTALLATION://Installations
                this.message = new JLabel("Експортувати всі установки?");
                break;
            case PATH_ELEMENT://Path elements
                this.message = new JLabel("Експортувати всі елементи розташування каналів?");
                break;
            case PERSON://Persons
                this.message = new JLabel("Експортувати всх працівників?");
                break;
        }
        this.message.setHorizontalAlignment(SwingConstants.CENTER);

        this.positiveButton = new DefaultButton(EXPORT);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.negativeButton.addActionListener(this.clickCancel);
        this.positiveButton.addActionListener(this.clickExport);
    }

    private void build() {
        this.setSize(400,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));
        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickExport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(ConfirmExportDialog.this)) return;
            dispose();
            switch (exportData){
                case CHANNEL://Channels
                    new ExportChannels(mainScreen).execute();
                    break;
                case SENSOR://Sensors
                    new ExportSensors(mainScreen).execute();
                    break;
                case CALIBRATOR://Calibrators
                    new ExportCalibrators(mainScreen).execute();
                    break;
                case DEPARTMENT://Departments
                    new ExportDepartments(mainScreen).execute();
                    break;
                case AREA://Areas
                    new ExportAreas(mainScreen).execute();
                    break;
                case PROCESS://Processes
                    new ExportProcesses(mainScreen).execute();
                    break;
                case INSTALLATION://Installations
                    new ExportInstallations(mainScreen).execute();
                    break;
                case PATH_ELEMENT://Path elements
                    new ExportPathElements(mainScreen).execute();
                    break;
                case PERSON://Persons
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