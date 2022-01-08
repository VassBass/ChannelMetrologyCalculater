package ui.mainScreen;

import constants.Files;
import constants.Strings;
import ui.channelInfo.DialogChannel;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel {
    private final MainScreen mainScreen;

    private final Color buttonsColor = new Color(51,51,51);

    private JButton buttonDetails, buttonRemove, buttonAdd, buttonCalculate, buttonCertificateFolder;

    public ButtonsPanel(MainScreen mainScreen){
        super(new GridBagLayout());
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public void createElements() {
        this.buttonDetails = new JButton(Strings.DETAILS);
        this.buttonDetails.setBackground(buttonsColor);
        this.buttonDetails.setForeground(Color.white);
        this.buttonDetails.setFocusPainted(false);
        this.buttonDetails.setContentAreaFilled(false);
        this.buttonDetails.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(buttonsColor);
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);

        this.buttonAdd = new JButton(Strings.ADD);
        this.buttonAdd.setBackground(buttonsColor);
        this.buttonAdd.setForeground(Color.white);
        this.buttonAdd.setFocusPainted(false);
        this.buttonAdd.setContentAreaFilled(false);
        this.buttonAdd.setOpaque(true);

        this.buttonCalculate = new JButton(Strings.CALCULATE);
        this.buttonCalculate.setBackground(buttonsColor);
        this.buttonCalculate.setForeground(Color.white);
        this.buttonCalculate.setFocusPainted(false);
        this.buttonCalculate.setContentAreaFilled(false);
        this.buttonCalculate.setOpaque(true);

        this.buttonCertificateFolder = new JButton(Strings.CERTIFICATES_PROTOCOLS);
        this.buttonCertificateFolder.setBackground(buttonsColor);
        this.buttonCertificateFolder.setForeground(Color.white);
        this.buttonCertificateFolder.setFocusPainted(false);
        this.buttonCertificateFolder.setContentAreaFilled(false);
        this.buttonCertificateFolder.setOpaque(true);
    }

    public void setReactions() {
        this.buttonDetails.addChangeListener(pushButton);
        this.buttonRemove.addChangeListener(pushButton);
        this.buttonAdd.addChangeListener(pushButton);
        this.buttonCalculate.addChangeListener(pushButton);
        this.buttonCertificateFolder.addChangeListener(pushButton);

        this.buttonDetails.addActionListener(clickDetails);
        this.buttonRemove.addActionListener(clickRemove);
        this.buttonAdd.addActionListener(clickAdd);
        this.buttonCalculate.addActionListener(clickCalculate);
        this.buttonCertificateFolder.addActionListener(clickCertificateFolder);
    }

    public void build() {
        this.add(this.buttonRemove, new Cell(0, 0, 1));
        this.add(this.buttonAdd, new Cell(1, 0, 1));
        this.add(this.buttonDetails, new Cell(0, 1, 2));
        this.add(this.buttonCertificateFolder, new Cell(0, 2, 1));
        this.add(this.buttonCalculate, new Cell(1, 2, 1));
    }

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final int channelIndex = mainScreen.mainTable.getSelectedRow();
            if (channelIndex!=-1) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainScreen.channelsList.size() > 0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new DialogRemoveChannels(mainScreen).setVisible(true);
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
                    new DialogChannel(mainScreen, null).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickCalculate = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int index = mainScreen.mainTable.getSelectedRow();
                    if (index != -1){
                        //new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
                    }
                }
            });
        }
    };

    private final ActionListener clickCertificateFolder = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Desktop desktop;
            if (Desktop.isDesktopSupported()){
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(Files.CERTIFICATES_DIR);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();

            if (button.getModel().isPressed()) {
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }

        }
    };

    private static class Cell extends GridBagConstraints{

        protected Cell(int x, int y, int width){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;
            this.insets = new Insets(2,2,2,2);

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
        }
    }
}
