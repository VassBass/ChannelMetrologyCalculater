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

public class ConfirmExportDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog parent;
    private final int exportData;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    private final Color buttonColor = new Color(51,51,51);

    public ConfirmExportDialog(MainScreen mainScreen, JDialog parent, int exportData){
        super(parent, Strings.EXPORT, true);
        this.mainScreen = mainScreen;
        this.parent = parent;
        this.exportData = exportData;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        switch (this.exportData){
            case 0:
                this.message = new JLabel("Експортувати всі данні?");
                break;
            case 1:
                this.message = new JLabel("Експортувати всі вимірювальні канали?");
                break;
            case 2:
                this.message = new JLabel("Експортувати всі ПВП?");
                break;
            case 3:
                this.message = new JLabel("Експортувати всі калібратори?");
                break;
        }
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

    }

    @Override
    public void build() {
        this.setSize(200,200);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
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
            }
        }
    }
}
