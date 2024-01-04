package service.certificate.converter.kt200k.writer;

import java.io.File;
import java.util.Map;
import service.certificate.converter.kt200k.model.Certificate;

/**
 * @author Ihor Vasyliev
 * @since 2024/01/04
 */
public interface CertificateWriter {
	void write(Map<Certificate, File> certificateFileMap);
}
