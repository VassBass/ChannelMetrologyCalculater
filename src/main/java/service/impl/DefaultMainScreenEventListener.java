package service.impl;

import factory.AbstractFactory;
import factory.ApplicationFactoryContext;
import factory.WindowFactory;
import model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileBrowser;
import service.MainScreenEventListener;
import ui.calculate.start.DialogCalculateStart;
import ui.channelInfo.DialogChannel;
import ui.event.Event;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.awt.EventQueue.invokeLater;
import static ui.mainScreen.MainScreen.KEY_CHANNEL;

public class DefaultMainScreenEventListener implements MainScreenEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMainScreenEventListener.class);

    private final ApplicationFactoryContext context = ApplicationFactoryContext.getInstance();
    private AbstractFactory windowFactory;

    private static final String CLOSE_WINDOW_TITLE_TEXT = "Вихід";
    private static final String CLOSE_WINDOW_QUESTION_TEXT = "Закрити програму?";

    @Override
    public <O> WindowListener clickClose(final Event<O> event) {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                invokeLater(() -> {
                    if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

                    MainScreen mainScreen = windowFactory.create(MainScreen.class);
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
     * @param event
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickInfoButton(Event<O> event) {
        return e -> {
            Channel channel = (Channel) event.getObjectBuffer().get(KEY_CHANNEL);
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            DialogChannel dialogChannel = windowFactory.create(DialogChannel.class);
            dialogChannel.setChannelInfo(channel);
            dialogChannel.showWindow();
        };
    }

    @Override
    public <O> ActionListener clickAddButton(Event<O> event) {
        return e -> {
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            windowFactory.create(DialogChannel.class).showWindow();
        };
    }

    /**
     * @param event
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickRemoveButton(Event<O> event) {
        return e -> {
            Channel channel = (Channel) event.getObjectBuffer().get(KEY_CHANNEL);
            if (windowFactory == null) windowFactory = context.getFactory(WindowFactory.class);

            DialogRemoveChannels dialogRemoveChannels = windowFactory.create(DialogRemoveChannels.class);
            dialogRemoveChannels.setChannelInfo(channel);
            dialogRemoveChannels.showWindow();
        };
    }

    /**
     * @param event
     * @param <O> - {@link Channel}
     */
    @Override
    public <O> ActionListener clickCalculateButton(Event<O> event) {
        return e -> {
            Channel channel = (Channel) event.getObjectBuffer().get(KEY_CHANNEL);
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
    public <O> ActionListener clickOpenFolderButton(Event<O> event) {
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
}
