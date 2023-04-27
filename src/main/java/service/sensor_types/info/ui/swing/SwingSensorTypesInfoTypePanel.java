package service.sensor_types.info.ui.swing;

import model.ui.TitledTextField;
import service.sensor_types.info.ui.SensorTypesInfoTypePanel;

import java.awt.*;

public class SwingSensorTypesInfoTypePanel extends TitledTextField implements SensorTypesInfoTypePanel {
    private static final String TITLE_TEXT = "Тип ПВП";

    private final String oldType;

    public SwingSensorTypesInfoTypePanel(String oldType) {
        super(20, TITLE_TEXT, Color.BLACK);
        this.oldType = oldType;
        this.setText(oldType);
    }

    @Override
    public String getType() {
        String type = this.getText();
        if (type.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return type;
    }

    @Override
    public void refreshType() {
        this.setText(oldType);
    }
}
