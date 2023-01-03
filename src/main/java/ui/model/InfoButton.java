package ui.model;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.annotation.Nonnull;

import static ui.event.Event.CLICK_INFO_BUTTON;

public class InfoButton extends DefaultButton {
    private final EventManager eventManager = EventManager.getInstance();

    public InfoButton(@Nonnull EventSource eventSource, @Nonnull String text) {
        super(text);

        this.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_INFO_BUTTON));
    }
}
