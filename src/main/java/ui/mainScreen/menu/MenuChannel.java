package ui.mainScreen.menu;

import service.FileBrowser;
import ui.calculate.start.CalculateStartDialog;
import ui.channelInfo.DialogChannel;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuChannel extends JMenu {
    private static final String CHANNEL = "Канал";
    private static final String ADD = "Додати";
    private static final String CALCULATE = "Розрахувати";
    private static final String DETAILS = "Детальніше";
    private static final String REMOVE = "Видалити";
    private static final String CERTIFICATES_PROTOCOLS = "Сертифікати/Протоколи";

    private final MainScreen mainScreen;

    private JMenuItem btn_calculate, btn_add, btn_details, btn_remove, btn_certificateProtocols = null;

    public MenuChannel(MainScreen mainScreen){
        super(CHANNEL);
        this.mainScreen = mainScreen;

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
                        new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
                    }
                }
            });
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
}