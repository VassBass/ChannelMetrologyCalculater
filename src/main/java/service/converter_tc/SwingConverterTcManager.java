package service.converter_tc;

import model.dto.Measurement;
import service.converter_tc.converter.Converter;
import service.converter_tc.model.Type;
import service.converter_tc.ui.ConverterTcContext;
import service.converter_tc.ui.ResultPanel;
import service.converter_tc.ui.TypePanel;
import service.converter_tc.ui.ValuePanel;
import service.converter_tc.ui.swing.SwingConverterTcDialog;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static util.DoubleHelper.nonNaN;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingConverterTcManager implements ConverterTcManager {

    private SwingConverterTcDialog dialog;
    private final ConverterTcContext context;
    private final Converter converter;

    public SwingConverterTcManager(@Nonnull ConverterTcContext context, @Nonnull Converter converter) {
        this.context = context;
        this.converter = converter;
    }

    public void registerDialog(SwingConverterTcDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changingInputMeasurementValue() {

    }

    @Override
    public void calculate() {
        TypePanel typePanel = context.getElement(TypePanel.class);
        ValuePanel valuePanel = context.getElement(ValuePanel.class);
        ResultPanel resultPanel = context.getElement(ResultPanel.class);

        Map.Entry<Type, Double> type = typePanel.getType();
        if (nonNull(type)) {
            double numValue = valuePanel.getNumericValue();
            if (nonNaN(numValue)) {
                String input = valuePanel.getMeasurementValue();
                String result;
                String output;
                if (input.equals(Measurement.Om)) {
                    result = StringHelper.roundingDouble(converter.transformFromResistance(type.getKey(), type.getValue(), numValue), FOR_LAST_ZERO);
                    output = Measurement.DEGREE_CELSIUS;
                } else {
                    result = StringHelper.roundingDouble(converter.transformFromTemperature(type.getKey(), type.getValue(), numValue), FOR_LAST_ZERO);
                    output = Measurement.Om;
                }
                String old = resultPanel.getResult();
                String prefix = old.isEmpty() ? EMPTY : String.format("%s\n", old);
                resultPanel.setResult(String.format("%s%s %s = %s %s", prefix, numValue, input, result, output));
            }
        }
        dialog.refresh();
    }

    @Override
    public void close() {
        dialog.shutdown();
    }
}
