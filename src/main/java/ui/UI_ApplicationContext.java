package ui;

import application.AbstractApplicationContext;
import ui.mainScreen.MainScreen;

public class UI_ApplicationContext implements AbstractApplicationContext {
    private static UI_ApplicationContext instance;

    public static UI_ApplicationContext getInstance() {
        if (instance == null) instance = new UI_ApplicationContext();
        return instance;
    }

    @Override
    public <T> T get (Class<T> clazz) {
        if (MainScreen.class.isAssignableFrom(clazz)) return (T) MainScreen.getInstance();

        return null;
    }
}
