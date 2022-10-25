package ui.channelInfo;

import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonRemove extends DefaultButton {
    private static final String REMOVE = "Видалити";

    private DialogChannel parent;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    ButtonRemove(@Nonnull DialogChannel parent) {
        super(REMOVE);
        this.parent = parent;

        this.addActionListener(click);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final ActionListener click = e -> {
        if (parent.oldChannel != null){
            EventQueue.invokeLater(() -> {
                int result = JOptionPane.showConfirmDialog(
                        parent,
                        parent.oldChannel.getName(),
                        "Видалити канал?",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (result == 0){
                    channelRepository.remove(parent.oldChannel);
                    parent.dispose();
                    if (ChannelSorter.getInstance().isOn()){
                        MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
                    }else {
                        MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
                    }
                }
            });
        }
    };
}
