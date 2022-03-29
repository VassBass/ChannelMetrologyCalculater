package service;

import def.DefaultDepartments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.impl.DepartmentServiceImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final DepartmentService service = new DepartmentServiceImpl(DB_URL);

    private static final String DOF = "ДОФ";
    private static final String CVO = "ЦВО";
    private static final String VC = "ВЦ";

    @BeforeEach
    void setUp() {
        this.service.init();
        this.service.clear();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        this.service.addInCurrentThread(testArray);
    }

    @Test
    void getAll() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void getAllInStrings() {
        String[]testArray = new String[]{DOF, CVO};
        assertArrayEquals(testArray, this.service.getAllInStrings());
    }

    @Test
    void add() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        testArray.add(VC);
        ArrayList<String>departments = this.service.add(VC);
        assertIterableEquals(testArray, departments);
        departments = this.service.add(null);
        assertIterableEquals(testArray, departments);
    }

    @Test
    void remove() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        ArrayList<String>departments = this.service.remove(CVO);
        assertIterableEquals(testArray, departments);
        departments = this.service.remove(null);
        assertIterableEquals(testArray, departments);
        departments = this.service.remove(VC);
        assertIterableEquals(testArray, departments);
    }

    @Test
    void set() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(VC);
        ArrayList<String>departments = this.service.set(CVO, VC);
        assertIterableEquals(testArray, departments);
        departments = this.service.set(null, VC);
        assertIterableEquals(testArray, departments);
        departments = this.service.set(null, null);
        assertIterableEquals(testArray, departments);
        departments = this.service.set(CVO, VC);
        assertIterableEquals(testArray, departments);
        departments = this.service.set(VC, null);
        assertIterableEquals(testArray, departments);
    }

    @Test
    void get() {
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(DOF, dof);
        assertEquals(CVO, cvo);
        assertNull(nullString);
    }

    @Test
    void rewriteInCurrentThread() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(VC);
        testArray.add(CVO);
        testArray.add(DOF);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void resetToDefault() {
        ArrayList<String>testArray = DefaultDepartments.get();
        this.service.resetToDefault();
        assertIterableEquals(testArray, this.service.getAll());
    }
}