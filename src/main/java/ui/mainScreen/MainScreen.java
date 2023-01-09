package ui.mainScreen;

import factory.AbstractFactory;
import model.Channel;
import repository.ChannelRepository;
import service.MainScreenEventListener;
import ui.event.Event;
import ui.mainScreen.buttonsPanel.ButtonsPanel;
import ui.mainScreen.channelTable.ChannelTable;
import ui.mainScreen.infoTable.InfoTable;
import ui.mainScreen.menu.MenuBar;
import ui.mainScreen.searchPanel.SearchPanel;
import ui.model.CellBuilder;
import ui.model.Window;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.awt.EventQueue.invokeLater;
import static ui.UI_ConfigHolder.SCREEN_SIZE;

public class MainScreen extends JFrame implements Window {
    public static final String KEY_CHANNEL = "channel";

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

    private final MainScreenEventListener eventListener;

    public MainScreen(AbstractFactory repositoryFactory, MainScreenEventListener eventListener){
        super();
        ChannelRepository channelRepository = repositoryFactory.create(ChannelRepository.class);
        this.eventListener = eventListener;

        channelsList = new ArrayList<>(channelRepository.getAll());
        this.setTitle(windowHeader(channelsList.size()));

        this.createElements(channelsList);
        this.setReactions();
        this.build();
    }

    private void createElements(@Nonnull List<Channel>channelList) {
        this.channelTable = new ChannelTable(this, channelList);
        this.menuBar = new MenuBar(eventListener, channelTable);
        this.infoTable = new InfoTable();
        this.buttonsPanel = new ButtonsPanel(this);
        this.searchPanel = new SearchPanel(this);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(eventListener.clickClose(Event.emptyEvent));
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
        this.refreshWindow();
    }

    @Override
    public void refreshWindow(){
        invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    @Override
    public void showWindow() {
        invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hideWindow() {
        invokeLater(this::dispose);
    }
}