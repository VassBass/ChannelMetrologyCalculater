package service.certificate.converter.kt200k.reader;

import service.certificate.converter.kt200k.model.Certificate;

import java.io.File;

public interface CertificateReader {
    Certificate read(File file);
}
