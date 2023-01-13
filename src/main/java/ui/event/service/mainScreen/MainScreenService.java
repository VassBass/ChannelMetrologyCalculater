package ui.event.service.mainScreen;

import backgroundTasks.CheckChannel;
import backgroundTasks.SearchChannels;
import constants.Sort;
import developer.calculating.DialogOSChoose;
import service.ChannelSorter;
import service.FileBrowser;
import ui.calculate.start.DialogCalculateStart;
import ui.channelInfo.DialogChannel;
import ui.event.EventWithData;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import java.awt.*;
import java.util.Map;

import static ui.mainScreen.MainScreen.*;
import static ui.mainScreen.searchPanel.SearchPanel.*;
import static ui.mainScreen.searchPanel.SearchPanel.TEXT_SUITABILITY;

public class MainScreenService {

    private void showChannelInfoDialog() {
        final int channelIndex = MainScreen.getInstance().channelTable.getSelectedRow();
        if (channelIndex!=-1) {
            EventQueue.invokeLater(() -> {
                new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
            });
        }
    }

    private void showAddChannelDialog() {
        EventQueue.invokeLater(() ->
                new DialogChannel(null).setVisible(true));
    }

    private void showRemoveChannelsDialog() {
        if (MainScreen.getInstance().getChannelsList().size() > 0) {
            EventQueue.invokeLater(() ->
                    new DialogRemoveChannels(MainScreen.getInstance()).setVisible(true));
        }
    }

    private void showCalculateDialog() {
        int index = MainScreen.getInstance().channelTable.getSelectedRow();
        if (index != -1){
            new DialogCalculateStart(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
        }
    }

    private void openCertificateFolder() {
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

    private void showChooseOSDialog() {
        EventQueue.invokeLater(() -> {
            int index = MainScreen.getInstance().channelTable.getSelectedRow();
            if (index >= 0 && index < MainScreen.getInstance().getChannelsList().size()) {
                new DialogOSChoose(MainScreen.getInstance(), MainScreen.getInstance().getChannelsList().get(index))
                        .setVisible(true);
            }
        });
    }

    private void showChannelShortInfo() {
        int selectedRow = MainScreen.getInstance().channelTable.getSelectedRow();
        if (selectedRow != -1) {
            MainScreen.getInstance()
                    .updateChannelInfo(MainScreen.getInstance().getChannelsList().get(selectedRow));
        }
    }

    private void startSearchingChannels(EventWithData event) {
        Map<String, String> buffer = event.getStringBuffer();
        String field = buffer.get(KEY_SEARCH_FIELD_TEXT);
        String valueText = buffer.get(KEY_SEARCH_VALUE_TEXT);
        String valueListItem = buffer.get(KEY_SEARCH_VALUE_LIST_ITEM_TEXT);
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
                boolean valueBoolean = event.getBooleanBuffer().get(KEY_SEARCH_VALUE_BOOLEAN);
                new SearchChannels().startSearch(Sort.SUITABILITY, valueBoolean);
                break;
        }
    }

    private void endChannelsSearching() {
        new SearchChannels().cancel();
        ChannelSorter.getInstance().setOff();
    }
}
