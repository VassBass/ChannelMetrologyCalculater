package service.certificate.converter.kt200k.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Ihor Vasyliev
 * @since 2024/01/07
 */
public interface TemplateHolder {
	HSSFWorkbook getTemplate();
}
