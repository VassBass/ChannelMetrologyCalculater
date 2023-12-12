package service.measurement.converter.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.converter.ConverterManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {

    public SwingButtonsPanel(@Nonnull ConverterManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton convertButton = new DefaultButton(labels.get(RootLabelName.RECALCULATE));

        closeButton.addActionListener(e -> manager.shutdownService());
        convertButton.addActionListener(e -> manager.convert());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(convertButton, new CellBuilder().fill(NONE).x(1).build());
    }
}
