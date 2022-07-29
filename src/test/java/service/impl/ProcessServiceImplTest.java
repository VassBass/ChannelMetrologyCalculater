package service.impl;

import def.DefaultProcesses;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.ProcessRepositorySQLite;
import service.ProcessService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ProcessServiceImplTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final ProcessService service = new ProcessServiceImpl(new ProcessRepositorySQLite(DB_URL, null, null));

    private static final String PROCESS_1 = "process1";
    private static final String PROCESS_2 = "process2";
    private static final String PROCESS_3 = "process3";
    private static final String PROCESS_4 = "process4";
    private static final String PROCESS_5 = "process5";
    private static final String PROCESS_6 = "process6";
    private static final String PROCESS_7 = "process7";

    @Before
    public void setUp() {
        service.add(PROCESS_1);
        service.add(PROCESS_2);
        service.add(PROCESS_3);
        service.add(PROCESS_4);
        service.add(PROCESS_5);
    }

    @After
    public void tearDown() {
        service.clear();
    }

    @Test
    public void testGetAll() {
        Set<String> expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertArrayEquals(expected.toArray(new String[0]), service.getAll().toArray(new String[0]));
    }

    @Test
    public void testGetAllInStrings() {
        String[] expected = new String[]{PROCESS_1, PROCESS_2, PROCESS_3, PROCESS_4, PROCESS_5};

        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNew() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);
        expected.add(PROCESS_6);

        assertTrue(service.add(PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.add(PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.add(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertTrue(service.remove(PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.remove(PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.remove(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_6);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertTrue(service.set(PROCESS_2, PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(PROCESS_6, PROCESS_7));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(null, PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertTrue(service.set(PROCESS_2, PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(PROCESS_6, PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(null, PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNullInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(PROCESS_2, null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNullInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.set(null, null));
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
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_6);
        expected.add(PROCESS_7);
        expected.add(PROCESS_2);

        assertTrue(service.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRewriteNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(PROCESS_1);
        expected.add(PROCESS_2);
        expected.add(PROCESS_3);
        expected.add(PROCESS_4);
        expected.add(PROCESS_5);

        assertFalse(service.rewrite(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testResetToDefault() {
        String[]expected = DefaultProcesses.get().toArray(new String[0]);

        assertTrue(service.resetToDefault());
        assertArrayEquals(expected, service.getAllInStrings());
    }
}