package service.person.info.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.person.info.PersonInfoManager;
import service.person.info.ui.swing.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PersonInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(PersonInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private PersonInfoManager manager;

    public PersonInfoContext(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            logger.warn(Messages.Log.MISSING_UI_MANAGER_ERROR);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(PersonInfoNamePanel.class) || clazz.isAssignableFrom(SwingPersonInfoNamePanel.class)) {
                element = (T) new SwingPersonInfoNamePanel();
                buffer.put(PersonInfoNamePanel.class, element);
                buffer.put(SwingPersonInfoNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(PersonInfoSurnamePanel.class) || clazz.isAssignableFrom(SwingPersonInfoSurnamePanel.class)) {
                element = (T) new SwingPersonInfoSurnamePanel();
                buffer.put(PersonInfoSurnamePanel.class, element);
                buffer.put(SwingPersonInfoSurnamePanel.class, element);
            }
            if (clazz.isAssignableFrom(PersonInfoPatronymicPanel.class) || clazz.isAssignableFrom(SwingPersonInfoPatronymicPanel.class)) {
                element = (T) new SwingPersonInfoPatronymicPanel();
                buffer.put(PersonInfoPatronymicPanel.class, element);
                buffer.put(SwingPersonInfoPatronymicPanel.class, element);
            }
            if (clazz.isAssignableFrom(PersonInfoPositionPanel.class) || clazz.isAssignableFrom(SwingPersonInfoPositionPanel.class)) {
                element = (T) new SwingPersonInfoPositionPanel(repositoryFactory);
                buffer.put(PersonInfoPositionPanel.class, element);
                buffer.put(SwingPersonInfoPositionPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingPersonInfoButtonsPanel.class)) {
                element = (T) new SwingPersonInfoButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(PersonInfoManager manager) {
        this.manager = manager;
    }
}
