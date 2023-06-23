package service.method_name;

import repository.RepositoryFactory;
import repository.repos.calculation_method.CalculationMethodRepository;
import service.method_name.ui.MeasurementNamePanel;
import service.method_name.ui.MethodNameContext;
import service.method_name.ui.MethodNamePanel;
import service.method_name.ui.swing.SwingMethodNameDialog;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingMethodNameManager implements MethodNameManager {

    private final  RepositoryFactory repositoryFactory;
    private final MethodNameContext context;
    private SwingMethodNameDialog dialog;

    public SwingMethodNameManager(@Nonnull RepositoryFactory repositoryFactory,
                                  @Nonnull MethodNameContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    public void registerDialog(@Nonnull SwingMethodNameDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickSave() {
        CalculationMethodRepository repository = repositoryFactory.getImplementation(CalculationMethodRepository.class);
        MeasurementNamePanel measurementNamePanel = context.getElement(MeasurementNamePanel.class);
        MethodNamePanel methodNamePanel = context.getElement(MethodNamePanel.class);

        String measurementName = measurementNamePanel.getMeasurementName();
        String methodName = methodNamePanel.getMethodName();
        repository.set(measurementName, methodName);

        dialog.shutdown();
    }

    @Override
    public void changeMeasurementName() {
        CalculationMethodRepository repository = repositoryFactory.getImplementation(CalculationMethodRepository.class);
        MeasurementNamePanel measurementNamePanel = context.getElement(MeasurementNamePanel.class);

        String measurementName = measurementNamePanel.getMeasurementName();
        String methodName = repository.getMethodNameByMeasurementName(measurementName);

        if (Objects.nonNull(methodName)) {
            MethodNamePanel methodNamePanel = context.getElement(MethodNamePanel.class);
            methodNamePanel.setMethodName(methodName);
        }
    }
}
