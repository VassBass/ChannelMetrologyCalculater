package service.certificate.converter.kt200k.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import service.certificate.converter.kt200k.model.Certificate;

/**
 * @author Ihor Vasyliev
 * @since 2024/01/07
 */
public interface CertificateFormer {
	HSSFWorkbook format(Certificate certificate);
}
