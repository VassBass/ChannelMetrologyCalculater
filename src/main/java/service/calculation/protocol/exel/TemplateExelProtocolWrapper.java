package service.calculation.protocol.exel;

import model.OS;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import service.calculation.CalculationConfigHolder;
import service.calculation.protocol.Protocol;
import service.calculation.protocol.exel.former.ExelProtocolFormer;
import service.calculation.protocol.exel.former.TemplateExelProtocolFormer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TemplateExelProtocolWrapper implements ExelProtocolWrapper {
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final OS os;

    public TemplateExelProtocolWrapper(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull CalculationConfigHolder configHolder,
                                       @Nullable OS os) {
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.os = os;
    }

    @Override
    public TemplateExelProtocol wrap(Protocol protocol) {
        ExelProtocolFormer protocolFormer = new TemplateExelProtocolFormer(repositoryFactory, os);
        HSSFWorkbook book = protocolFormer.format(protocol);
        String folderPath = configHolder.getProtocolsFolder();
        String fileName = String.format("№%s(%s).xls", protocol.getNumber(), protocol.getDate());
        return new TemplateExelProtocol(book, folderPath, fileName);
    }
}
