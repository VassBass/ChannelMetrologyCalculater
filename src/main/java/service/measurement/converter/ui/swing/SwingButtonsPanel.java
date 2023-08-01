package service.measurement.converter.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.converter.ConverterManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {
    private static final String CLOSE_TEXT = "Закрити";
    private static final String CONVERT_TEXT = "Перерахувати";

    public SwingButtonsPanel(@Nonnull ConverterManager manager) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_TEXT);
        DefaultButton convertButton = new DefaultButton(CONVERT_TEXT);

        closeButton.addActionListener(e -> manager.shutdownService());
        convertButton.addActionListener(e -> manager.convert());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(convertButton, new CellBuilder().fill(NONE).x(1).build());
    }
}
