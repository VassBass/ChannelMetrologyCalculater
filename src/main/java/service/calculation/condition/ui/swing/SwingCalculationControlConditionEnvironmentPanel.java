package service.calculation.condition.ui.swing;

import model.dto.Measurement;
import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.IntegerTextField;
import model.ui.builder.CellBuilder;
import service.calculation.condition.CalculationControlConditionValuesBuffer;
import service.calculation.condition.ui.CalculationControlConditionEnvironmentPanel;

import static javax.swing.SwingConstants.CENTER;
import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationControlConditionEnvironmentPanel extends DefaultPanel implements CalculationControlConditionEnvironmentPanel {
    private static final String TEMPERATURE_MEASUREMENT_VALUE = Measurement.DEGREE_CELSIUS;
    private static final String HUMIDITY_MEASUREMENT_VALUE = Measurement.PERCENT;
    private static final String PRESSURE_MEASUREMENT_VALUE = Measurement.MM_ACVA;

    private final IntegerTextField temperatureValue;
    private final IntegerTextField humidityValue;
    private final IntegerTextField pressureValue;

    public SwingCalculationControlConditionEnvironmentPanel() {
        super();
        Labels labels = Labels.getInstance();
        CalculationControlConditionValuesBuffer buffer = CalculationControlConditionValuesBuffer.getInstance();

        ButtonCell header = new ButtonCell(HEADER, labels.controlConditions);
        ButtonCell temperatureLabel = new ButtonCell(SIMPLE, labels.environmentTemperature);
        ButtonCell humidityLabel = new ButtonCell(SIMPLE, labels.airHumidity);
        ButtonCell pressureLabel = new ButtonCell(SIMPLE, labels.atmospherePressure);

        ButtonCell temperatureMeasurementValue = new ButtonCell(SIMPLE, TEMPERATURE_MEASUREMENT_VALUE);
        ButtonCell humidityMeasurementValue = new ButtonCell(SIMPLE, HUMIDITY_MEASUREMENT_VALUE);
        ButtonCell pressureMeasurementValue = new ButtonCell(SIMPLE, PRESSURE_MEASUREMENT_VALUE);

        temperatureValue = new IntegerTextField(2, buffer.getTemperature(), CENTER);
        humidityValue = new IntegerTextField(2, buffer.getHumidity(), CENTER);
        pressureValue = new IntegerTextField(3, buffer.getPressure(), CENTER);

        this.add(header, new CellBuilder().x(0).y(0).width(3).build());
        this.add(temperatureLabel, new CellBuilder().x(0).y(1).width(1).build());
        this.add(temperatureValue, new CellBuilder().x(1).y(1).width(1).build());
        this.add(temperatureMeasurementValue, new CellBuilder().x(2).y(1).width(1).build());
        this.add(humidityLabel, new CellBuilder().x(0).y(2).width(1).build());
        this.add(humidityValue, new CellBuilder().x(1).y(2).width(1).build());
        this.add(humidityMeasurementValue, new CellBuilder().x(2).y(2).width(1).build());
        this.add(pressureLabel, new CellBuilder().x(0).y(3).width(1).build());
        this.add(pressureValue, new CellBuilder().x(1).y(3).width(1).build());
        this.add(pressureMeasurementValue, new CellBuilder().x(2).y(3).width(1).build());
    }

    @Override
    public int getTemperature() {
        return Integer.parseInt(temperatureValue.getText());
    }

    @Override
    public int getPressure() {
        return Integer.parseInt(pressureValue.getText());
    }

    @Override
    public int getHumidity() {
        return Integer.parseInt(humidityValue.getText());
    }
}
