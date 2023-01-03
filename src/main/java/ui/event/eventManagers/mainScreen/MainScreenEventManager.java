package ui.event.eventManagers.mainScreen;

import developer.calculating.OS_Chooser;
import service.FileBrowser;
import ui.channelInfo.DialogChannel;
import ui.event.AbstractEventManager;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import java.awt.*;

import static ui.event.EventManager.*;

public class MainScreenEventManager extends AbstractEventManager {
    public static final int CLICK_CALCULATE_BUTTON = 5;
    public static final int CLICK_FOLDER_BUTTON = 6;
    public static final int CLICK_CHOOSE_OS = 7;

    public static final int SELECT_TABLE_ROW = 8;

    @Override
    public void runEvent(int event) {
        switch (event) {
            case CLICK_INFO_BUTTON: clickInfoButton(); break;
            case CLICK_ADD_BUTTON: clickAddButton(); break;
            case CLICK_REMOVE_BUTTON: clickRemoveButton(); break;
            case CLICK_CALCULATE_BUTTON: clickCalculateButton(); break;
            case CLICK_FOLDER_BUTTON: clickFolderButton(); break;
            case CLICK_CHOOSE_OS: clickChooseOS(); break;

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


}
