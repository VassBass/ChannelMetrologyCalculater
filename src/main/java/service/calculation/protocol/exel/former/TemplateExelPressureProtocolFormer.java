package service.calculation.protocol.exel.former;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;

import javax.annotation.Nonnull;

public class TemplateExelPressureProtocolFormer extends TemplateExelTemperatureProtocolFormer {

    public TemplateExelPressureProtocolFormer(@Nonnull HSSFWorkbook book,
                                               @Nonnull RepositoryFactory repositoryFactory) {
        super(book, repositoryFactory);
    }
}
