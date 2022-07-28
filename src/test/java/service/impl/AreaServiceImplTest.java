package service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.AreaRepositorySQLite;
import service.AreaService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AreaServiceImplTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private AreaService service = new AreaServiceImpl(new AreaRepositorySQLite(DB_URL, null, null));

    private static final String AREA_1 = "area1";
    private static final String AREA_2 = "area2";
    private static final String AREA_3 = "area3";
    private static final String AREA_4 = "area4";
    private static final String AREA_5 = "area5";
    private static final String AREA_6 = "area6";
    private static final String AREA_7 = "area7";

    @Before
    public void setUp() {
        service.add(AREA_1);
        service.add(AREA_2);
        service.add(AREA_3);
        service.add(AREA_4);
        service.add(AREA_5);
    }

    @After
    public void tearDown() {
        service.clear();
    }

    @Test
    public void testGetAll() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(AREA_1);
        expected.add(AREA_2);
        expected.add(AREA_3);
        expected.add(AREA_4);
        expected.add(AREA_5);

        assertArrayEquals(expected.toArray(new String[0]), service.getAll().toArray(new String[0]));
    }

    @Test
    public void testGetAllInStrings() {
        String[] expected = new String[]{AREA_1, AREA_2, AREA_3, AREA_4, AREA_5};

        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNewArea() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(AREA_1);
        expected.add(AREA_2);
        expected.add(AREA_3);
        expected.add(AREA_4);
        expected.add(AREA_5);
        expected.add(AREA_6);

        assertTrue(service.add(AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void remove() {
    }

    @Test
    public void set() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void rewrite() {
    }

    @Test
    public void resetToDefault() {
    }
}