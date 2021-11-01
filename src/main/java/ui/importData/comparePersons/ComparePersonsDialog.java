package ui.importData.comparePersons;

import constants.Strings;
import converters.ConverterUI;
import support.*;
import ui.UI_Container;
import ui.importData.BreakImportDialog;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.importData.comparePersons.complexElements.ComparePersonsInfoPanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ComparePersonsDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog current;

    private final ArrayList<Sensor>sensors;
    private final ArrayList<Channel>channels;
    private final ArrayList<Worker>newPersonsList, oldPersonsList, importedPersonsList;
    private final ArrayList<Calibrator>newCalibratorsList, importedCalibratorsList;
    private final ArrayList<String>departments, areas, processes, installations;
    private final ArrayList<Integer[]>personsIndexes, calibratorsIndexes;

    private int marker = 0;

    private ComparePersonsInfoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public ComparePersonsDialog(final MainScreen mainScreen, final ArrayList<Sensor>sensors, final ArrayList<Channel>channels,
                                final ArrayList<Worker>newPersonsList, final ArrayList<Worker>importedPersonsList, final ArrayList<Integer[]>personsIndexes,
                                final ArrayList<Calibrator>newCalibratorsList, final ArrayList<Calibrator>importedCalibratorsList, final ArrayList<Integer[]>calibratorsIndexes,
                                final ArrayList<String>departments, final ArrayList<String>areas, final ArrayList<String>processes, final ArrayList<String>installations){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.current = this;
        this.sensors = sensors;
        this.channels = channels;

        this.newPersonsList = newPersonsList;
        this.oldPersonsList = Lists.persons();
        this.importedPersonsList = importedPersonsList;
        this.personsIndexes = personsIndexes;

        this.newCalibratorsList = newCalibratorsList;
        this.importedCalibratorsList = importedCalibratorsList;
        this.calibratorsIndexes = calibratorsIndexes;

        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        if (personsIndexes.size() == 0){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CompareCalibratorsDialog(mainScreen, sensors, channels, newPersonsList,
                            newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                            departments, areas, processes, installations);
                }
            });
        }else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    private void next(){
        marker++;
        if (marker >= personsIndexes.size()) {
            this.dispose();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CompareCalibratorsDialog(mainScreen, sensors, channels, newPersonsList,
                            newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                            departments, areas, processes, installations);
                }
            });
        }else {
            Integer[] index = personsIndexes.get(marker);
            int indexOld = index[0];
            int indexImport = index[1];
            this.infoPanel = new ComparePersonsInfoPanel(this.oldPersonsList.get(indexOld), this.importedPersonsList.get(indexImport));
            this.setContentPane(new MainPanel());
            this.setVisible(false);
            this.setVisible(true);
        }
    }

    @Override
    public void createElements() {
        Integer[] index = personsIndexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new ComparePersonsInfoPanel(this.oldPersonsList.get(indexOld), this.importedPersonsList.get(indexImport));

        this.buttonChange = new JButton(Strings.CHANGE);
        this.buttonChange.setBackground(Color.white);
        this.buttonChange.setFocusPainted(false);
        this.buttonChange.setContentAreaFilled(false);
        this.buttonChange.setOpaque(true);

        this.buttonSkip = new JButton(Strings.SKIP);
        this.buttonSkip.setBackground(Color.white);
        this.buttonSkip.setFocusPainted(false);
        this.buttonSkip.setContentAreaFilled(false);
        this.buttonSkip.setOpaque(true);

        this.buttonChangeAll = new JButton(Strings.CHANGE_ALL);
        this.buttonChangeAll.setBackground(Color.white);
        this.buttonChangeAll.setFocusPainted(false);
        this.buttonChangeAll.setContentAreaFilled(false);
        this.buttonChangeAll.setOpaque(true);

        this.buttonSkipAll = new JButton(Strings.SKIP_ALL);
        this.buttonSkipAll.setBackground(Color.white);
        this.buttonSkipAll.setFocusPainted(false);
        this.buttonSkipAll.setContentAreaFilled(false);
        this.buttonSkipAll.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);

        this.buttonSkipAll.addChangeListener(this.pushButton);
        this.buttonSkip.addChangeListener(this.pushButton);
        this.buttonChange.addChangeListener(this.pushButton);
        this.buttonChangeAll.addChangeListener(this.pushButton);

        this.buttonChangeAll.addActionListener(this.clickChangeAll);
        this.buttonSkip.addActionListener(this.clickSkip);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonSkipAll.addActionListener(this.clickSkipAll);
    }

    @Override
    public void build() {
        this.setSize(800,700);
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

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            new BreakImportDialog(mainScreen, current).setVisible(true);
        }
    };

    private final ActionListener clickSkip = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = personsIndexes.get(marker);
            newPersonsList.add(i[0], oldPersonsList.get(i[0]));
            next();
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = personsIndexes.get(marker);
            newPersonsList.add(i[0], importedPersonsList.get(i[1]));
            next();
        }
    };

    private final ActionListener clickSkipAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<personsIndexes.size();marker++){
                Integer[]i = personsIndexes.get(marker);
                newPersonsList.add(i[0], oldPersonsList.get(i[0]));
            }
            next();
        }
    };

    private final ActionListener clickChangeAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<personsIndexes.size();marker++){
                Integer[]i = personsIndexes.get(marker);
                newPersonsList.add(i[0], importedPersonsList.get(i[1]));
            }
            next();
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            JScrollPane scroll = new JScrollPane(infoPanel);
            scroll.setPreferredSize(new Dimension(800,650));
            this.add(scroll, new Cell(0, 0.95));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonSkip);
            buttonsPanel.add(buttonSkipAll);
            buttonsPanel.add(buttonChangeAll);
            buttonsPanel.add(buttonChange);
            this.add(buttonsPanel, new Cell(1, 0.05));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int y, double weighty){
                super();

                this.fill = BOTH;
                this.weightx = 1.0;
                this.weighty = weighty;

                this.gridx = 0;
                this.gridy = y;
            }
        }
    }
}
