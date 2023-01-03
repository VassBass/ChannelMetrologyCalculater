package ui.model;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.annotation.Nonnull;

import static ui.event.EventManager.CLICK_NEGATIVE_BUTTON;

public class NegativeButton extends DefaultButton {
    private final EventManager eventManager = EventManager.getInstance();

    public NegativeButton(@Nonnull EventSource eventSource, @Nonnull String text) {
        super(text);

        this.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_NEGATIVE_BUTTON));
    }
}
