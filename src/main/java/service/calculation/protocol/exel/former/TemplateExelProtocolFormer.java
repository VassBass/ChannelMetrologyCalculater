package service.calculation.protocol.exel.former;

import model.OS;
import model.dto.Measurement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import service.calculation.protocol.Protocol;
import service.calculation.protocol.exel.template.ExelTemplateHolder;
import service.calculation.protocol.exel.template.LinuxSuitableExelTemplateHolder;
import service.calculation.protocol.exel.template.WindowsNotSuitableExelTemplateHolder;
import service.calculation.protocol.exel.template.WindowsSuitableExelTemplateHolder;

import java.util.Objects;

public class TemplateExelProtocolFormer implements ExelProtocolFormer {
    private final RepositoryFactory repositoryFactory;
    private final OS os;
    private ExelTemplateHolder templateHolder;

    public TemplateExelProtocolFormer(RepositoryFactory repositoryFactory, OS os) {
        this.repositoryFactory = repositoryFactory;
        this.os = Objects.isNull(os) ? OS.getName() : os;
    }

    @Override
    public HSSFWorkbook format(Protocol protocol) {
        HSSFWorkbook book;
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();
        String measurementName = protocol.getChannel().getMeasurementName();

        if (Objects.isNull(templateHolder)) templateHolder = createTemplateHolder(os, suitable);
        book = templateHolder.getTemplateFor(measurementName);

        ExelProtocolFormer former = null;
        if (measurementName.equals(Measurement.TEMPERATURE)) {
            former = new TemplateExelTemperatureProtocolFormer(book, repositoryFactory);
        } else if (measurementName.equals(Measurement.PRESSURE)) {
            former = new TemplateExelPressureProtocolFormer(book, repositoryFactory);
        } else if (measurementName.equals(Measurement.CONSUMPTION)) {
            former = new TemplateExelConsumptionProtocolFormer(book, repositoryFactory);
        }

        return Objects.isNull(former) ?
                null :
                former.format(protocol);
    }

    private ExelTemplateHolder createTemplateHolder(OS os, boolean suitable) {
        switch (os) {
            case WINDOWS:
                return suitable ?
                        new WindowsSuitableExelTemplateHolder() :
                        new WindowsNotSuitableExelTemplateHolder();
            default:
                return suitable ?
                        new LinuxSuitableExelTemplateHolder() :
                        new WindowsNotSuitableExelTemplateHolder();
        }
    }
}
