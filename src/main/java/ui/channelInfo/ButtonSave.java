package ui.channelInfo;

import application.Application;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.annotation.Nonnull;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonSave extends DefaultButton {
    private static final String SAVE = "Зберегти (Alt + Enter)";

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private DialogChannel parent;

    ButtonSave(@Nonnull DialogChannel parent){
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
        if (parent.oldChannel == null) {
            channelRepository.add(parent.getChannel());
        }else {
            channelRepository.set(parent.oldChannel, parent.getChannel());
        }
        if (ChannelSorter.getInstance().isOn()){
            MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
        }else {
            MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
        }
    };
}
