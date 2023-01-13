package service.impl;

import backgroundTasks.CheckChannel;
import backgroundTasks.SearchChannels;
import constants.Sort;
import converters.VariableConverter;
import developer.calculating.DialogOSChoose;
import factory.ApplicationFactoryContext;
import factory.WindowFactory;
import model.Channel;
import org.apache.commons.validator.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileBrowser;
import service.MainScreenEventListener;
import ui.calculate.start.DialogCalculateStart;
import ui.channelInfo.DialogChannel;
import ui.event.EventDataSource;
import ui.mainScreen.MainScreen;
import ui.model.CellBuilder;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

import static java.awt.EventQueue.invokeLater;
import static ui.mainScreen.MainScreen.*;
import static ui.mainScreen.searchPanel.SearchPanel.*;

public class DefaultMainScreenEventListener implements MainScreenEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMainScreenEventListener.class);

    private final ApplicationFactoryContext context = ApplicationFactoryContext.getInstance();
    private WindowFactory windowFactory;

    private static final String CLOSE_WINDOW_TITLE_TEXT = "Вихід";
    private static final String CLOSE_WINDOW_QUESTION_TEXT = "Закрити програму?";

    @Override
    public <O> WindowListener clickClose(final EventDataSource<O> eventDataSource) {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                invokeLater(() -> {
                    if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

                    MainScreen mainScreen = windowFactory.getMainScreen();
                    int result = JOptionPane.showConfirmDialog(
                            mainScreen,
                            CLOSE_WINDOW_QUESTION_TEXT,
                            CLOSE_WINDOW_TITLE_TEXT,
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == 0) System.exit(0);
                });
            }
        };
    }

    /**
     * @param eventDataSource
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickInfoButton(EventDataSource<O> eventDataSource) {
        return e -> {
            Channel channel = (Channel) eventDataSource.getObjectBuffer().get(KEY_CHANNEL);
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            DialogChannel dialogChannel = windowFactory.create(DialogChannel.class);
            dialogChannel.setChannelInfo(channel);
            dialogChannel.showWindow();
        };
    }

    @Override
    public <O> ActionListener clickAddButton(EventDataSource<O> eventDataSource) {
        return e -> {
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            windowFactory.create(DialogChannel.class).showWindow();
        };
    }

    /**
     * @param eventDataSource
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickRemoveButton(EventDataSource<O> eventDataSource) {
        return e -> {
            Channel channel = (Channel) eventDataSource.getObjectBuffer().get(KEY_CHANNEL);
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            DialogRemoveChannels dialogRemoveChannels = windowFactory.create(DialogRemoveChannels.class);
            dialogRemoveChannels.setChannelInfo(channel);
            dialogRemoveChannels.showWindow();
        };
    }

    /**
     * @param eventDataSource
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickCalculateButton(EventDataSource<O> eventDataSource) {
        return e -> {
            Channel channel = (Channel) eventDataSource.getObjectBuffer().get(KEY_CHANNEL);
            if (channel == null) {
                logger.warn("Channel must be not null!");
            } else {
                if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

                DialogCalculateStart dialogCalculateStart = windowFactory.create(DialogCalculateStart.class);
                dialogCalculateStart.setChannel(channel);
                dialogCalculateStart.showWindow();
            }
        };
    }

    @Override
    public <O> ActionListener clickOpenFolderButton(EventDataSource<O> eventDataSource) {
        return e -> {
            Desktop desktop;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(FileBrowser.DIR_CERTIFICATES);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    @Override
    public <O> ListSelectionListener selectChannel(EventDataSource<O> eventDataSource) {
        return e -> {
            Channel channel = (Channel) eventDataSource.getObjectBuffer().get(KEY_CHANNEL);

            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            MainScreen mainScreen = windowFactory.getMainScreen();
            mainScreen.updateChannelInfo(channel);
        };
    }

    @Override
    public <O> ActionListener clickChooseOS(EventDataSource<O> eventDataSource) {
        return e -> {
            Channel channel = (Channel) eventDataSource.getObjectBuffer().get(KEY_CHANNEL);
            if (channel == null) {
                logger.warn("Channel must be not null!");
            } else {
                if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

                MainScreen mainScreen = windowFactory.getMainScreen();
                DialogOSChoose dialogOSChoose = windowFactory.create(DialogOSChoose.class);
                dialogOSChoose.setChannel(channel);
                dialogOSChoose.showWindow();
            }
        };
    }

    @Override
    public <O> ActionListener clickSearchButton(EventDataSource<O> eventDataSource) {
        return e -> {
            Map<String, Boolean> booleanBuffer = eventDataSource.getBooleanBuffer();
            boolean searchStart = booleanBuffer.get(KEY_SEARCH_START);
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);
            MainScreen mainScreen = windowFactory.getMainScreen();
            if (searchStart) {
                Map<String, String> stringBuffer = eventDataSource.getStringBuffer();
                String searchField = stringBuffer.get(KEY_SEARCH_FIELD_TEXT);

                startSearch(searchField, stringBuffer, booleanBuffer);
                if (!searchField.equals(TEXT_CODE)) mainScreen.setEnabledSearchPanel(false);
            } else {
                new SearchChannels().cancel();
                mainScreen.setEnabledSearchPanel(true);
            }
        };
    }

    @Override
    public <O> ItemListener changeSearchField(EventDataSource<O> eventDataSource) {
        return e -> {
            String selectedField = eventDataSource.getStringBuffer().get(KEY_SEARCH_FIELD_TEXT);
            if (windowFactory == null) context.getFactory(WindowFactory.class);
            MainScreen mainScreen = windowFactory.getMainScreen();
            int mode = TEXT_MODE;
            switch (selectedField) {
                case TEXT_MEASUREMENT_NAME:
                case TEXT_MEASUREMENT_VALUE:
                case TEXT_SENSOR_TYPE:
                case TEXT_DEPARTMENT:
                case TEXT_AREA:
                case TEXT_PROCESS:
                case TEXT_INSTALLATION:
                    mode = LIST_MODE;
                    break;
                case TEXT_SUITABILITY:
                    mode = CHECK_MODE;
                    break;
            }
            mainScreen.rebuildSearchPanel(mode, selectedField);
        };
    }

    @Override
    public <O> FocusListener focusOnSearchValue(EventDataSource<O> eventDataSource) {
        if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);
        MainScreen mainScreen = windowFactory.getMainScreen();
        new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                mainScreen.selectAllTextInSearchValue();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String str = connect.inputValue.getText();
                if (connect.listSearchFields.getSelectedIndex() == Sort.FREQUENCY){
                    connect.inputValue.setText(VariableConverter.doubleString(str));
                }else if (connect.listSearchFields.getSelectedIndex() == Sort.DATE){
                    String str1 = VariableConverter.commasToDots(str);
                    DateValidator dateValidator = DateValidator.getInstance();
                    if (dateValidator.isValid(str1,"dd.MM.yyyy", false)){
                        connect.inputValue.setText(VariableConverter.stringToDateString(str));
                    }else {
                        connect.inputValue.setText(TEXT_DEFAULT_DATE);
                    }
                }
            }
        };
    }


    private void startSearch(String searchField,
                             Map<String, String>stringBuffer, Map<String, Boolean> booleanBuffer) {
        String value = "";
        switch (searchField) {
            case TEXT_CODE:
                if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);
                value = stringBuffer.get(KEY_SEARCH_VALUE_TEXT);
                new CheckChannel(windowFactory.getMainScreen(), value).start();
                break;
            case TEXT_NAME:
            case TEXT_DATE:
            case TEXT_FREQUENCY:
            case TEXT_TECHNOLOGY_NUMBER:
            case TEXT_SENSOR_NAME:
            case TEXT_PROTOCOL_NUMBER:
            case TEXT_REFERENCE:
                value = stringBuffer.get(KEY_SEARCH_VALUE_TEXT);
                break;
            case TEXT_MEASUREMENT_NAME:
            case TEXT_MEASUREMENT_VALUE:
            case TEXT_DEPARTMENT:
            case TEXT_AREA:
            case TEXT_PROCESS:
            case TEXT_INSTALLATION:
                value = stringBuffer.get(KEY_SEARCH_VALUE_LIST_ITEM_TEXT);
                break;
            case TEXT_SENSOR_TYPE:
                break;
            case TEXT_SUITABILITY:
                new SearchChannels().startSearch(TEXT_SUITABILITY, booleanBuffer.get(KEY_SEARCH_VALUE_BOOLEAN));
                return;
        }

        new SearchChannels().startSearch(searchField, value);
    }
}
