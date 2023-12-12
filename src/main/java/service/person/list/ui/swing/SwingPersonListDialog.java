package service.person.list.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.list.PersonListConfigHolder;
import service.person.list.ui.PersonListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class SwingPersonListDialog extends DefaultDialog {

    private final SwingPersonListTable table;

    public SwingPersonListDialog(@Nonnull ApplicationScreen applicationScreen,
                                 @Nonnull PersonListContext context,
                                 @Nonnull PersonListConfigHolder configHolder) {
        super(applicationScreen, Labels.getRootLabels().get(RootLabelName.PERSONS_LIST));

        table = context.getElement(SwingPersonListTable.class);
        SwingPersonListButtonsPanel buttonsPanel = context.getElement(SwingPersonListButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(new JScrollPane(table), new CellBuilder().weightY(0.95).y(0).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.05).y(1).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        table.updateContent();
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }
}
