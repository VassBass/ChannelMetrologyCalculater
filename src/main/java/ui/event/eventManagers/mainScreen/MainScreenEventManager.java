package ui.event.eventManagers.mainScreen;

import backgroundTasks.CheckChannel;
import backgroundTasks.SearchChannels;
import constants.Sort;
import developer.calculating.OS_Chooser;
import service.ChannelSorter;
import service.DataTransfer;
import service.FileBrowser;
import ui.channelInfo.DialogChannel;
import ui.event.AbstractEventManager;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import java.awt.*;

import static ui.event.EventManager.*;
import static ui.mainScreen.searchPanel.SearchPanel.*;

public class MainScreenEventManager extends AbstractEventManager {
    public static final int CLICK_CALCULATE_BUTTON = 5;
    public static final int CLICK_FOLDER_BUTTON = 6;
    public static final int CLICK_CHOOSE_OS = 7;
    public static final int CLICK_SEARCH_BUTTON_START = 9;
    public static final int CLICK_SEARCH_BUTTON_END = 10;

    public static final int SELECT_TABLE_ROW = 8;

    public static final String KEY_SEARCH_FIELD = "search_field";
    public static final String KEY_SEARCH_VALUE_TEXT = "search_value_text";
    public static final String KEY_SEARCH_VALUE_LIST_ITEM = "search_value_list_item";
    public static final String KEY_SEARCH_VALUE_BOOLEAN = "search_value_boolean";

    private final DataTransfer dataTransfer = DataTransfer.getInstance();

    @Override
    public void runEvent(int event) {
        switch (event) {
            case CLICK_INFO_BUTTON: clickInfoButton(); break;
            case CLICK_ADD_BUTTON: clickAddButton(); break;
            case CLICK_REMOVE_BUTTON: clickRemoveButton(); break;
            case CLICK_CALCULATE_BUTTON: clickCalculateButton(); break;
            case CLICK_FOLDER_BUTTON: clickFolderButton(); break;
            case CLICK_CHOOSE_OS: clickChooseOS(); break;
            case CLICK_SEARCH_BUTTON_START: clickSearchButtonStart(); break;
            case CLICK_SEARCH_BUTTON_END: clickSearchButtonEnd(); break;

            case SELECT_TABLE_ROW: selectTableRow(); break;

            default: notProcessed(); break;
        }
    }

    private void clickInfoButton() {
        final int channelIndex = MainScreen.getInstance().mainTable.getSelectedRow();
        if (channelIndex!=-1) {
            EventQueue.invokeLater(() -> {
                //new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
            });
        }
    }

    private void clickAddButton() {
        EventQueue.invokeLater(() ->
                new DialogChannel(null).setVisible(true));
    }

    private void clickRemoveButton() {
        if (MainScreen.getInstance().getChannelsList().size() > 0) {
            EventQueue.invokeLater(() ->
                    new DialogRemoveChannels(MainScreen.getInstance()).setVisible(true));
        }
    }

    private void clickCalculateButton() {
        int index = MainScreen.getInstance().mainTable.getSelectedRow();
        if (index != -1){
            //new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
        }
    }

    private void clickFolderButton() {
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

    private void clickChooseOS() {
        EventQueue.invokeLater(() -> {
            int index = MainScreen.getInstance().mainTable.getSelectedRow();
            if (index >= 0 && index < MainScreen.getInstance().getChannelsList().size()) {
                new OS_Chooser(MainScreen.getInstance(), MainScreen.getInstance().getChannelsList().get(index))
                        .setVisible(true);
            }
        });
    }

    private void selectTableRow() {
        int selectedRow = MainScreen.getInstance().mainTable.getSelectedRow();
        if (selectedRow != -1) {
            MainScreen.getInstance()
                    .updateChannelInfo(MainScreen.getInstance().getChannelsList().get(selectedRow));
        }
    }

    private void clickSearchButtonStart() {
        String field = dataTransfer.extractString(KEY_SEARCH_FIELD);
        String valueText = dataTransfer.extractString(KEY_SEARCH_VALUE_TEXT);
        String valueListItem = dataTransfer.extractString(KEY_SEARCH_VALUE_LIST_ITEM);
        switch (field) {
            case TEXT_CODE:
                new CheckChannel(MainScreen.getInstance(), valueText).start();
                break;
            case TEXT_NAME:
                new SearchChannels().startSearch(Sort.NAME, valueText);
                break;
            case TEXT_MEASUREMENT_NAME:
                new SearchChannels().startSearch(Sort.MEASUREMENT_NAME, valueListItem);
                break;
            case TEXT_MEASUREMENT_VALUE:
                new SearchChannels().startSearch(Sort.MEASUREMENT_VALUE, valueListItem);
                break;
            case TEXT_DEPARTMENT:
                new SearchChannels().startSearch(Sort.DEPARTMENT, valueListItem);
                break;
            case TEXT_AREA:
                new SearchChannels().startSearch(Sort.AREA, valueListItem);
                break;
            case TEXT_PROCESS:
                new SearchChannels().startSearch(Sort.PROCESS, valueListItem);
                break;
            case TEXT_INSTALLATION:
                new SearchChannels().startSearch(Sort.INSTALLATION, valueListItem);
                break;
            case TEXT_DATE:
                new SearchChannels().startSearch(Sort.DATE, valueText);
                break;
            case TEXT_FREQUENCY:
                new SearchChannels().startSearch(Sort.FREQUENCY, valueText);
                break;
            case TEXT_TECHNOLOGY_NUMBER:
                new SearchChannels().startSearch(Sort.TECHNOLOGY_NUMBER, valueText);
                break;
            case TEXT_SENSOR_NAME:
                new SearchChannels().startSearch(Sort.SENSOR_NAME, valueText);
                break;
            case TEXT_SENSOR_TYPE:
                new SearchChannels().startSearch(Sort.SENSOR_TYPE, valueListItem);
                break;
            case TEXT_PROTOCOL_NUMBER:
                new SearchChannels().startSearch(Sort.PROTOCOL_NUMBER, valueText);
                break;
            case TEXT_REFERENCE:
                new SearchChannels().startSearch(Sort.REFERENCE, valueText);
                break;
            case TEXT_SUITABILITY:
                boolean valueBoolean = dataTransfer.extractBoolean(KEY_SEARCH_VALUE_BOOLEAN).orElse(false);
                new SearchChannels().startSearch(Sort.SUITABILITY, valueBoolean);
                break;
        }
    }

    private void clickSearchButtonEnd() {
        new SearchChannels().cancel();
        ChannelSorter.getInstance().setOff();
    }
}
