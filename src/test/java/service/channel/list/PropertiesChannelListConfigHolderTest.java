package service.channel.list;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

public class PropertiesChannelListConfigHolderTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/channel_test.properties";

    private ChannelListConfigHolder configHolder;
    private ChannelListConfigHolder configHolderPropertiesFileNotExists;

    @Before
    public void setUp() {
        configHolder = new PropertiesChannelListConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        configHolderPropertiesFileNotExists = new PropertiesChannelListConfigHolder(EMPTY);
    }

    @Test
    public void getChannelsCertificatesFolder() {
        String expected = "test_Certificates";

        assertEquals(expected, configHolder.getChannelsCertificatesFolder());
        assertNotNull(configHolderPropertiesFileNotExists.getChannelsCertificatesFolder());
    }
}