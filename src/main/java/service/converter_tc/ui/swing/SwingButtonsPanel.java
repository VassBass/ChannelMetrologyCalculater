package service.converter_tc.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.converter_tc.ConverterTcManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {
    private static final String CLOSE_TEXT = "Закрити";
    private static final String CALCULATE_TEXT = "Розрахувати";

    public SwingButtonsPanel(@Nonnull ConverterTcManager manager) {
        super();

        DefaultButton closeBtn = new DefaultButton(CLOSE_TEXT);
        DefaultButton calculateBtn = new DefaultButton(CALCULATE_TEXT);

        closeBtn.addActionListener(e -> manager.close());
        calculateBtn.addActionListener(e -> manager.calculate());

        this.add(closeBtn, new CellBuilder().x(0).fill(NONE).build());
        this.add(calculateBtn, new CellBuilder().x(1).fill(NONE).build());
    }
}
