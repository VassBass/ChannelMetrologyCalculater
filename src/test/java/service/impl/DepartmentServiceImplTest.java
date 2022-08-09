package service.impl;

import def.DefaultDepartments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.DepartmentRepositorySQLite;
import service.DepartmentService;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class DepartmentServiceImplTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final DepartmentService service = new DepartmentServiceImpl(new DepartmentRepositorySQLite(DB_URL, null, null));

    private static final String DEPARTMENT_1 = "department1";
    private static final String DEPARTMENT_2 = "department2";
    private static final String DEPARTMENT_3 = "department3";
    private static final String DEPARTMENT_4 = "department4";
    private static final String DEPARTMENT_5 = "department5";
    private static final String DEPARTMENT_6 = "department6";
    private static final String DEPARTMENT_7 = "department7";

    @Before
    public void setUp() {
        service.add(DEPARTMENT_1);
        service.add(DEPARTMENT_2);
        service.add(DEPARTMENT_3);
        service.add(DEPARTMENT_4);
        service.add(DEPARTMENT_5);
    }

    @After
    public void tearDown() {
        service.clear();
    }

    @Test
    public void testGetInstance() {
        assertSame(service, DepartmentServiceImpl.getInstance());
    }

    @Test
    public void testGetAll() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertArrayEquals(expected.toArray(new String[0]), service.getAll().toArray(new String[0]));
    }

    @Test
    public void testGetAllInStrings() {
        String[] expected = new String[]{DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5};

        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNew() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5,
                DEPARTMENT_6
        ));

        assertTrue(service.add(DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertFalse(service.add(DEPARTMENT_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertTrue(service.remove(DEPARTMENT_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertFalse(service.remove(DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_6,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertTrue(service.set(DEPARTMENT_2, DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertFalse(service.set(DEPARTMENT_6, DEPARTMENT_7));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertTrue(service.set(DEPARTMENT_2, DEPARTMENT_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNotExisting() {
        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        assertFalse(service.set(DEPARTMENT_6, DEPARTMENT_2));
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
                DEPARTMENT_6,
                DEPARTMENT_7,
                DEPARTMENT_2
        ));

        assertTrue(service.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testResetToDefault() {
        String[]expected = DefaultDepartments.get().toArray(new String[0]);

        assertTrue(service.resetToDefault());
        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNewCollection(){
        List<String> toAdd = Arrays.asList(DEPARTMENT_6, DEPARTMENT_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        expected.addAll(toAdd);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNewCollectionWithExisted(){
        List<String> toAdd = Arrays.asList(DEPARTMENT_6, DEPARTMENT_2, DEPARTMENT_4, DEPARTMENT_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        ));

        expected.addAll(toAdd);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNewCollectionWithNull(){
        List<String> toAdd = Arrays.asList(DEPARTMENT_6, null, DEPARTMENT_7);

        Set<String> expected = new LinkedHashSet<>(Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5,
                DEPARTMENT_6,
                DEPARTMENT_7
        ));

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }
}