package ui.mainScreen;

import application.Application;
import model.Channel;
import ui.model.DialogExit;
import ui.mainScreen.menu.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class MainScreen extends JFrame {

    private static final String windowHeader = "Вимірювальні канали";

    public MainTable mainTable;
    private MenuBar menuBar;
    private InfoTable infoTable;
    private ButtonsPanel buttonsPanel;
    public SearchPanel searchPanel;

    public ArrayList<Channel>channelsList;


    public MainScreen(){
        super(windowHeader);
    }

    public void init(ArrayList<Channel>channelsList){
        this.channelsList = channelsList;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.menuBar = new MenuBar(this);
        this.mainTable = new MainTable(this);
        this.infoTable = new InfoTable();
        this.buttonsPanel = new ButtonsPanel(this);
        this.searchPanel = new SearchPanel();
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
    }

    private void build() {
        this.setSize(Application.sizeOfScreen);
        this.setJMenuBar(this.menuBar);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(this.infoTable, new Cell(0, 0, 2, 0.05));
        mainPanel.add(this.searchPanel, new Cell(0,1,1,0.025));
        mainPanel.add(this.buttonsPanel, new Cell(1,1,1,0.025));
        mainPanel.add(new JScrollPane(this.mainTable), new Cell(0, 2, 2,0.9));

        this.setContentPane(mainPanel);
    }

    public void updateChannelInfo(Channel channel) {
        this.infoTable.updateInfo(channel);
    }

    public void setChannelsList(ArrayList<Channel>list){
        this.channelsList = list;
        this.mainTable.setList(channelsList);
        this.infoTable.updateInfo(null);
    }

    public void refreshMenu(){
        this.setJMenuBar(new MenuBar(this));
        this.refresh();
    }

    public void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private final WindowListener windowListener = new WindowListener() {
        @Override public void windowOpened(WindowEvent e) {}
        @Override public void windowClosed(WindowEvent e) {}
        @Override public void windowIconified(WindowEvent e) {}
        @Override public void windowDeiconified(WindowEvent e) {}
        @Override public void windowActivated(WindowEvent e) {}
        @Override public void windowDeactivated(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            if (!Application.isBusy(MainScreen.this)) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new DialogExit(MainScreen.this).setVisible(true);
                    }

                });
            }
        }
    };

    private static class Cell extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        protected Cell(int x, int y, int width, double height) {
            this.fill = BOTH;
            this.weightx = 1.0;

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
            this.weighty = height;
        }

    }
}