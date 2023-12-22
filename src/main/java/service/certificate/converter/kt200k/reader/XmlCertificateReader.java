package service.certificate.converter.kt200k.reader;

import localization.Labels;
import localization.Messages;
import model.dto.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.certificate.converter.kt200k.KT200KTranslate;
import service.certificate.converter.kt200k.model.*;
import util.RegexHelper;
import util.StringHelper;
import util.Symbol;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class XmlCertificateReader extends DefaultHandler implements CertificateReader {
    private static final Logger logger = LoggerFactory.getLogger(XmlCertificateReader.class);

    private static final String ROWS = "Rows";
    private static final String ROW = "Row";
    private static final String CELL = "Cell";

    private static final String ENVIRONMENT_MARK = "УСЛОВИЯ ПРОВЕДЕНИЯ ПОВЕРКИ<";
    private static final String CHECK_MARK = "ВНЕШНИЙ ОСМОТР И ОПРОБОВАНИЕ";
    private static final String EL_STRONG_MARK = "ОПРЕДЕЛЕНИЕ ЭЛЕКТРИЧЕСКОЙ ПРОЧНОСТИ";
    private static final String RESULTS_MARK = "РЕЗУЛЬТАТЫ ИЗМЕРЕНИЙ";
    private static final String RESULTS_VALS_MARK = "ЗАКЛЮЧЕНИЕ ПО РЕЗУЛЬТАТАМ ПОВЕРКИ";
    private static final String DATE_MARK = "Дата:";

    private static final String RESISTANCE_MARK = "R";
    private static final String TEMPERATURE_MARK = "T";
    private static final String DELTA_MARK = Symbol.DELTA + Labels.COMMA;
    private static final String VOLTAGE_MARK = "U";
    private static final String ADDITIONAL_DELTA_MARK = Symbol.DELTA + "доп";

    private static final String NUMBER_MARK = "Номер";
    private static final String CLASS_MARK = "Класс допуска";

    private static final String RESULT_MARK = "Заключение о пригодности ТС";

    private static final String WORKER_MARK = "Поверитель";

    private static final String START_ROW = "<Row";
    private static final String START_CELL = "<Cell";

    private static final String STRING_DATA_REGEX = "(?<=String\"\\>)(.*)(?=\\<\\/Data\\>)";
    private static final String WORKER_NAME_REGEX = "(?<=\\()(.*)(?=\\))";
    private static final String DATE_REGEX = "(?<=:)(.*)";
    private static final String LAST_NAME_REGEX = "\\p{Lu}{2,}";
    private static final String NOT_FIRST_LETTER_REGEX = "(?<=\\W)\\W*";

    private final RepositoryFactory repositoryFactory;

    public XmlCertificateReader(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public Certificate read(File file) {
        Certificate certificate = new Certificate();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            XmlElement rows = createRowsTree(reader);
            for (XmlElement row : rows.getChildren()) {
                String rowData = row.getData();
                if (rowData.contains(ENVIRONMENT_MARK)) {
                    extractEnvironmentValues(row.getNext(), certificate);
                }
                if (rowData.contains(CHECK_MARK)) {
                    createSensors(row.getNext(), certificate);
                }
                if (rowData.contains(RESULTS_MARK)) {
                    appendResults(row.getNext(), certificate);
                }
                if (rowData.contains(RESULTS_VALS_MARK)) {
                    appendRemark(row.getNext(), certificate);
                }
                if (rowData.contains(DATE_MARK)) {
                    appendDateAndWorker(row, certificate);
                }
            }
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }

        return certificate;
    }

    private XmlElement createRowsTree(BufferedReader reader) throws IOException {
        XmlElement rows = new XmlElement(ROWS, null);

        Pattern startRowPattern = Pattern.compile(START_ROW);
        Pattern startCellPattern = Pattern.compile(START_CELL);

        String line;
        while ((line = reader.readLine()) != null) {
            Matcher startRowMatcher = startRowPattern.matcher(line);
            Matcher startCellMatcher = startCellPattern.matcher(line);

            if (startRowMatcher.find()) {
                XmlElement previousRow = rows.getLatsChild();
                XmlElement row = new XmlElement(ROW, rows);
                if (previousRow == null) {
                    row.setPrevious(rows);
                } else {
                    row.setPrevious(previousRow);
                    previousRow.setNext(row);
                }
            } else if (startCellMatcher.find()) {
                XmlElement currentRow = rows.getLatsChild();
                XmlElement previousCell = currentRow.getLatsChild();
                XmlElement cell = new XmlElement(CELL, currentRow);
                if (previousCell == null) {
                    cell.setPrevious(currentRow);
                } else {
                    cell.setPrevious(previousCell);
                    previousCell.setNext(cell);
                }
                cell.setData(extractDataValue(line));
            }
        }

        return rows;
    }

    private void extractEnvironmentValues(XmlElement row, Certificate certificate) {
        certificate.setEnvironmentTemperature(row.getChild(1).getData());
        XmlElement nextRow = row.getNext();
        certificate.setEnvironmentPressure(nextRow.getChild(1).getData());
        nextRow = nextRow.getNext();
        certificate.setEnvironmentHumidity(nextRow.getChild(1).getData());
    }

    private void createSensors(XmlElement row, Certificate certificate) {
        int numberIndex = -1;
        int classIndex = -1;
        List<XmlElement> cells = row.getChildren();
        for (int i = 0; i < cells.size(); i++) {
            String data = cells.get(i).getData();
            if (data.equals(NUMBER_MARK)) numberIndex = i;
            if (data.equals(CLASS_MARK)) classIndex = i;

            if (numberIndex >= 0 && classIndex >= 0) break;
        }
        if (numberIndex >= 0) {
            XmlElement nextRow = row.getNext();
            while (!nextRow.getData().contains(EL_STRONG_MARK)) {
                String sensorNumber = nextRow.getChild(numberIndex).getData();
                if (!sensorNumber.isEmpty()) {
                    Sensor sensor = new Sensor(false);
                    sensor.setNumber(sensorNumber);
                    sensor.setClearanceClass(ClearanceClass.create(nextRow.getChild(classIndex).getData()));
                    certificate.addSensor(sensor);
                }
                nextRow = nextRow.getNext();
            }
        }
    }

    private void appendResults(XmlElement row, Certificate certificate) {
        Sensor benchmark = new Sensor(true);
        certificate.setBenchmarkSensor(benchmark);

        Map<Integer, Sensor> sensorsMap = new HashMap<>(certificate.getSensorsCount() + 1);
        sensorsMap.put(1, benchmark);

        XmlElement nextRow = row.getNext();
        List<XmlElement> cells = nextRow.getChildren();
        for (int i = 0; i < cells.size(); i++) {
            String sensorNumber = cells.get(i).getData().replace(Symbol.NUMBER, EMPTY);
            Sensor sensor = certificate.getSensor(sensorNumber);
            if (sensor != null) {
                sensorsMap.put(i + 2, sensor);
            }
        }

        nextRow = nextRow.getNext();
        String startPointMark = RESISTANCE_MARK;
        while (!nextRow.getData().contains(RESULTS_VALS_MARK)) {
            cells = nextRow.getChildren();

            String outType = cells.get(0).getData();
            for (Integer i : sensorsMap.keySet()) {
                Sensor sensor = sensorsMap.get(i);
                if (outType.contains(startPointMark)) {
                    sensor.addPoint(new Point());
                }
                Point point = sensor.getLastPoint();

                String value = cells.get(i).getData();
                if (StringHelper.nonEmpty(value)) {
                    if (outType.contains(RESISTANCE_MARK)) {
                        point.setResistance(value);
                    } else if (outType.contains(TEMPERATURE_MARK)) {
                        point.setTemperature(value);
                    } else if (outType.contains(DELTA_MARK)) {
                        point.setDelta(value);
                    } else if (outType.contains(VOLTAGE_MARK)) {
                        point.setVoltage(value);
                    } else if (outType.contains(ADDITIONAL_DELTA_MARK)) {
                        point.setAdditionalDelta(value);
                    }
                }
            }

            nextRow = nextRow.getNext();
        }
    }

    private void appendRemark(XmlElement row, Certificate certificate) {
        int numberIndex = -1;
        int remarkIndex = -1;
        for (int i = 0; i < row.getChildren().size(); i++) {
            String data = row.getChild(i).getData();
            if (Symbol.NUMBER.equals(data)) {
                numberIndex = i;
            }
            if (RESULT_MARK.equals(data)) {
                remarkIndex = i;
            }
        }

        XmlElement nextRow = row.getNext();
        while (!nextRow.getData().isEmpty()) {
            String number = nextRow.getChild(numberIndex).getData();
            String remark = nextRow.getChild(remarkIndex).getData();
            String[] splittedRemark = remark.split(RegexHelper.DOT_REGEX);
            StringBuilder builder = new StringBuilder();
            for (String r : splittedRemark) {
                if (StringHelper.isBlank(r)) continue;

                builder.append(KT200KTranslate.translateToLocale(r))
                        .append(Labels.DOT)
                        .append(Labels.SPACE);
            }
            builder.deleteCharAt(builder.length() - 1);
            certificate.getSensor(number).setRemark(builder.toString());

            nextRow = nextRow.getNext();
        }
    }

    private void appendDateAndWorker(XmlElement row, Certificate certificate) {
        for (XmlElement cell : row.getChildren()) {
            String data = cell.getData();
            if (data.contains(DATE_MARK)) {
                Matcher dateMatcher = Pattern.compile(DATE_REGEX).matcher(data);
                if (dateMatcher.find()) {
                    certificate.setDate(dateMatcher.group().replaceAll(RegexHelper.SPACE_REGEX, EMPTY));
                }
            }
            if (data.contains(WORKER_MARK)) {
                Matcher workerMatcher = Pattern.compile(WORKER_NAME_REGEX).matcher(data);
                if (workerMatcher.find()) {
                    Person person = null;
                    String value = workerMatcher.group().replaceAll(RegexHelper.START_SPACES_REGEX, EMPTY);
                    if (!value.isEmpty()) {
                        Matcher lastNameMatcher = Pattern.compile(LAST_NAME_REGEX).matcher(value);
                        String lastNameUpper = lastNameMatcher.find() ? lastNameMatcher.group() : EMPTY;
                        if (!lastNameUpper.isEmpty()) {
                            Matcher lastNamePostfixMatcher = Pattern.compile(NOT_FIRST_LETTER_REGEX).matcher(lastNameUpper);
                            String lastName = lastNamePostfixMatcher.find() ?
                                    lastNameUpper.charAt(0) + lastNamePostfixMatcher.group() :
                                    lastNameUpper;

                            PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
                            person = personRepository.findMostSimilarByLastName(lastName);
                        }
                    }
                    if (person == null) {
                        person = new Person();
                        person.setName(value);
                    }
                    certificate.setWorker(person);
                }
            }
        }
    }

    private String extractDataValue(String row) {
        if (row == null) return null;

        Matcher matcher = Pattern.compile(STRING_DATA_REGEX).matcher(row);
        String val = matcher.find() ? matcher.group()  : null;
        return val != null && val.isEmpty() ? null : val;
    }
}
