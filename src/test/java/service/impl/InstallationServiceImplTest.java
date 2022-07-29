package service.impl;

import def.DefaultInstallations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.InstallationRepositorySQLite;
import service.InstallationService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class InstallationServiceImplTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final InstallationService service = new InstallationServiceImpl(new InstallationRepositorySQLite(DB_URL, null, null));

    private static final String INSTALLATION_1 = "installation1";
    private static final String INSTALLATION_2 = "installation2";
    private static final String INSTALLATION_3 = "installation3";
    private static final String INSTALLATION_4 = "installation4";
    private static final String INSTALLATION_5 = "installation5";
    private static final String INSTALLATION_6 = "installation6";
    private static final String INSTALLATION_7 = "installation7";

    @Before
    public void setUp() {
        service.add(INSTALLATION_1);
        service.add(INSTALLATION_2);
        service.add(INSTALLATION_3);
        service.add(INSTALLATION_4);
        service.add(INSTALLATION_5);
    }

    @After
    public void tearDown() {
        service.clear();
    }

    @Test
    public void testGetAll() {
        Set<String> expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertArrayEquals(expected.toArray(new String[0]), service.getAll().toArray(new String[0]));
    }

    @Test
    public void testGetAllInStrings() {
        String[] expected = new String[]{INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5};

        assertArrayEquals(expected, service.getAllInStrings());
    }

    @Test
    public void testAddNew() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);
        expected.add(INSTALLATION_6);

        assertTrue(service.add(INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.add(INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testAddNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.add(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(service.remove(INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.remove(INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRemoveNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.remove(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_6);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(service.set(INSTALLATION_2, INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.set(INSTALLATION_6, INSTALLATION_7));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNewInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.set(null, INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(service.set(INSTALLATION_2, INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNotExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.set(INSTALLATION_6, INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetExistingInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.set(null, INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNullInsteadOfExisting() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.set(INSTALLATION_2, null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testSetNullInsteadOfNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

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
        expected.add(INSTALLATION_6);
        expected.add(INSTALLATION_7);
        expected.add(INSTALLATION_2);

        assertTrue(service.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testRewriteNull() {
        Set<String>expected = new LinkedHashSet<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(service.rewrite(null));
        assertArrayEquals(expected.toArray(new String[0]), service.getAllInStrings());
    }

    @Test
    public void testResetToDefault() {
        String[]expected = DefaultInstallations.get().toArray(new String[0]);

        assertTrue(service.resetToDefault());
        assertArrayEquals(expected, service.getAllInStrings());
    }
}