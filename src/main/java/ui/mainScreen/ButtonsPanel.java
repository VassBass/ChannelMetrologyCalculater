package ui.mainScreen;

import service.FileBrowser;
import ui.calculate.start.CalculateStartDialog;
import ui.channelInfo.DialogChannel;
import ui.model.DefaultButton;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel {
    private static final String DETAILS = "Детальніше";
    private static final String REMOVE = "Видалити";
    private static final String ADD = "Додати";
    private static final String CALCULATE = "Розрахувати";
    private static final String CERTIFICATES_PROTOCOLS = "Сертифікати/Протоколи";

    private final MainScreen mainScreen;

    private JButton buttonDetails, buttonRemove, buttonAdd, buttonCalculate, buttonCertificateFolder;

    public ButtonsPanel(MainScreen mainScreen){
        super(new GridBagLayout());
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonDetails = new DefaultButton(DETAILS);
        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonAdd = new DefaultButton(ADD);
        this.buttonCalculate = new DefaultButton(CALCULATE);
        this.buttonCertificateFolder = new DefaultButton(CERTIFICATES_PROTOCOLS);
    }

    private void setReactions() {
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonCalculate.addActionListener(this.clickCalculate);
        this.buttonCertificateFolder.addActionListener(this.clickCertificateFolder);
    }

    private void build() {
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
                    if (index >= 0 && index < mainScreen.channelsList.size() ){
                        new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
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
                    desktop.open(FileBrowser.DIR_CERTIFICATES);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
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