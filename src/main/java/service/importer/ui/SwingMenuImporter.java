package service.importer.ui;

import service.importer.updater.from_v5_4.to_v6_0.From_v5_4_to_v6_0_ImporterExecuter;

import javax.swing.*;

public class SwingMenuImporter extends JMenu {
    private static final String HEADER_TEXT = "Імпорт";

    private static final String FROM_v5_4_TO_v6_0_TEXT = "Імпортувати з версії 5.4";

    public SwingMenuImporter() {
        super(HEADER_TEXT);

        JMenuItem btnFrom_v5_4_to_v6_0 = new JMenuItem(FROM_v5_4_TO_v6_0_TEXT);

        btnFrom_v5_4_to_v6_0.addActionListener(e -> new From_v5_4_to_v6_0_ImporterExecuter().execute());

        this.add(btnFrom_v5_4_to_v6_0);
    }
}
