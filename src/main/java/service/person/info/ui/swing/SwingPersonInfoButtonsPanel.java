package service.person.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.info.PersonInfoManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingPersonInfoButtonsPanel extends DefaultPanel {

    public SwingPersonInfoButtonsPanel(@Nonnull PersonInfoManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton cancelButton = new DefaultButton(labels.get(RootLabelName.CANCEL));
        DefaultButton refreshButton = new DefaultButton(labels.get(RootLabelName.RESET));
        DefaultButton saveButton = new DefaultButton(labels.get(RootLabelName.SAVE));

        cancelButton.addActionListener(e -> manager.clickClose());
        refreshButton.addActionListener(e -> manager.clickRefresh());
        saveButton.addActionListener(e -> manager.clickSave());

        this.add(cancelButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(refreshButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
