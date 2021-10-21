package ui.exportChannels;

import backgroundTasks.ExportChannels;
import constants.Files;
import converters.ConverterUI;
import support.Lists;
import constants.Strings;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ExportChannelsDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;

    private JLabel label;
    private JButton buttonExport, buttonCancel, buttonFolder;

    public ExportChannelsDialog(MainScreen mainScreen){
        super(mainScreen, Strings.EXPORT, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        String message = "Експортувати канали? (кількість["
                + Objects.requireNonNull(Lists.channels()).size()
                + "])";
        this.label = new JLabel(message);

        this.buttonExport = new JButton(Strings.EXPORT);
        this.buttonExport.setBackground(Color.white);
        this.buttonExport.setFocusPainted(false);
        this.buttonExport.setContentAreaFilled(false);
        this.buttonExport.setOpaque(true);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonFolder = new JButton(Strings.EXPORTED_FILES);
        this.buttonFolder.setBackground(Color.white);
        this.buttonFolder.setFocusPainted(false);
        this.buttonFolder.setContentAreaFilled(false);
        this.buttonFolder.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonExport.addChangeListener(this.pushButton);
        this.buttonCancel.addChangeListener(this.pushButton);
        this.buttonFolder.addChangeListener(this.pushButton);

        this.buttonFolder.addActionListener(this.clickFolder);
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonExport.addActionListener(this.clickExport);
    }

    @Override
    public void build() {
        this.setSize(300,200);
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

    private final ActionListener clickFolder = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Desktop desktop;
            if (Desktop.isDesktopSupported()){
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(Files.EXPORT_DIR);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
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
            new ExportChannels(mainScreen).execute();
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(label, new Cell(0,0,2));
            this.add(buttonCancel, new Cell(0,1,1));
            this.add(buttonExport, new Cell(1,1,1));
            this.add(buttonFolder, new Cell(0,2,2));
        }

        private class Cell extends GridBagConstraints{

            protected Cell(int x, int y, int width){
                super();

                this.fill = BOTH;
                this.insets = new Insets(5,5,5,5);

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
