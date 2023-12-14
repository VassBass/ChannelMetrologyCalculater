package service.certificate.converter.kt200k.reader;

import localization.Messages;
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
    private static final String ENVIRONMENT_PRESSURE_MARK = "Атмосферное давление";
    private static final String ENVIRONMENT_HUMIDITY_MARK = "Относительная влажность";

    private static final String STRING_DATA_REGEX = "(?<=String\"\\>)(.*)(?=\\<\\/Data\\>)";

    private final Certificate certificate;

    public XmlCertificateReader() {
        certificate = new Certificate();
    }

    @Override
    public Certificate read(File file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String row;
            while ((row = reader.readLine()) != null) {
                if (certificate.getEnvironmentTemperature() == null && isEnvironmentTemperatureMark(row)) {
                    certificate.setEnvironmentTemperature(extractDataValue(reader.readLine()));
                }

                if (certificate.getEnvironmentPressure() == null && isEnvironmentPressureMark(row)) {
                    certificate.setEnvironmentPressure(extractDataValue(reader.readLine()));
                }

                if (certificate.getEnvironmentHumidity() == null && isEnvironmentHumidityMark(row)) {
                    certificate.setEnvironmentHumidity(extractDataValue(reader.readLine()));
                }
            }
        }catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }

        return certificate;
    }


    private boolean isEnvironmentTemperatureMark(String row) {
        String data = extractDataValue(row);
        return data != null && data.contains(ENVIRONMENT_TEMPERATURE_MARK);
    }

    private boolean isEnvironmentPressureMark(String row) {
        String data = extractDataValue(row);
        return data != null && data.contains(ENVIRONMENT_PRESSURE_MARK);
    }

    private boolean isEnvironmentHumidityMark(String row) {
        String data = extractDataValue(row);
        return data != null && data.contains(ENVIRONMENT_HUMIDITY_MARK);
    }

    private String extractDataValue(String row) {
        if (row == null) return null;

        Matcher matcher = Pattern.compile(STRING_DATA_REGEX).matcher(row);
        return matcher.find() ? matcher.group() : null;
    }
}
