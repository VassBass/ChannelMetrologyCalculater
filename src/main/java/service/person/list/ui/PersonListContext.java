package service.person.list.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.person.list.PersonListManager;
import service.person.list.ui.swing.SwingPersonListButtonsPanel;
import service.person.list.ui.swing.SwingPersonListTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PersonListContext {
    private static final Logger logger = LoggerFactory.getLogger(PersonListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private PersonListManager manager;

    public PersonListContext(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(PersonListTable.class) || clazz.isAssignableFrom(SwingPersonListTable.class)) {
                element = (T) new SwingPersonListTable(repositoryFactory);
                buffer.put(PersonListTable.class, element);
                buffer.put(SwingPersonListTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingPersonListButtonsPanel.class)) {
                element = (T) new SwingPersonListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(PersonListManager manager) {
        this.manager = manager;
    }
}
