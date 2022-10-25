package ui.channelInfo;

import ui.model.DefaultButton;

import javax.annotation.Nonnull;
import java.awt.event.ActionListener;

public class ButtonReset extends DefaultButton {
    private static final String RESET = "Скинути";

    private DialogChannel parent;

    ButtonReset(@Nonnull DialogChannel parent) {
        super(RESET);
        this.parent = parent;

        this.addActionListener(click);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final ActionListener click = e -> {
        parent.setChannelInfo(parent.oldChannel);
        parent.specialCharactersPanel.setFieldForInsert(null);
    };
}
