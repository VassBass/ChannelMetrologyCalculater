package ui.mainScreen;

import application.Application;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import ui.mainScreen.menu.MenuBar;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainScreen extends JFrame {
    private static MainScreen mainScreen;

    private static final Logger LOGGER = Logger.getLogger(MainScreen.class.getName());

    private static String windowHeader(int listSize){
        return "Вимірювальні канали [кількість : "+ listSize + "]";
    }

    public MainTable mainTable;
    private MenuBar menuBar;
    private InfoTable infoTable;
    public ButtonsPanel buttonsPanel;
    public SearchPanel searchPanel;

    private List<Channel> channelsList;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private MainScreen(){
        super();

        channelsList = new ArrayList<>(channelRepository.getAll());
        this.setTitle(windowHeader(channelsList.size()));

        this.createElements(channelsList);
        this.setReactions();
        this.build();
    }

    public static MainScreen getInstance() {
        if (mainScreen == null) mainScreen = new MainScreen();

        return mainScreen;
    }

    private void createElements(@Nonnull List<Channel>channelList) {
        this.menuBar = new MenuBar();
        this.mainTable = new MainTable(channelList);
        this.infoTable = new InfoTable();
        this.buttonsPanel = new ButtonsPanel();
        this.searchPanel = new SearchPanel();
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);
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

    public List<Channel>getChannelsList(){
        return channelsList;
    }

    public void setChannelsList(List<Channel>list){
        this.channelsList = list;
        this.mainTable.setList(list);
        this.setTitle(windowHeader(list.size()));
        this.infoTable.updateInfo(null);
    }

    public void refreshMenu(){
        this.setJMenuBar(new MenuBar());
        this.refresh();
    }

    public void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            EventQueue.invokeLater(() -> {
                int result = JOptionPane.showConfirmDialog(MainScreen.this,
                        "Закрити програму?", "Вихід", JOptionPane.OK_CANCEL_OPTION);
                if (result == 0){
                    System.exit(0);
                }
            });
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