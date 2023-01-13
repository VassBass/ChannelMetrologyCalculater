package ui.event.manager.mainScreen;

import ui.event.AbstractEventManager;
import ui.event.EventType;
import ui.event.service.mainScreen.MainScreenService;

public class MainScreenEventManager extends AbstractEventManager {

    private final MainScreenService mainScreenService;

    public MainScreenEventManager(MainScreenService service) {
        service =
    }

    @Override
    public void runEvent(EventType event) {
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
}
