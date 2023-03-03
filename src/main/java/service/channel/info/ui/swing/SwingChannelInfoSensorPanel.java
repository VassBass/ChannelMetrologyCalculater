package service.channel.info.ui.swing;

import model.ui.*;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoSensorPanel;
import util.StringHelper;

import java.awt.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoSensorPanel extends TitledPanel implements ChannelInfoSensorPanel {
    private static final String TITLE_TEXT = "ПВП";
    private static final String SERIAL_NUMBER_TITLE_TEXT = "SN.";
    private static final String TOOLTIP_TEXT = "Первинний вимірювальний пристрій";
    private static final String RANGE_TITLE_TEXT = "Діапазон";
    private static final String CHECK_BOX_TEXT = "Однакові діапазони";

    private final DefaultComboBox sensorsNames;
    private final TitledTextField serialNumber;
    private final TitledPanel rangePanel;
    private final DefaultTextField rangeMin, rangeMax;
    private final DefaultComboBox measurementValues;
    private final DefaultCheckBox equalsRanges;

    public SwingChannelInfoSensorPanel(final ChannelInfoManager manager) {
        super(TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);

        sensorsNames = new DefaultComboBox(false);
        serialNumber = new TitledTextField(SERIAL_NUMBER_TITLE_TEXT, 15);
        rangeMin = new DefaultTextField(5);
        rangeMax = new DefaultTextField(5);

        equalsRanges = new DefaultCheckBox(CHECK_BOX_TEXT);
        equalsRanges.addItemListener(e -> {
            if (equalsRanges.isSelected()) {
                manager.setChannelAndSensorRangesEqual();
            } else setRangePanelEnabled(true);
        });

        measurementValues = new DefaultComboBox(false);

        rangePanel = new TitledPanel(RANGE_TITLE_TEXT);
        rangePanel.add(rangeMin, new CellBuilder().x(0).y(0).build());
        rangePanel.add(new DefaultLabel("..."), new CellBuilder().x(1).y(0).build());
        rangePanel.add(rangeMax, new CellBuilder().x(2).y(0).build());
        rangePanel.add(measurementValues, new CellBuilder().x(3).y(0).build());
        rangePanel.add(equalsRanges, new CellBuilder().x(0).y(1).width(4).build());

        this.add(sensorsNames, new CellBuilder().x(0).y(0).build());
        this.add(serialNumber, new CellBuilder().x(1).y(0).build());
        this.add(rangePanel, new CellBuilder().x(0).y(1).width(2).build());
    }

    @Override
    public void setSensorsNames(List<String> sensorsNames) {
        this.sensorsNames.setList(sensorsNames);
    }

    @Override
    public void setMeasurementValues(List<String> values) {
        this.measurementValues.setList(values);
    }

    @Override
    public void setRange(String min, String max) {
        rangeMin.setText(min);
        rangeMax.setText(max);
    }

    @Override
    public void setRangeMin(String min) {
        rangeMin.setText(min);
    }

    @Override
    public void setRangeMax(String max) {
        rangeMax.setText(max);
    }

    @Override
    public void setRangePanelEnabled(boolean enabled) {
        rangeMin.setEnabled(enabled);
        rangeMax.setEnabled(enabled);
        measurementValues.setEnabled(enabled);
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        this.serialNumber.setText(serialNumber);
    }

    @Override
    public String getSelectedSensorName() {
        return sensorsNames.getSelectedItem();
    }

    @Override
    public String getRangeMin() {
        String min = rangeMin.getText();
        return StringHelper.isDouble(min) ?
                min :
                EMPTY;
    }

    @Override
    public String getRangeMax() {
        String max = rangeMax.getText();
        return StringHelper.isDouble(max) ?
                max :
                EMPTY;
    }

    @Override
    public String getSerialNumber() {
        return serialNumber.getText();
    }

    @Override
    public boolean isRangeValid() {
        String min = getRangeMin();
        String max = getRangeMax();
        if (min.isEmpty() || max.isEmpty()) {
            rangePanel.setTitleColor(Color.RED);
            return false;
        }

        if (Double.parseDouble(min) <= Double.parseDouble(max)) {
            rangePanel.setTitleColor(Color.RED);
            return false;
        } else {
            rangePanel.setTitleColor(Color.BLACK);
            return true;
        }
    }
}
