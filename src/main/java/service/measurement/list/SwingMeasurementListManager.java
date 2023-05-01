package service.measurement.list;

import model.dto.MeasurementTransformFactor;
import repository.RepositoryFactory;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import service.measurement.list.ui.MeasurementListContext;
import service.measurement.list.ui.MeasurementListFactorTable;
import service.measurement.list.ui.MeasurementListValueTable;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingMeasurementListManager implements MeasurementListManager {

    private final RepositoryFactory repositoryFactory;
    private final MeasurementListContext context;
    private SwingMeasurementListDialog dialog;

    public SwingMeasurementListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull MeasurementListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    public void registerDialog(SwingMeasurementListDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changeMeasurementName() {
        dialog.refresh();
    }

    @Override
    public void selectMeasurementValue() {
        MeasurementListValueTable valueTable = context.getElement(MeasurementListValueTable.class);

        String value = valueTable.getSelectedValue();
        if (Objects.nonNull(value)) {
            MeasurementListFactorTable factorTable = context.getElement(MeasurementListFactorTable.class);
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

            Map<String, Double> valueList = factorRepository.getBySource(value).stream()
                    .collect(Collectors.toMap(MeasurementTransformFactor::getTransformTo, MeasurementTransformFactor::getTransformFactor));
            factorTable.setFactorList(value, valueList);
        }
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickChange() {

    }

    @Override
    public void clickAdd() {

    }
}
