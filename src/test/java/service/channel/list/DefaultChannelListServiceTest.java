package service.channel.list;

import mock.RepositoryMockFactory;
import model.dto.Channel;
import model.dto.Sensor;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.SensorBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.repos.channel.ChannelRepository;
import repository.repos.sensor.SensorRepository;
import util.DateHelper;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultChannelListServiceTest {

    private RepositoryMockFactory repositoryMockFactory;
    private ChannelRepository channelRepository;
    private SensorRepository sensorRepository;
    private ChannelListService service;

    @Before
    public void setUp() throws Exception {
        repositoryMockFactory = new RepositoryMockFactory();
        channelRepository = repositoryMockFactory.getImplementation(ChannelRepository.class);
        sensorRepository = repositoryMockFactory.getImplementation(SensorRepository.class);

        service = new DefaultChannelListService(channelRepository, sensorRepository);
    }

    /**
     * If frequency = 1.0
     */
    private String createExpiredDate() {
        long result = Calendar.getInstance().getTimeInMillis() - 31_968_000_000L;
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(result);
        return DateHelper.dateToString(cal);
    }

    /**
     * If frequency = 1.0
     */
    private String createCloseToExpiredDate() {
        long result = Calendar.getInstance().getTimeInMillis() - 29_808_000_000L;
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(result);
        return DateHelper.dateToString(cal);
    }

    /**
     * If frequency = 1.0
     */
    private String createNotExpiredDate() {
        return DateHelper.dateToString(Calendar.getInstance());
    }

    @After
    public void tearDown() {
        repositoryMockFactory.dispose();
    }

    @Test
    public void testGetCodesOfExpiredChannels() {
        Channel expired0 = new ChannelBuilder("0").setDate(createExpiredDate()).setFrequency(1).build();
        Channel expired1 = new ChannelBuilder("1").setDate("expired").build();
        Channel closeToExpired2 = new ChannelBuilder("2").setDate(createCloseToExpiredDate()).setFrequency(1).build();
        Channel closeToExpired3 = new ChannelBuilder("3").setDate(createCloseToExpiredDate()).setFrequency(1).build();
        Channel notExpired4 = new ChannelBuilder("4").setDate(createNotExpiredDate()).setFrequency(1).build();
        Channel notExpired5 = new ChannelBuilder("5").setDate(createNotExpiredDate()).setFrequency(1).build();
        channelRepository.add(expired0);
        channelRepository.add(expired1);
        channelRepository.add(closeToExpired2);
        channelRepository.add(closeToExpired3);
        channelRepository.add(notExpired4);
        channelRepository.add(notExpired5);

        List<String> expected = Arrays.asList(expired0.getCode(), expired1.getCode());

        Collection<String> actual = service.getCodesOfExpiredChannels();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetCodesOfChannelsCloseToExpired() {
        Channel expired0 = new ChannelBuilder("0").setDate(createExpiredDate()).setFrequency(1).build();
        Channel expired1 = new ChannelBuilder("1").setDate("expired").build();
        Channel closeToExpired2 = new ChannelBuilder("2").setDate(createCloseToExpiredDate()).setFrequency(1).build();
        Channel closeToExpired3 = new ChannelBuilder("3").setDate(createCloseToExpiredDate()).setFrequency(1).build();
        Channel notExpired4 = new ChannelBuilder("4").setDate(createNotExpiredDate()).setFrequency(1).build();
        Channel notExpired5 = new ChannelBuilder("5").setDate(createNotExpiredDate()).setFrequency(1).build();
        channelRepository.add(expired0);
        channelRepository.add(expired1);
        channelRepository.add(closeToExpired2);
        channelRepository.add(closeToExpired3);
        channelRepository.add(notExpired4);
        channelRepository.add(notExpired5);

        List<String> expected = Arrays.asList(closeToExpired2.getCode(), closeToExpired3.getCode());

        Collection<String> actual = service.getCodesOfChannelsCloseToExpired();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void getFullPath() {
        String department = "department";
        String area = "area";
        String process = "process";
        String installation = "installation";
        Channel channel = new ChannelBuilder("0")
                .setDepartment(department)
                .setArea(area)
                .setProcess(process)
                .setInstallation(installation)
                .build();

        String fullPath = service.getFullPath(channel);
        assertTrue(fullPath.contains(department));
        assertTrue(fullPath.contains(area));
        assertTrue(fullPath.contains(process));
        assertTrue(fullPath.contains(installation));
    }

    @Test
    public void getDateOfNextCheck() {
        Calendar date = Calendar.getInstance();
        Channel channel = new ChannelBuilder("0")
                .setDate(DateHelper.dateToString(date))
                .setFrequency(2)
                .build();

        Calendar expected = DateHelper.stringToDate(DateHelper.getNextDate(channel.getDate(), channel.getFrequency()));
        assertEquals(expected, service.getDateOfNextCheck(channel));
    }

    @Test
    public void getSensor() {
        Channel channel = new ChannelBuilder("0").build();
        Sensor sensor = new SensorBuilder(channel.getCode()).build();

        sensorRepository.add(sensor);
        assertEquals(sensor, service.getSensor(channel));
    }
}