package service.sensor_types.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.TitledTextField;
import service.sensor_types.info.ui.SensorTypesInfoTypePanel;

import java.awt.*;
import java.util.Map;

public class SwingSensorTypesInfoTypePanel extends TitledTextField implements SensorTypesInfoTypePanel {

    private static final Map<String, String> labels = Labels.getRootLabels();

    private final String oldType;

    public SwingSensorTypesInfoTypePanel(String oldType) {
        super(20, labels.get(RootLabelName.TYPE) + Labels.SPACE + labels.get(RootLabelName.SENSOR_SHORT), Color.BLACK);
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
