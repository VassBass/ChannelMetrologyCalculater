package service.certificate.archive;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceExecutor;

import java.io.IOException;

public class CertificateArchiveExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CertificateArchiveExecutor.class);

    @Override
    public void execute() {
        logger.info("Start archiving certificates...");
        CertificateArchiveConfigHolder configHolder = new PropertiesCertificateArchiveConfigHolder();
        CertificateArchiver archiver = new DefaultCertificateArchiver(configHolder);
        try {
            archiver.archive();
            logger.info("Archiving was successful");
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }
}
