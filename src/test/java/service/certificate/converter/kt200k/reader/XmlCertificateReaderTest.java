package service.certificate.converter.kt200k.reader;

import localization.Labels;
import localization.Messages;
import mock.RepositoryConfigHolderMock;
import mock.RepositoryMockFactory;
import model.dto.Person;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.repos.person.PersonRepository;
import service.certificate.converter.kt200k.KT200KTranslate;
import service.certificate.converter.kt200k.model.Certificate;
import service.certificate.converter.kt200k.model.ClearanceClass;
import service.certificate.converter.kt200k.model.Sensor;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlCertificateReaderTest {

    private static final String TEST_FILE = "KT200K_testFile.xml";

    private static RepositoryMockFactory repositoryFactory;
    private static XmlCertificateReader reader;

    private static Person testPerson;

    @BeforeClass
    public static void init() {
        repositoryFactory = new RepositoryMockFactory(new RepositoryConfigHolderMock());
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);

        testPerson = new Person();
        testPerson.setName("Ігор");
        testPerson.setSurname("Васильєв");
        testPerson.setPosition("Інженер-програміст");
        testPerson.setPatronymic("Сергійович");
        personRepository.add(testPerson);

        reader = new XmlCertificateReader(repositoryFactory);
    }

    @AfterClass
    public static void dispose() {
        repositoryFactory.dispose();
    }

    @Test
    @SuppressWarnings("all")
    public void read() throws URISyntaxException {
        String path = XmlCertificateReaderTest.class.getClassLoader().getResource(TEST_FILE).getPath();
        File file = new File(path);

        Certificate certificate = reader.read(file);

        //Environment values
        assertEquals("23.00", certificate.getEnvironmentTemperature());
        assertEquals("758", certificate.getEnvironmentPressure());
        assertEquals("65", certificate.getEnvironmentHumidity());

        //Sensors numbers
        Sensor sensor12_03 = certificate.getSensor("12-03.");
        Sensor sensor12_04 = certificate.getSensor("12-04.");
        assertNotNull(sensor12_03);
        assertNotNull(sensor12_04);

        //Sensors clearence classes
        assertEquals(ClearanceClass.B, sensor12_03.getClearanceClass());
        assertEquals(ClearanceClass.B, sensor12_04.getClearanceClass());

        //Output values
        Sensor benchmark = certificate.getBenchmarkSensor();

        //Point index = 0
        assertEquals(Labels.DASH, benchmark.getPoint(0).getResistance());
        assertEquals("54.379", sensor12_03.getPoint(0).getResistance());
        assertEquals("54.301", sensor12_04.getPoint(0).getResistance());

        assertEquals("19.978", benchmark.getPoint(0).getTemperature());
        assertEquals("20.445", sensor12_03.getPoint(0).getTemperature());
        assertEquals("20.082", sensor12_04.getPoint(0).getTemperature());

        assertEquals(Labels.DASH, benchmark.getPoint(0).getDelta());
        assertEquals("-0.467", sensor12_03.getPoint(0).getDelta());
        assertEquals("-0.104", sensor12_04.getPoint(0).getDelta());

        assertEquals("0.0000", benchmark.getPoint(0).getVoltage());
        assertEquals(Labels.DASH, sensor12_03.getPoint(0).getVoltage());
        assertEquals(Labels.DASH, sensor12_04.getPoint(0).getVoltage());

        assertEquals(Labels.DASH, benchmark.getPoint(0).getAdditionalDelta());
        assertEquals("0.400", sensor12_03.getPoint(0).getAdditionalDelta());
        assertEquals("0.400", sensor12_04.getPoint(0).getAdditionalDelta());

        //Point index = 1
        assertEquals(Labels.DASH, benchmark.getPoint(1).getResistance());
        assertEquals("60.797", sensor12_03.getPoint(1).getResistance());
        assertEquals("60.719", sensor12_04.getPoint(1).getResistance());

        assertEquals("50.003", benchmark.getPoint(1).getTemperature());
        assertEquals("50.444", sensor12_03.getPoint(1).getTemperature());
        assertEquals("50.077", sensor12_04.getPoint(1).getTemperature());

        assertEquals(Labels.DASH, benchmark.getPoint(1).getDelta());
        assertEquals("-0.441", sensor12_03.getPoint(1).getDelta());
        assertEquals("-0.074", sensor12_04.getPoint(1).getDelta());

        assertEquals("0.0000", benchmark.getPoint(1).getVoltage());
        assertEquals(Labels.DASH, sensor12_03.getPoint(1).getVoltage());
        assertEquals(Labels.DASH, sensor12_04.getPoint(1).getVoltage());

        assertEquals(Labels.DASH, benchmark.getPoint(1).getAdditionalDelta());
        assertEquals("0.550", sensor12_03.getPoint(1).getAdditionalDelta());
        assertEquals("0.550", sensor12_04.getPoint(1).getAdditionalDelta());

        //Point index = 2
        assertEquals(Labels.DASH, benchmark.getPoint(2).getResistance());
        assertEquals("71.507", sensor12_03.getPoint(2).getResistance());
        assertEquals("71.408", sensor12_04.getPoint(2).getResistance());

        assertEquals("100.003", benchmark.getPoint(2).getTemperature());
        assertEquals("100.513", sensor12_03.getPoint(2).getTemperature());
        assertEquals("100.049", sensor12_04.getPoint(2).getTemperature());

        assertEquals(Labels.DASH, benchmark.getPoint(2).getDelta());
        assertEquals("-0.510", sensor12_03.getPoint(2).getDelta());
        assertEquals("-0.046", sensor12_04.getPoint(2).getDelta());

        assertEquals("0.0000", benchmark.getPoint(2).getVoltage());
        assertEquals(Labels.DASH, sensor12_03.getPoint(2).getVoltage());
        assertEquals(Labels.DASH, sensor12_04.getPoint(2).getVoltage());

        assertEquals(Labels.DASH, benchmark.getPoint(2).getAdditionalDelta());
        assertEquals("0.800", sensor12_03.getPoint(2).getAdditionalDelta());
        assertEquals("0.800", sensor12_04.getPoint(2).getAdditionalDelta());

        //Point index = 3
        assertEquals(Labels.DASH, benchmark.getPoint(3).getResistance());
        assertEquals("60.800", sensor12_03.getPoint(3).getResistance());
        assertEquals("60.713", sensor12_04.getPoint(3).getResistance());

        assertEquals("50.000", benchmark.getPoint(3).getTemperature());
        assertEquals("50.457", sensor12_03.getPoint(3).getTemperature());
        assertEquals("50.048", sensor12_04.getPoint(3).getTemperature());

        assertEquals(Labels.DASH, benchmark.getPoint(3).getDelta());
        assertEquals("-0.457", sensor12_03.getPoint(3).getDelta());
        assertEquals("-0.048", sensor12_04.getPoint(3).getDelta());

        assertEquals("0.0000", benchmark.getPoint(3).getVoltage());
        assertEquals(Labels.DASH, sensor12_03.getPoint(3).getVoltage());
        assertEquals(Labels.DASH, sensor12_04.getPoint(3).getVoltage());

        assertEquals(Labels.DASH, benchmark.getPoint(3).getAdditionalDelta());
        assertEquals("0.550", sensor12_03.getPoint(3).getAdditionalDelta());
        assertEquals("0.550", sensor12_04.getPoint(3).getAdditionalDelta());

        //Point index = 4
        assertEquals(Labels.DASH, benchmark.getPoint(4).getResistance());
        assertEquals("54.379", sensor12_03.getPoint(4).getResistance());
        assertEquals("54.299", sensor12_04.getPoint(4).getResistance());

        assertEquals("19.994", benchmark.getPoint(4).getTemperature());
        assertEquals("20.447", sensor12_03.getPoint(4).getTemperature());
        assertEquals("20.073", sensor12_04.getPoint(4).getTemperature());

        assertEquals(Labels.DASH, benchmark.getPoint(4).getDelta());
        assertEquals("-0.453", sensor12_03.getPoint(4).getDelta());
        assertEquals("-0.079", sensor12_04.getPoint(4).getDelta());

        assertEquals("0.0000", benchmark.getPoint(4).getVoltage());
        assertEquals(Labels.DASH, sensor12_03.getPoint(4).getVoltage());
        assertEquals(Labels.DASH, sensor12_04.getPoint(4).getVoltage());

        assertEquals(Labels.DASH, benchmark.getPoint(4).getAdditionalDelta());
        assertEquals("0.400", sensor12_03.getPoint(4).getAdditionalDelta());
        assertEquals("0.400", sensor12_04.getPoint(4).getAdditionalDelta());

        //Sensor remark
        Map<String, String> messages = Messages.getMessages(KT200KTranslate.class);
        String remarkSensor12_03 = messages.get("notApprove") +
                Labels.DOT +
                Labels.SPACE +
                String.format(messages.get("note"), "C") +
                Labels.DOT;
        String remarkSensor12_04 = messages.get("approve") + Labels.DOT;
        assertEquals(remarkSensor12_03, sensor12_03.getRemark());
        assertEquals(remarkSensor12_04, sensor12_04.getRemark());

        //Date
        assertEquals("17.01.2023", certificate.getDate());

        //Worker
        assertEquals(testPerson, certificate.getWorker());
    }
}