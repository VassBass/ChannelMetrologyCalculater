package ui.mainScreen;

import service.FileBrowser;
import ui.calculate.start.CalculateStartDialog;
import ui.channelInfo.DialogChannel;
import ui.model.DefaultButton;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel {
    private static final String DETAILS = "Детальніше (D)";
    private static final String REMOVE = "Видалити (R)";
    private static final String ADD = "Додати (A)";
    private static final String CALCULATE = "Розрахувати (C)";
    private static final String CERTIFICATES_PROTOCOLS = "Сертифікати/Протоколи (F)";

    public JButton buttonDetails, buttonRemove, buttonAdd, buttonCalculate, buttonCertificateFolder;

    public ButtonsPanel(){
        super(new GridBagLayout());

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

    private final ActionListener clickDetails = e -> {
        final int channelIndex = MainScreen.getInstance().mainTable.getSelectedRow();
        if (channelIndex!=-1) {
            EventQueue.invokeLater(() -> new DialogChannel(MainScreen.getInstance(), MainScreen.getInstance().getChannelsList().get(channelIndex)).setVisible(true));
        }
    };

    private final ActionListener clickRemove = e -> {
        if (MainScreen.getInstance().getChannelsList().size() > 0) {
            EventQueue.invokeLater(() -> new DialogRemoveChannels(MainScreen.getInstance()).setVisible(true));
        }
    };

    private final ActionListener clickAdd = e -> EventQueue.invokeLater(() -> new DialogChannel(MainScreen.getInstance(), null).setVisible(true));

    private final ActionListener clickCalculate = e -> EventQueue.invokeLater(() -> {
        int index = MainScreen.getInstance().mainTable.getSelectedRow();
        if (index >= 0 && index < MainScreen.getInstance().getChannelsList().size() ){
            new CalculateStartDialog(MainScreen.getInstance(), MainScreen.getInstance().getChannelsList().get(index), null).setVisible(true);
        }
    });

    private final ActionListener clickCertificateFolder = e -> {
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.open(FileBrowser.DIR_CERTIFICATES);
            }catch (Exception ex){
                ex.printStackTrace();
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