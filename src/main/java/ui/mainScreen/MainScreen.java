package ui.mainScreen;

import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import ui.event.EventSource;
import ui.event.EventSourceIdGenerator;
import ui.mainScreen.menu.MenuBar;
import ui.model.CellBuilder;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ui.UI_ConfigHolder.SCREEN_SIZE;

public class MainScreen extends JFrame implements EventSource {
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

    private MainScreen(){
        super();

        channelsList = new ArrayList<>(ChannelRepositorySQLite.getInstance().getAll());
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
        this.menuBar = new MenuBar(this);
        this.mainTable = new MainTable(this, channelList);
        this.infoTable = new InfoTable();
        this.buttonsPanel = new ButtonsPanel(this);
        this.searchPanel = new SearchPanel(this);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);
    }

    private void build() {
        this.setSize(SCREEN_SIZE);
        this.setJMenuBar(this.menuBar);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(this.infoTable, new CellBuilder()
                .coordinates(0,0)
                .width(2)
                .weightY(0.05)
                .create());

        mainPanel.add(this.searchPanel, new CellBuilder()
                .coordinates(0,1)
                .width(1)
                .weightY(0.025)
                .create());

        mainPanel.add(this.buttonsPanel, new CellBuilder()
                .coordinates(1,1)
                .width(1)
                .weightY(0.025)
                .create());

        mainPanel.add(new JScrollPane(this.mainTable), new CellBuilder()
                .coordinates(0, 2)
                .width(2)
                .weightY(0.9)
                .create());

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
        this.setJMenuBar(new MenuBar(this));
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

    @Override
    public String getId() {
        return EventSourceIdGenerator.generate(MainScreen.class);
    }
}