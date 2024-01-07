package service.certificate.converter.kt200k.writer;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import service.certificate.converter.kt200k.KT200KConfigHolder;
import service.certificate.converter.kt200k.model.Certificate;
import util.ExelHelper;

public class AsynchExelCertificateWriter implements CertificateWriter {
    private final KT200KConfigHolder configHolder;
    private final CertificateFormer certificateFormer;
    private final int threadPoolSize;

    public AsynchExelCertificateWriter(KT200KConfigHolder configHolder,
                                       CertificateFormer certificateFormer,
                                       int threadPoolSize) {
        this.configHolder = configHolder;
        this.certificateFormer = certificateFormer;
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Writes a certificates to exel files.
     * @param certificateFileMap - Key = File name, Value = certificate for write
     */
    @Override
    public void write(Map<String, Certificate> certificateFileMap) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        try {
            Set<Callable<Void>> callables = new HashSet<>(certificateFileMap.size());
        for (Map.Entry<String, Certificate> certificateEntry : certificateFileMap.entrySet()) {
            callables.add(writeSingleFile(certificateEntry.getKey(), certificateEntry.getValue()));
        }
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    private Callable<Void> writeSingleFile(String fileName, Certificate certificate) {
        return () -> {
            String folderPath = new File(configHolder.getSaveFolderName()).getAbsolutePath();
            ExelHelper.save(certificateFormer.format(certificate), folderPath, fileName);
            return null;
        };
    }
}
