package ui.exportData;

import backgroundTasks.Exporter;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static ui.UI_Constants.POINT_CENTER;

public class ConfirmExportDialog extends JDialog {
    private static final String EXPORT = "Експорт";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    public ConfirmExportDialog(MainScreen mainScreen){
        super(mainScreen, EXPORT, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.message = new JLabel("Експортувати дані?");
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
        this.setLocation(POINT_CENTER(this.mainScreen, this));
        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = e -> dispose();

    private final ActionListener clickExport = e -> {
        dispose();
        new Exporter().export();
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