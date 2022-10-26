package ui.channelInfo.button;

import application.Application;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.channelInfo.DialogChannel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.annotation.Nonnull;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

public class ButtonSave extends DefaultButton {
    private static final String SAVE = "Зберегти (Alt + Enter)";

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private DialogChannel parent;

    public ButtonSave(@Nonnull DialogChannel parent){
        super(SAVE);
        this.parent = parent;

        this.addActionListener(click);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final ActionListener click = e -> {
        parent.specialCharactersPanel.setFieldForInsert(null);
        if (!parent.isFieldsAreFilled()) return;

        Application.putHint(parent.panelName.getChannelName());
        parent.dispose();

        Optional<Channel> c = parent.getChannel();
        if (c.isPresent()) {
            Channel channel = c.get();

            if (parent.oldChannel == null) {
                channelRepository.add(channel);
            } else {
                channelRepository.set(parent.oldChannel, channel);
            }
            if (ChannelSorter.getInstance().isOn()) {
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
            } else {
                MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
            }
        }
    };
}
