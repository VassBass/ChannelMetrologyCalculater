package service.channel.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoMeasurementPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingChannelInfoMeasurementPanel extends TitledPanel implements ChannelInfoMeasurementPanel {

    private final DefaultComboBox measurementName;
    private final DefaultComboBox measurementValue;

    public SwingChannelInfoMeasurementPanel(ChannelInfoManager manager) {
        super(Labels.getRootLabels().get(RootLabelName.TYPE_OF_MEASUREMENT), Color.BLACK);

        measurementName = new DefaultComboBox(false);
        measurementValue = new DefaultComboBox(false);

        measurementName.addItemListener(e -> manager.changeMeasurementName());
        measurementValue.addItemListener(e -> manager.changeMeasurementValue());

        this.add(measurementName, new CellBuilder().x(0).build());
        this.add(measurementValue, new CellBuilder().x(1).build());
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

    @Override
    public String getSelectedMeasurementName() {
        return measurementName.getSelectedString();
    }

    @Override
    public String getSelectedMeasurementValue() {
        return measurementValue.getSelectedString();
    }
}
