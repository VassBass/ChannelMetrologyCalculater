package ui.model;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.annotation.Nonnull;

import static ui.event.Event.CLICK_ADD_BUTTON;

public class AddButton extends DefaultButton {
    private final EventManager eventManager = EventManager.getInstance();

    public AddButton(@Nonnull EventSource eventSource, @Nonnull String text) {
        super(text);

        this.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_ADD_BUTTON));
    }
}
