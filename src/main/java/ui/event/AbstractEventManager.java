package ui.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEventManager {
    private static final Logger logger = LoggerFactory.getLogger(AbstractEventManager.class);

    protected void notProcessed() {
        logger.info("This action not processed!");
    }

    public abstract void runEvent(Event event);
    public abstract <I> void  runEvent(Event event, I ... input);
}
