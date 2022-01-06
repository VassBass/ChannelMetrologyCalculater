package ui.main;

import application.Application;
import model.Channel;
import constants.Strings;
import ui.DialogExit;
import ui.main.info_panel.InfoPanel;
import ui.main.info_panel.complex_elements.InfoPanel_buttonsPanel;
import ui.main.info_panel.complex_elements.InfoPanel_infoTable;
import ui.main.info_panel.complex_elements.InfoPanel_searchPanel;
import ui.main.menu.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class MainScreen extends JFrame {
    private final JFrame current;

    private static final String windowHeader = "Вимірювальні канали";

    private MenuBar menuBar;
    private InfoPanel infoPanel;

    public ArrayList<Channel>channelsList;

    public InfoPanel_infoTable infoTable;
    public InfoPanel_buttonsPanel buttonsPanel;
    public InfoPanel_searchPanel searchPanel;
    public MainTable mainTable;

    public MainScreen(){
        super(windowHeader);
        this.current = this;
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
        this.infoPanel = new InfoPanel(this);
        this.infoTable = this.infoPanel.infoTable;
        this.buttonsPanel = this.infoPanel.buttonsPanel;
        this.searchPanel = this.infoPanel.searchPanel;
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
    }

    private void build() {
        this.localizationOfComponents();
        this.setSize(Application.sizeOfScreen);
        this.setJMenuBar(this.menuBar);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(this.infoPanel, new Cell(0, 0, 0.1));
        mainPanel.add(new JScrollPane(this.mainTable), new Cell(0, 1, 0.9));

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

    public void update(ArrayList<Channel>channelsList, boolean searchOn, String searchFiled, String searchValue){
        //this.searchPanel.update(searchOn, searchFiled, searchValue);
        this.channelsList = channelsList;
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
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new DialogExit(current).setVisible(true);
                }

            });
        }
    };

    private void localizationOfComponents(){
        //File chooser
        UIManager.put("FileChooser.cancelButtonText", Strings.CANCEL);
        UIManager.put("FileChooser.openButtonText", Strings.IMPORT);
        UIManager.put("FileChooser.fileNameLabelText", Strings.FILE_NAME);
        UIManager.put("FileChooser.filesOfTypeLabelText", Strings.TYPES_OF_FILES);
        UIManager.put("FileChooser.lookInLabelText", Strings.DIRECTORY);
        UIManager.put("FileChooser.acceptAllFileFilterText", Strings.ALL_FILES);
        UIManager.put("FileChooser.cancelButtonToolTipText", Strings.CLOSE_WINDOW);
        UIManager.put("FileChooser.directoryOpenButtonText", Strings.OPEN);
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", Strings.OPEN_SELECTED_DIRECTORY);
        UIManager.put("FileChooser.openButtonToolTipText", Strings.OPEN_SELECTED_FILE);
        UIManager.put("FileChooser.detailsViewActionLabelText", Strings.TABLE);
        UIManager.put("FileChooser.detailsViewButtonToolTipText", Strings.TABLE);
        UIManager.put("FileChooser.fileDateHeaderText", Strings.DATE_LAST_CHANGE);
        UIManager.put("FileChooser.fileNameHeaderText", Strings._NAME);
        UIManager.put("FileChooser.fileSizeHeaderText", Strings.SIZE);
        UIManager.put("FileChooser.fileTypeHeaderText", Strings.TYPE);
        UIManager.put("FileChooser.homeFolderToolTipText", Strings.TO_HOME);
        UIManager.put("FileChooser.listViewActionLabelText", Strings.LIST);
        UIManager.put("FileChooser.listViewButtonToolTipText", Strings.LIST);
        UIManager.put("FileChooser.newFolderActionLabelText", Strings.CREATE_NEW_FOLDER);
        UIManager.put("FileChooser.newFolderToolTipText", Strings.CREATE_NEW_FOLDER);
        UIManager.put("FileChooser.refreshActionLabelText", Strings.UPDATE);
        UIManager.put("FileChooser.upFolderToolTipText", Strings.UP_FOLDER);
        UIManager.put("FileChooser.viewMenuLabelText", Strings.MODE_OF_VIEW);

        //ToolTip
        UIManager.put("ToolTip.background", Color.BLACK);
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

    private static class Cell extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        protected Cell(int x, int y, double weighty) {
            this.fill = BOTH;
            this.weightx = 1.0;

            this.gridx = x;
            this.gridy = y;
            this.weighty = weighty;
        }

    }
}
