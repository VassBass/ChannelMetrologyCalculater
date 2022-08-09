package service.impl;

import def.DefaultAreas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.AreaRepositorySQLite;
import service.AreaService;

import java.util.*;

import static org.junit.Assert.*;

public class AreaServiceImplTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final AreaService service = new AreaServiceImpl(new AreaRepositorySQLite(DB_URL, null, null));

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
    public void testGetInstance() {
        assertSame(service, AreaServiceImpl.getInstance());
    }

    @Test
    public void testGetAll() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertArrayEquals(expected.toArray(new String[0]), service.getAll().toArray(new String[0]));
    }

    @Test
    public void testGetAllInStrings() {
        String[] expected = new String[]{AREA_1, AREA_2, AREA_3, AREA_4, AREA_5};

        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNew() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5,
                AREA_6
        ));

        assertTrue(service.add(AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertFalse(service.add(AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertTrue(service.remove(AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertFalse(service.remove(AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_6,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertTrue(service.set(AREA_2, AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertFalse(service.set(AREA_6, AREA_7));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertTrue(service.set(AREA_2, AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        assertFalse(service.set(AREA_6, AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testClear() {
        assertTrue(service.clear());
        assertEquals(0, service.getAll().size());
        assertEquals(0, service.getAllInStrings().length);
    }

    @Test
    public void testRewriteNew() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_6,
                AREA_7,
                AREA_2
        ));

        assertTrue(service.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testResetToDefault() {
        String[]expected = DefaultAreas.get().toArray(new String[0]);

        assertTrue(service.resetToDefault());
        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNewCollection(){
        List<String> toAdd = Arrays.asList(AREA_6, AREA_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        expected.addAll(toAdd);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNewCollectionWithExisted(){
        List<String> toAdd = Arrays.asList(AREA_6, AREA_2, AREA_4, AREA_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        ));

        expected.addAll(toAdd);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNewCollectionWithNull(){
        List<String> toAdd = Arrays.asList(AREA_6, null, AREA_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5,
                AREA_6,
                AREA_7));

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }
}