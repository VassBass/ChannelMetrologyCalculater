package service.converter_tc.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.converter_tc.ConverterTcManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {

    public SwingButtonsPanel(@Nonnull ConverterTcManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeBtn = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton calculateBtn = new DefaultButton(labels.get(RootLabelName.CALCULATE));

        closeBtn.addActionListener(e -> manager.close());
        calculateBtn.addActionListener(e -> manager.calculate());

        this.add(closeBtn, new CellBuilder().x(0).fill(NONE).build());
        this.add(calculateBtn, new CellBuilder().x(1).fill(NONE).build());
    }
}
