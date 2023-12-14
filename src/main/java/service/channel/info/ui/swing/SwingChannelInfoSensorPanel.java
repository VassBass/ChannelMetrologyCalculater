package service.channel.info.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.ui.*;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoSensorPanel;
import service.error_calculater.MxParserErrorCalculater;
import util.StringHelper;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoSensorPanel extends TitledPanel implements ChannelInfoSensorPanel {
    private static final String RANGES_EQUALS = "rangesEquals";
    private static final String SENSOR_ERROR_FORMULA = "sensorErrorFormula";

    private final DefaultComboBox sensorsTypes;
    private final TitledTextField serialNumber;
    private final TitledPanel rangePanel;
    private final DefaultTextField rangeMin, rangeMax;
    private final DefaultComboBox measurementValues;
    private final DefaultCheckBox equalsRanges;
    private final DefaultLabel errorLabel;
    private final DefaultComboBox errorFormulas;

    private static final Map<String, String> rootLabels = Labels.getRootLabels();

    public SwingChannelInfoSensorPanel(final ChannelInfoManager manager) {
        super(rootLabels.get(RootLabelName.SENSOR_SHORT), Color.BLACK);
        this.setToolTipText(rootLabels.get(RootLabelName.SENSOR_LONG));
        Map<String, String> labels = Labels.getLabels(SwingChannelInfoSensorPanel.class);
        Map<String, String> messages = Messages.getRootMessages();

        sensorsTypes = new DefaultComboBox(true);
        sensorsTypes.addItemListener(e -> manager.setExpectedSensorInfo());

        serialNumber = new TitledTextField(15, rootLabels.get(RootLabelName.SERIAL_NUMBER_SHORT));
        rangeMin = new DefaultTextField(5, null, RIGHT);
        rangeMax = new DefaultTextField(5);
        DefaultLabel separator = new DefaultLabel("...", CENTER);

        equalsRanges = new DefaultCheckBox(labels.get(RANGES_EQUALS));
        equalsRanges.addItemListener(e -> {
            if (equalsRanges.isSelected()) {
                manager.setChannelAndSensorRangesEqual();
            } else setRangePanelEnabled(true);
        });

        measurementValues = new DefaultComboBox(false);

        rangePanel = new TitledPanel(rootLabels.get(RootLabelName.RANGE));
        rangePanel.add(rangeMin, new CellBuilder().x(0).y(0).build());
        rangePanel.add(separator, new CellBuilder().x(1).y(0).build());
        rangePanel.add(rangeMax, new CellBuilder().x(2).y(0).build());
        rangePanel.add(measurementValues, new CellBuilder().x(3).y(0).build());
        rangePanel.add(equalsRanges, new CellBuilder().x(3).y(1).width(4).build());

        errorLabel = new DefaultLabel(labels.get(SENSOR_ERROR_FORMULA), messages.get(RootMessageName.ERROR_FORMULA_TOOLTIP));
        errorFormulas = new DefaultComboBox(true);
        errorFormulas.setToolTipText(messages.get(RootMessageName.ERROR_FORMULA_TOOLTIP));

        this.add(sensorsTypes, new CellBuilder().x(0).y(0).build());
        this.add(serialNumber, new CellBuilder().x(1).y(0).build());
        this.add(rangePanel, new CellBuilder().x(0).y(1).width(2).build());
        this.add(errorLabel, new CellBuilder().x(0).y(2).build());
        this.add(errorFormulas, new CellBuilder().x(1).y(2).build());
    }

    @Override
    public void setSensorsTypes(List<String> sensorsTypes) {
        this.sensorsTypes.setList(sensorsTypes);
    }

    @Override
    public void setMeasurementValues(List<String> values) {
        this.measurementValues.setList(values);
    }

    @Override
    public void setErrorFormulas(List<String> errors) {
        errorFormulas.setList(errors);
    }

    @Override
    public void setSensorType(String type) {
        sensorsTypes.setSelectedItem(type == null ? EMPTY : type);
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValues.setSelectedItem(value);
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
    public void setErrorFormula(String error) {
        errorFormulas.setSelectedItem(error);
    }

    @Override
    public String getSelectedSensorType() {
        return sensorsTypes.getSelectedString();
    }

    @Override
    public String getSelectedMeasurementValue() {
        return measurementValues.getSelectedString();
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
    public String getErrorFormula() {
        return errorFormulas.getSelectedString();
    }

    @Override
    public boolean isRangeValid() {
        String min = getRangeMin();
        String max = getRangeMax();
        if (min.isEmpty() || max.isEmpty()) {
            rangePanel.setTitleColor(Color.RED);
            return false;
        }

        if (Double.parseDouble(min) > Double.parseDouble(max)) {
            rangePanel.setTitleColor(Color.RED);
            return false;
        } else {
            rangePanel.setTitleColor(Color.BLACK);
            return true;
        }
    }

    @Override
    public boolean isErrorFormulaValid() {
        if (MxParserErrorCalculater.isFormulaValid(errorFormulas.getSelectedString())) {
            errorLabel.setForeground(Color.BLACK);
            return true;
        } else {
            errorLabel.setForeground(Color.RED);
            return false;
        }
    }

    @Override
    public boolean isEqualsRangesCheckboxAreSelected() {
        return equalsRanges.isSelected();
    }
}
