package service.certificate.converter.kt200k.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;
import service.certificate.converter.kt200k.model.Certificate;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlCertificateReader extends DefaultHandler implements CertificateReader {
    private static final Logger logger = LoggerFactory.getLogger(XmlCertificateReader.class);

    private static final String ENVIRONMENT_TEMPERATURE_MARK = "Температура окружающей среды";

    private static final String STRING_DATA_REGEX = "(?<=String\"\\>)(.*)(?=\\<\\/Data\\>)";

    private static final String DATA = "Data";
    private static final String ROW = "Row";
    private static final String CELL = "Cell";
    private static final String TABLE = "Table";
    private static final String WORKSHEET = "Worksheet";

    private boolean isCell;
    private boolean isData;

    private boolean isEnvironmentTemperatureMark;

    private StringBuilder elementValue;

    private final Certificate certificate;

    public XmlCertificateReader() {
        certificate = new Certificate();
    }

    @Override
    public Certificate read(File file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

        }catch (IOException e) {
            logger.warn();
        }

        return certificate;
    }


    private boolean isEnvironmentTemperatureMark(String row) {
        Matcher matcher = Pattern.compile(STRING_DATA_REGEX).matcher(row);
        return ENVIRONMENT_TEMPERATURE_MARK.equalsIgnoreCase(matcher.group());
    }
}
