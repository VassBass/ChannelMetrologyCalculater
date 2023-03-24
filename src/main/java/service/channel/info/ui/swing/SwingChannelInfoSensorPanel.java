package service.channel.info.ui.swing;

import model.ui.*;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoSensorPanel;
import service.error_calculater.ErrorCalculater;
import util.StringHelper;

import java.awt.*;
import java.util.List;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoSensorPanel extends TitledPanel implements ChannelInfoSensorPanel {
    private static final String TITLE_TEXT = "ПВП";
    private static final String SERIAL_NUMBER_TITLE_TEXT = "SN.";
    private static final String TOOLTIP_TEXT = "Первинний вимірювальний пристрій";
    private static final String RANGE_TITLE_TEXT = "Діапазон";
    private static final String CHECK_BOX_TEXT = "Однакові діапазони";
    private static final String ERROR_FORMULA_TEXT = "Формула розрахунку похибки вимірювання ПВП";
    private static final String ERROR_FORMULA_TOOLTIP_TEXT = "<html>"
            + "Щоб написати формулу користуйтеся:"
            + "<br>0...9, 0.1, 0,1 - Натуральні та дробні числа"
            + "<br>() - Дужки, для розстановки послідовності дій"
            + "<br>+, -, *, / - сума, різниця, множення, ділення"
            + "<br>R - Діапазон вимірювання вимірювального каналу"
            + "<br>convR - Діапазон вимірювання ПВП переконвертований під вимірювальну величину вимірювального каналу"
            + "<br>r - Діапазон вимірювання ПВП"
            + "<br>conv(...) - Число переконвертоване з вимірювальної величини ПВП до вимірювальної величини каналу"
            + "<br>-----------------------------------------------------------------------------------"
            + "<br>Приклад: ((0.005 * R) / r) + convR - conv(10)"
            + "<br>Дія №1 - 0.005 помножено на діапазон вимірювання вимірювального каналу(R)"
            + "<br>Дія №2 - Результат першої дії поділено на діапазон вимірювання ПВП(r)"
            + "<br>Дія №3 - До результату другої дії додати діапазон вимірювання ПВП переконвертований під вимірювальну"
            + "<br>величину вимірювального каналу(convR)"
            + "<br>Дія №4 - Від результату третьої дії відняти число 10 переконвертоване з вимірювальної величини ПВП"
            + "<br>до вимірювальної величини вимірювального каналу (conv(10))"
            + "</html>";

    private final DefaultComboBox sensorsTypes;
    private final TitledTextField serialNumber;
    private final TitledPanel rangePanel;
    private final DefaultTextField rangeMin, rangeMax;
    private final DefaultComboBox measurementValues;
    private final DefaultCheckBox equalsRanges;
    private final DefaultLabel errorLabel;
    private final DefaultComboBox errorFormulas;

    public SwingChannelInfoSensorPanel(final ChannelInfoManager manager) {
        super(TITLE_TEXT, Color.BLACK);
        this.setToolTipText(TOOLTIP_TEXT);

        sensorsTypes = new DefaultComboBox(true);
        serialNumber = new TitledTextField(15, SERIAL_NUMBER_TITLE_TEXT);
        rangeMin = new DefaultTextField(5, null, RIGHT);
        rangeMax = new DefaultTextField(5);
        DefaultLabel separator = new DefaultLabel("...", CENTER);

        equalsRanges = new DefaultCheckBox(CHECK_BOX_TEXT);
        equalsRanges.addItemListener(e -> {
            if (equalsRanges.isSelected()) {
                manager.setChannelAndSensorRangesEqual();
            } else setRangePanelEnabled(true);
        });

        measurementValues = new DefaultComboBox(false);

        rangePanel = new TitledPanel(RANGE_TITLE_TEXT);
        rangePanel.add(rangeMin, new CellBuilder().x(0).y(0).build());
        rangePanel.add(separator, new CellBuilder().x(1).y(0).build());
        rangePanel.add(rangeMax, new CellBuilder().x(2).y(0).build());
        rangePanel.add(measurementValues, new CellBuilder().x(3).y(0).build());
        rangePanel.add(equalsRanges, new CellBuilder().x(3).y(1).width(4).build());

        errorLabel = new DefaultLabel(ERROR_FORMULA_TEXT, ERROR_FORMULA_TOOLTIP_TEXT);
        errorFormulas = new DefaultComboBox(true);
        errorFormulas.setToolTipText(ERROR_FORMULA_TOOLTIP_TEXT);

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
        sensorsTypes.setSelectedItem(type);
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
        return sensorsTypes.getSelectedItem();
    }

    @Override
    public String getSelectedMeasurementValue() {
        return measurementValues.getSelectedItem();
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
        return errorFormulas.getSelectedItem();
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
        if (ErrorCalculater.isFormulaValid(errorFormulas.getSelectedItem())) {
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
