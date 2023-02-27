package service.channel.list;

import org.junit.Before;
import org.junit.Test;
import service.channel.list.PropertiesChannelListConfigHolder;

import static org.junit.Assert.*;

public class PropertiesChannelListConfigHolderTest {
    private static final String PROPERTIES_FILE = "properties/test_channel.properties";

    private PropertiesChannelListConfigHolder configHolder;

    @Before
    public void init() {
        configHolder = new PropertiesChannelListConfigHolder(PROPERTIES_FILE);
    }

    @Test
    public void getChannelsCertificatesFolder() {
        String expected = "test_Certificates";
        assertEquals(expected, configHolder.getChannelsCertificatesFolder());
    }
}