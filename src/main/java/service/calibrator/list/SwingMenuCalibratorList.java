package service.calibrator.list;

import application.ApplicationScreen;
import repository.RepositoryFactory;

import javax.swing.*;

public class SwingMenuCalibratorList extends JMenu {
    private static final String HEADER_TEXT = "Калібратори";
    private static final String LIST_TEXT = "Список";

    public SwingMenuCalibratorList(ApplicationScreen applicationScreen, RepositoryFactory repositoryFactory) {
        super(HEADER_TEXT);

        JMenuItem list = new JMenuItem(LIST_TEXT);
        list.addActionListener(e -> new CalibratorListExecuter(applicationScreen, repositoryFactory).execute());

        this.add(list);
    }
}
