package service.measurement.converter;

import localization.Labels;
import localization.RootLabelName;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.converter.service.Converter;
import service.measurement.converter.ui.ConverterContext;
import service.measurement.converter.ui.MeasurementNamePanel;
import service.measurement.converter.ui.ResultPanel;
import service.measurement.converter.ui.swing.ConverterDialog;
import service.measurement.converter.ui.swing.SwingResultMeasurementValuePanel;
import service.measurement.converter.ui.swing.SwingSourceMeasurementValuePanel;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SwingConverterManager implements ConverterManager {
    private final RepositoryFactory repositoryFactory;
    private final ConverterContext context;
    private final Converter converter;
    private ConverterDialog dialog;

    public SwingConverterManager(@Nonnull RepositoryFactory repositoryFactory,
                                 @Nonnull ConverterContext context,
                                 @Nonnull Converter converter) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
        this.converter = converter;
    }

    public void registerDialog(@Nonnull ConverterDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void shutdownService() {
        dialog.shutdown();
    }

    @Override
    public void convert() {
        Map<String, String> labels = Labels.getRootLabels();

        SwingSourceMeasurementValuePanel sourceValuePanel = context.getElement(SwingSourceMeasurementValuePanel.class);
        SwingResultMeasurementValuePanel resultValuePanel = context.getElement(SwingResultMeasurementValuePanel.class);
        ResultPanel resultPanel = context.getElement(ResultPanel.class);

        String sourceMeasurementValue = sourceValuePanel.getMeasurementValue();
        String resultMeasurementValue = resultValuePanel.getMeasurementValue();
        double sourceValue = sourceValuePanel.getValue();

        if (Double.isNaN(sourceValue)) {
            JOptionPane.showMessageDialog(dialog, labels.get(RootLabelName.INPUT_NOT_VALID), labels.get(RootLabelName.INPUT_NOT_VALID), JOptionPane.ERROR_MESSAGE);
        } else {
            double resultValue = converter.convert(sourceMeasurementValue, resultMeasurementValue, sourceValue);
            resultPanel.appendResult(sourceMeasurementValue, sourceValue, resultMeasurementValue, resultValue);
            dialog.refresh();
        }
    }

    @Override
    public void updateMeasurementValues() {
        MeasurementRepository repository = repositoryFactory.getImplementation(MeasurementRepository.class);

        MeasurementNamePanel namePanel = context.getElement(MeasurementNamePanel.class);
        SwingSourceMeasurementValuePanel sourcePanel = context.getElement(SwingSourceMeasurementValuePanel.class);
        SwingResultMeasurementValuePanel resultPanel = context.getElement(SwingResultMeasurementValuePanel.class);

        String name = namePanel.getMeasurementName();
        List<String> values = Arrays.asList(repository.getValues(name));

        sourcePanel.setMeasurementValuesList(values);
        resultPanel.setMeasurementValuesList(values);
        dialog.refresh();
    }
}
