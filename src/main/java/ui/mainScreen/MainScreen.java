package ui.mainScreen;

import model.Channel;
import repository.ChannelRepository;
import repository.SQLiteRepositoryFactory;
import ui.event.EventSource;
import ui.event.EventSourceIdGenerator;
import ui.mainScreen.buttonsPanel.ButtonsPanel;
import ui.mainScreen.channelTable.ChannelTable;
import ui.mainScreen.infoTable.InfoTable;
import ui.mainScreen.menu.MenuBar;
import ui.mainScreen.searchPanel.SearchPanel;
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
    public static final String KEY_SEARCH_FIELD_TEXT = "search_field_text";
    public static final String KEY_SEARCH_VALUE_TEXT = "search_value_text";
    public static final String KEY_SEARCH_VALUE_LIST_ITEM_TEXT = "search_value_list_item_text";
    public static final String KEY_SEARCH_VALUE_BOOLEAN = "search_value_boolean";

    private static final Logger LOGGER = Logger.getLogger(MainScreen.class.getName());

    private static String windowHeader(int listSize){
        return "Вимірювальні канали [кількість : "+ listSize + "]";
    }

    public ChannelTable channelTable;
    private MenuBar menuBar;
    private InfoTable infoTable;
    public ButtonsPanel buttonsPanel;
    public SearchPanel searchPanel;

    private List<Channel> channelsList;

    public MainScreen(SQLiteRepositoryFactory repositoryFactory){
        super();
        ChannelRepository channelRepository = repositoryFactory.create(ChannelRepository.class);

        channelsList = new ArrayList<>(channelRepository.getAll());
        this.setTitle(windowHeader(channelsList.size()));

        this.createElements(channelsList);
        this.setReactions();
        this.build();
    }

    private void createElements(@Nonnull List<Channel>channelList) {
        this.menuBar = new MenuBar(this);
        this.channelTable = new ChannelTable(this, channelList);
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

        mainPanel.add(new JScrollPane(this.channelTable), new CellBuilder()
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
        this.channelTable.setList(list);
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