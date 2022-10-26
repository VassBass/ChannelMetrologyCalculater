package ui.channelInfo.button;

import ui.channelInfo.DialogChannel;
import ui.model.DefaultButton;

import javax.annotation.Nonnull;

public class ButtonClose extends DefaultButton {
    private static final String CLOSE = "Закрити (Alt + Esc)";

    public ButtonClose(@Nonnull DialogChannel parent) {
        super(CLOSE);

        this.addActionListener(e -> parent.dispose());
    }
}
