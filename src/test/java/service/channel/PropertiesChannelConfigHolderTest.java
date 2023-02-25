package service.channel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesChannelConfigHolderTest {
    private static final String PROPERTIES_FILE = "properties/test_channel.properties";

    private PropertiesChannelConfigHolder configHolder;

    @Before
    public void init() {
        configHolder = new PropertiesChannelConfigHolder(PROPERTIES_FILE);
    }

    @Test
    public void getChannelsCertificatesFolder() {
        String expected = "test_Certificates";
        assertEquals(expected, configHolder.getChannelsCertificatesFolder());
    }
}