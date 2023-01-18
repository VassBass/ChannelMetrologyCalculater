package service.json;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

public class JacksonJsonObjectMapperTest {

    @Test
    public void testGetInstance() {
        assertSame(JacksonJsonObjectMapper.getInstance(), JacksonJsonObjectMapper.getInstance());
    }

    @Test
    public void objectToJson() {
        Collection<String> collection = Arrays.asList("string1", "string2", EMPTY, "string3", null);
        String expected = "[ \"string1\", \"string2\", \"\", \"string3\", null ]";

        assertEquals(expected, JacksonJsonObjectMapper.getInstance().objectToJson(collection));
    }

    @Test
    public void jsonToObject() {
        String[] expected = {"string1", "string2", EMPTY, "string3", null};
        String json = "[ \"string1\", \"string2\", \"\", \"string3\", null ]";

        String[] actual = JacksonJsonObjectMapper.getInstance().JsonToObject(json, String[].class);
        assertNotNull(actual);
        assertArrayEquals(expected, actual);
    }
}