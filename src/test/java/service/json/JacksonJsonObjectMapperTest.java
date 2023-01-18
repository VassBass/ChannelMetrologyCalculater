package service.json;

import model.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class JacksonJsonObjectMapperTest {

    @Test
    public void testGetInstance() {
        assertSame(JacksonJsonObjectMapper.getInstance(), JacksonJsonObjectMapper.getInstance());
    }

    @Test
    public void objectToJson() {
        Person person = new Person(1);
        String expected = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"surname\" : \"\",\n" +
                "  \"name\" : \"\",\n" +
                "  \"patronymic\" : \"\",\n" +
                "  \"position\" : \"\"\n" +
                "}";
        assertEquals(expected, JacksonJsonObjectMapper.getInstance().objectToJson(person));
    }

    @Test
    public void jsonToObject() {
        Person expected = new Person(1);
        String json = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"surname\" : \"\",\n" +
                "  \"name\" : \"\",\n" +
                "  \"patronymic\" : \"\",\n" +
                "  \"position\" : \"\"\n" +
                "}";
        Person actual = JacksonJsonObjectMapper.getInstance().JsonToObject(json, Person.class);
        assertNotNull(actual);
        assertTrue(expected.isMatch(actual));
    }
}