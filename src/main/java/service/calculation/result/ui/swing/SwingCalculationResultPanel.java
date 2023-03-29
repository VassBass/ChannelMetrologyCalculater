package service.calculation.result.ui.swing;

import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.dto.Protocol;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationResultPanel extends DefaultPanel {
    private static final String EXTENDED_INDETERMINACY_LABEL_TEXT = "Розширена невизначеність";
    private static final String ABSOLUTE_ERROR_LABEL_TEXT = "Абсолютна похибка";
    private static final String RELATIVE_ERROR_LABEL_TEXT = "Відносна похибка";
    private static final String SYSTEMATIC_ERRORS_LABEL_TEXT = "Систематичні похибки";

    public SwingCalculationResultPanel(@Nonnull Protocol protocol) {
        super();
        String measurementValue = protocol.getChannel().getMeasurementValue();
        int decimalPoint = protocol.getDecimalPoint();

        ButtonCell extendedIndeterminacyLabel = new ButtonCell(HEADER, EXTENDED_INDETERMINACY_LABEL_TEXT);
        String extendedIndeterminacy = StringHelper.roundingDouble(protocol.getExtendedIndeterminacy(), decimalPoint);
        ButtonCell extendedIndeterminacyValue = new ButtonCell(SIMPLE, String.format("%s%s", extendedIndeterminacy, measurementValue));

        ButtonCell absoluteErrorLabel = new ButtonCell(HEADER, ABSOLUTE_ERROR_LABEL_TEXT);
        String absoluteError = StringHelper.roundingDouble(protocol.getAbsoluteError(), decimalPoint);
        ButtonCell absoluteErrorValue = new ButtonCell(SIMPLE, String.format("%s%s", absoluteError, measurementValue));

        ButtonCell relativeErrorLabel = new ButtonCell(HEADER, RELATIVE_ERROR_LABEL_TEXT);
        String relativeError = StringHelper.roundingDouble(protocol.getRelativeError(), decimalPoint);
        ButtonCell relativeErrorValue = new ButtonCell(SIMPLE, String.format("%s%%", relativeError));

        ButtonCell systematicErrorsLabel = new ButtonCell(HEADER, SYSTEMATIC_ERRORS_LABEL_TEXT);
        List<ButtonCell> systematicErrors = createSystematicErrorsTable(protocol.getInput(), protocol.getSystematicErrors(),
                decimalPoint, measurementValue);

        this.add(extendedIndeterminacyLabel, new CellBuilder().x(0).y(0).build());
        this.add(extendedIndeterminacyValue, new CellBuilder().x(1).y(0).build());
        this.add(absoluteErrorLabel, new CellBuilder().x(0).y(1).build());
        this.add(absoluteErrorValue, new CellBuilder().x(1).y(1).build());
        this.add(relativeErrorLabel, new CellBuilder().x(0).y(2).build());
        this.add(relativeErrorValue, new CellBuilder().x(1).y(2).build());
        this.add(systematicErrorsLabel, new CellBuilder().x(0).y(3).height(systematicErrors.size()).build());

        int y = 3;
        for (ButtonCell systematicError : systematicErrors) {
            this.add(systematicError, new CellBuilder().x(1).y(y++).height(1).build());
        }
    }

    private List<ButtonCell> createSystematicErrorsTable(Map<Double, Double> input,
                                                         Map<Double, Double> systematicErrors,
                                                         int decimalPoint,
                                                         String measurementValue) {
        List<ButtonCell> result = new ArrayList<>();
        TreeMap<Double, Double> sortedInput = new TreeMap<>(input);
        for (Map.Entry<Double, Double> entry : sortedInput.entrySet()) {
            String percentValue = StringHelper.roundingDouble(entry.getKey(), decimalPoint);
            String value = StringHelper.roundingDouble(entry.getValue(), decimalPoint);
            String error = StringHelper.roundingDouble(systematicErrors.get(Double.parseDouble(value)), decimalPoint);
            String text = String.format("S%s%% = %s%s", percentValue, error, measurementValue);
            result.add(new ButtonCell(SIMPLE, text));
        }
        return result;
    }
}
