package service.certificate.converter.kt200k.writer;

import service.certificate.converter.kt200k.model.Certificate;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynchExelCertificateWriter implements CertificateWriter {
    private final int threadPoolSize;

    public AsynchExelCertificateWriter(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    @Override
    public void write(Map<Certificate, File> certificateFileMap) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        try {
            Set<Callable<Void>> callables = new HashSet<>(certificateFileMap.size());
        for (Map.Entry<Certificate, File> certificateEntry : certificateFileMap.entrySet()) {
            callables.add(writeSingleFile(certificateEntry.getKey(), certificateEntry.getValue()));
        }
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    private Callable<Void> writeSingleFile(Certificate certificate, File file) {
        return () -> null;
    }
}
