package ui.event;

public abstract class ReturnsResultEventManager<V> extends AbstractEventManager {
    public abstract V runEventWithResult(Event event);
    public abstract <I> V runEventWithResult(Event event, I ... input);
}
