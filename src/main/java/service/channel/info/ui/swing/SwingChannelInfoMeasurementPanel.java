package service.channel.info.ui.swing;

import model.ui.TitledPanel;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoMeasurementPanel;

import javax.swing.*;
import java.util.List;

public class SwingChannelInfoMeasurementPanel extends TitledPanel implements ChannelInfoMeasurementPanel {
    private static final String TITLE_TEXT = "Вид вимірювання";

    private final JComboBox<String> measurementName;
    private final JComboBox<String> measurementValue;

    public SwingChannelInfoMeasurementPanel(final ChannelInfoManager manager) {
        super(TITLE_TEXT);

        measurementName = new JComboBox<>();
        measurementValue = new JComboBox<>();

        measurementName.addItemListener(e -> manager.changeMeasurementName());
        measurementValue.addItemListener(e -> manager.changeMeasurementValue());
    }

    @Override
    public void setMeasurementName(String name) {
        measurementName.setSelectedItem(name);
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValue.setSelectedItem(value);
    }

    @Override
    public void setMeasurementValues(List<String> values) {
        measurementValue.setModel(new DefaultComboBoxModel<>(values.toArray(new String[0])));
    }

    @Override
    public void setMeasurementNames(List<String> names) {
        measurementName.setModel(new DefaultComboBoxModel<>(names.toArray(new String[0])));
    }
}
