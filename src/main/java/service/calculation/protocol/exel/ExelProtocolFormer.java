package service.calculation.protocol.exel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import service.calculation.protocol.Protocol;

public interface ExelProtocolFormer {
    HSSFWorkbook format(Protocol protocol);
}
