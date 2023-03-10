package service.importer.ui;

import javax.swing.*;

public class SwingMenuImporter extends JMenu {
    private static final String HEADER_TEXT = "Імпорт";

    private static final String FROM_v5_4_TO_v6_0_TEXT = "Імпортувати з версії 5.4";

    public SwingMenuImporter() {
        super(HEADER_TEXT);

        JMenuItem btnFrom_v5_4_to_v6_0 = new JMenuItem(FROM_v5_4_TO_v6_0_TEXT);

        this.add(btnFrom_v5_4_to_v6_0);
    }
}
