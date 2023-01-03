package ui.event;

public abstract class ReturnsResultEventManager<V> extends AbstractEventManager {
    public abstract V runEventWithResult(int event);
}
