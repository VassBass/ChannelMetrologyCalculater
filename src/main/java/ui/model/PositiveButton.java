package ui.model;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.annotation.Nonnull;

import static ui.event.EventManager.CLICK_POSITIVE_BUTTON;

public class PositiveButton extends DefaultButton {
    private final EventManager eventManager = EventManager.getInstance();

    public PositiveButton(final @Nonnull EventSource eventSource, @Nonnull String text) {
        super(text);

        this.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_POSITIVE_BUTTON));
    }
}
