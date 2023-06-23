package service.method_name.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.method_name.MethodNameManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonsPanel extends DefaultPanel {
    private static final String CLOSE_BTN_TEXT = "Закрити";
    private static final String SAVE_BTN_TEXT = "Зберегти";

    public  SwingButtonsPanel(MethodNameManager manager) {
        super();

        DefaultButton closeBtn = new DefaultButton(CLOSE_BTN_TEXT);
        DefaultButton saveBtn = new DefaultButton(SAVE_BTN_TEXT);

        closeBtn.addActionListener(e -> manager.clickClose());
        saveBtn.addActionListener(e -> manager.clickSave());

        this.add(closeBtn, new CellBuilder().x(0).fill(NONE).build());
        this.add(saveBtn, new CellBuilder().x(1).fill(NONE).build());
    }
}
