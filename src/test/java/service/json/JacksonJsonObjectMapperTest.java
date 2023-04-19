package service.json;

import org.junit.Test;

import java.util.*;

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

        String[] actual = JacksonJsonObjectMapper.getInstance().jsonToObject(json, String[].class);
        assertNotNull(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDoubleMapToJson() {
        TreeMap<Double, Double> map = new TreeMap<>();
        map.put(1.0, 1.0);
        map.put(10.0, 10.0);
        map.put(50.0, 5.0);
        map.put(5.0, 50.0);

        String expected = "{\n 1.0 : 1.0,\n 5.0 : 50.0,\n 10.0 : 10.0,\n 50.0 : 5.0\n}";
        assertEquals(expected, JacksonJsonObjectMapper.getInstance().doubleMapToJson(map));
    }

    @Test
    public void testJsonToDoubleMap() {
        TreeMap<Double, Double> expected = new TreeMap<>();
        expected.put(1.0, 1.0);
        expected.put(10.0, 10.0);
        expected.put(50.0, 5.0);
        expected.put(5.0, 50.0);

        String json = "{\n 1.0 : 1.0,\n 5.0 : 50.0,\n 10.0 : 10.0,\n 50.0 : 5.0\n}";
        assertEquals(expected, JacksonJsonObjectMapper.getInstance().jsonToDoubleMap(json));
    }
}