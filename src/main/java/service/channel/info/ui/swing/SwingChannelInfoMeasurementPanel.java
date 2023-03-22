package service.channel.info.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoMeasurementPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SwingChannelInfoMeasurementPanel extends TitledPanel implements ChannelInfoMeasurementPanel {
    private static final String TITLE_TEXT = "Вид вимірювання";

    private final DefaultComboBox measurementName;
    private final DefaultComboBox measurementValue;

    public SwingChannelInfoMeasurementPanel(RepositoryFactory repositoryFactory, ChannelInfoManager manager) {
        super(TITLE_TEXT, Color.BLACK);

        measurementName = new DefaultComboBox(false);
        measurementValue = new DefaultComboBox(false);

        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        if (Objects.nonNull(measurementRepository)) setMeasurementNames(Arrays.asList(measurementRepository.getAllNames()));

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
        return measurementName.getSelectedItem();
    }

    @Override
    public String getSelectedMeasurementValue() {
        return measurementValue.getSelectedItem();
    }
}
