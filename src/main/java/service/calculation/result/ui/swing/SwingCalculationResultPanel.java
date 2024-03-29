package service.calculation.result.ui.swing;

import localization.Labels;
import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.protocol.Protocol;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationResultPanel extends DefaultPanel {
    private static final String EXTENDED_INDETERMINACY = "extendedIndeterminacy";
    private static final String ABSOLUTE_ERROR = "absoluteError";
    private static final String RELATIVE_ERROR = "relativeError";
    private static final String SYSTEMATIC_ERRORS = "systematicErrors";

    public SwingCalculationResultPanel(@Nonnull Protocol protocol) {
        super();
        Map<String, String> labels = Labels.getLabels(SwingCalculationResultPanel.class);

        String measurementValue = protocol.getChannel().getMeasurementValue();
        int valueDecimalPoint = protocol.getValuesDecimalPoint();
        int percentDecimalPoint = protocol.getPercentsDecimalPoint();

        ButtonCell extendedIndeterminacyLabel = new ButtonCell(HEADER, labels.get(EXTENDED_INDETERMINACY));
        String extendedIndeterminacy = StringHelper.roundingDouble(protocol.getExtendedIndeterminacy(), valueDecimalPoint);
        ButtonCell extendedIndeterminacyValue = new ButtonCell(SIMPLE, String.format("%s%s", extendedIndeterminacy, measurementValue));

        ButtonCell absoluteErrorLabel = new ButtonCell(HEADER, labels.get(ABSOLUTE_ERROR));
        String absoluteError = StringHelper.roundingDouble(protocol.getAbsoluteError(), valueDecimalPoint);
        ButtonCell absoluteErrorValue = new ButtonCell(SIMPLE, String.format("%s%s", absoluteError, measurementValue));

        ButtonCell relativeErrorLabel = new ButtonCell(HEADER, labels.get(RELATIVE_ERROR));
        String relativeError = StringHelper.roundingDouble(protocol.getRelativeError(), percentDecimalPoint);
        ButtonCell relativeErrorValue = new ButtonCell(SIMPLE, String.format("%s%%", relativeError));

        ButtonCell systematicErrorsLabel = new ButtonCell(HEADER, labels.get(SYSTEMATIC_ERRORS));
        List<ButtonCell> systematicErrors = createSystematicErrorsTable(protocol.getInput(), protocol.getSystematicErrors(),
                percentDecimalPoint, valueDecimalPoint, measurementValue);

        this.add(extendedIndeterminacyLabel, new CellBuilder().x(0).y(0).build());
        this.add(extendedIndeterminacyValue, new CellBuilder().x(1).y(0).build());
        this.add(relativeErrorLabel, new CellBuilder().x(0).y(1).build());
        this.add(relativeErrorValue, new CellBuilder().x(1).y(1).build());
        this.add(absoluteErrorLabel, new CellBuilder().x(0).y(2).build());
        this.add(absoluteErrorValue, new CellBuilder().x(1).y(2).build());
        this.add(systematicErrorsLabel, new CellBuilder().x(0).y(3).height(systematicErrors.size()).build());

        int y = 3;
        for (ButtonCell systematicError : systematicErrors) {
            this.add(systematicError, new CellBuilder().x(1).y(y++).height(1).build());
        }
    }

    private List<ButtonCell> createSystematicErrorsTable(Map<Double, Double> input,
                                                         Map<Double, Double> systematicErrors,
                                                         int percentDecimalPoint,
                                                         int valueDecimalPoint,
                                                         String measurementValue) {
        List<ButtonCell> result = new ArrayList<>();
        TreeMap<Double, Double> sortedInput = new TreeMap<>(input);
        for (Map.Entry<Double, Double> entry : sortedInput.entrySet()) {
            String percentValue = StringHelper.roundingDouble(entry.getKey(), percentDecimalPoint);
            String value = StringHelper.roundingDouble(entry.getValue(), valueDecimalPoint);
            String error = StringHelper.roundingDouble(systematicErrors.get(Double.parseDouble(value)), valueDecimalPoint);
            String text = String.format("S%s%% = %s%s", percentValue, error, measurementValue);
            result.add(new ButtonCell(SIMPLE, text));
        }
        return result;
    }
}
