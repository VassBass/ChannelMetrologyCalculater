package service.channel.info;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PropertiesChannelInfoConfigHolderTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_channel.properties";

    private PropertiesChannelInfoConfigHolder configHolder;
    private PropertiesChannelInfoConfigHolder configHolderPropertiesFileNotExists;

    @Before
    public void setUp() {
        configHolder = new PropertiesChannelInfoConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        configHolderPropertiesFileNotExists = new PropertiesChannelInfoConfigHolder(EMPTY);
    }

    @Test
    public void testGetDialogWidth() {
        int expected = 100;

        assertEquals(expected, configHolder.getDialogWidth());
        assertTrue(configHolderPropertiesFileNotExists.getDialogWidth() > 0);
    }

    @Test
    public void testGetDialogHeight() {
        int expected = 100;

        assertEquals(expected, configHolder.getDialogHeight());
        assertTrue(configHolderPropertiesFileNotExists.getDialogHeight() > 0);
    }
}