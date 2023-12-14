package service.person.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.list.PersonListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingPersonListButtonsPanel extends DefaultPanel {

    public SwingPersonListButtonsPanel(@Nonnull PersonListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton changeButton = new DefaultButton(labels.get(RootLabelName.CHANGE));
        DefaultButton addButton = new DefaultButton(labels.get(RootLabelName.ADD));

        closeButton.addActionListener(e -> manager.clickClose());
        changeButton.addActionListener(e -> manager.clickChange());
        addButton.addActionListener(e -> manager.clickAdd());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
