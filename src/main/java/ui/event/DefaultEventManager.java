package ui.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEventManager extends AbstractEventManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventManager.class);

    private static DefaultEventManager instance;

    public static DefaultEventManager getInstance() {
        if (instance == null) instance = new DefaultEventManager();
        return instance;
    }

    private DefaultEventManager(){};

    @Override
    public void runEvent(Event event) {
        logger.info("This action not processed!");
    }
}
