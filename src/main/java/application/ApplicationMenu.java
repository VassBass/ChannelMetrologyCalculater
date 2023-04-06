package application;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApplicationMenu extends JMenuBar {
    public static final String MENU_LISTS = "Списки";

    private final Map<String, JMenu> menus;

    public ApplicationMenu() {
        super();
        menus = new HashMap<>();
    }

    public void addMenuIfNotExist(@Nonnull String menuTitle) {
        if (!menus.containsKey(menuTitle)) {
            JMenu menu = new JMenu(menuTitle);
            menus.put(menuTitle, menu);
            this.add(menu);
        }
    }

    public void addMenuIfNotExist(@Nonnull JMenu menu) {
        String title = menu.getText();
        if (!menus.containsKey(title)) {
            menus.put(title, menu);
            this.add(menu);
        }
    }

    public void addMenuItem(@Nonnull String menuTitle, @Nonnull JMenuItem menuItem) {
        JMenu menu = menus.get(menuTitle);
        if (Objects.nonNull(menu)) {
            menu.add(menuItem);
        }
    }
}
