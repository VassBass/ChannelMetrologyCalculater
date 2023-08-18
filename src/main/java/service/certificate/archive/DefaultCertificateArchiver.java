package service.certificate.archive;

import util.StringHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class DefaultCertificateArchiver implements CertificateArchiver {
    private static final String YEAR_REGEX = "(?<=\\d\\(\\d{2}\\.\\d{2}\\.)\\d{4}(?=\\))";
    private static final String MONTH_REGEX = "(?<=\\d\\(\\d{2}\\.)\\d{2}(?=\\.\\d{4}\\))";

    private static final String ARCHIVE_FOLDER_NAME = "archive";

    private final File certificateFolder;

    public DefaultCertificateArchiver(CertificateArchiveConfigHolder configHolder) {
        String certificateFolderName = configHolder.getCertificatesFolderName();
        certificateFolder = new File(certificateFolderName);
    }

    @Override
    public void archive() throws IOException {
        if (certificateFolder.exists() && certificateFolder.isDirectory()) {
            File[] content = certificateFolder.listFiles();
            if (Objects.nonNull(content)) {
                List<File> files = Arrays.stream(content)
                        .filter(File::isFile)
                        .collect(Collectors.toList());
                replaceArchivedFiles(files);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void replaceArchivedFiles(List<File> allFiles) throws IOException {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (File file : allFiles) {
            String fileName = file.getName();
            Matcher yearMatcher = Pattern.compile(YEAR_REGEX).matcher(fileName);
            if (yearMatcher.find()) {
                Integer year = StringHelper.parseInt(yearMatcher.group());
                if (year != null && year < currentYear) {
                    File archiveFolder = new File(certificateFolder, ARCHIVE_FOLDER_NAME);

                    File holderFolder = null;
                    Matcher monthMatcher = Pattern.compile(MONTH_REGEX).matcher(fileName);
                    if (monthMatcher.find()) {
                        Integer month = StringHelper.parseInt(monthMatcher.group());
                        if (Objects.nonNull(month)) {
                            holderFolder = new File(archiveFolder, String.format("%s%s%s", year, File.separator, month));
                        }
                    }
                    if (holderFolder == null) holderFolder = archiveFolder;

                    holderFolder.mkdirs();
                    if (holderFolder.exists()) {
                        File archiveFile = new File(holderFolder, fileName);
                        Files.copy(file.toPath(), archiveFile.toPath(), REPLACE_EXISTING);
                        file.delete();
                    }
                }
            }
        }
    }
}
