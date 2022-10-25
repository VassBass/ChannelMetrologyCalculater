package ui.mainScreen.menu;

import service.FileBrowser;
import ui.channelInfo.DialogChannel;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuChannel extends JMenu {
    private static final String CHANNEL = "Канал";
    private static final String ADD = "Додати";
    private static final String CALCULATE = "Розрахувати";
    private static final String DETAILS = "Детальніше";
    private static final String REMOVE = "Видалити";
    private static final String CERTIFICATES_PROTOCOLS = "Сертифікати/Протоколи";

    private JMenuItem btn_calculate, btn_add, btn_details, btn_remove, btn_certificateProtocols = null;

    public MenuChannel(){
        super(CHANNEL);

        createElements();
        setReactions();
        build();
    }

    private void createElements() {
        btn_add = new JMenuItem(ADD);
        btn_calculate = new JMenuItem(CALCULATE);
        btn_details = new JMenuItem(DETAILS);
        btn_remove = new JMenuItem(REMOVE);
        btn_certificateProtocols = new JMenuItem(CERTIFICATES_PROTOCOLS);
    }

    private void setReactions() {
        btn_details.addActionListener(clickDetails);
        btn_add.addActionListener(clickAdd);
        btn_remove.addActionListener(clickRemove);
        btn_calculate.addActionListener(clickCalculate);
        btn_certificateProtocols.addActionListener(clickCertificateFolder);
    }

    private void build() {
        this.add(btn_add);
        this.add(btn_remove);
        this.addSeparator();
        this.add(btn_details);
        this.add(btn_calculate);
        this.addSeparator();
        this.add(btn_certificateProtocols);
    }

    private final ActionListener clickDetails = e -> {
        final int channelIndex = MainScreen.getInstance().mainTable.getSelectedRow();
        if (channelIndex!=-1) {
            EventQueue.invokeLater(() -> {
                //new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
            });
        }
    };

    private final ActionListener clickAdd = e -> EventQueue.invokeLater(() ->
            new DialogChannel(null).setVisible(true));

    private final ActionListener clickCalculate = e -> EventQueue.invokeLater(() -> {
        int index = MainScreen.getInstance().mainTable.getSelectedRow();
        if (index != -1){
            //new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
        }
    });

    //}
    private final ActionListener clickRemove = e -> {
        if (MainScreen.getInstance().getChannelsList().size() > 0) {
            EventQueue.invokeLater(() -> new DialogRemoveChannels(MainScreen.getInstance()).setVisible(true));
        }
        };

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
}