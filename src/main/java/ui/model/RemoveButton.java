package ui.model;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.annotation.Nonnull;

import static ui.event.EventManager.CLICK_REMOVE_BUTTON;

public class RemoveButton extends DefaultButton {
    private final EventManager eventManager = EventManager.getInstance();

    public RemoveButton(@Nonnull EventSource eventSource, @Nonnull String text) {
        super(text);

        this.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_REMOVE_BUTTON));
    }
}
