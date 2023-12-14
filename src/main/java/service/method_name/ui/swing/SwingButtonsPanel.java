package service.method_name.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.method_name.MethodNameManager;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {

    public  SwingButtonsPanel(MethodNameManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeBtn = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton saveBtn = new DefaultButton(labels.get(RootLabelName.SAVE));

        closeBtn.addActionListener(e -> manager.clickClose());
        saveBtn.addActionListener(e -> manager.clickSave());

        this.add(closeBtn, new CellBuilder().x(0).fill(NONE).build());
        this.add(saveBtn, new CellBuilder().x(1).fill(NONE).build());
    }
}
