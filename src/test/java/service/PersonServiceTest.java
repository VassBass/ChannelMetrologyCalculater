package service;

import constants.WorkPositions;
import def.DefaultPersons;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.PersonServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final PersonService service = new PersonServiceImpl(DB_URL);

    private static final String EMPTY_ARRAY = "<Порожньо>";

    private Person engineer(){
        Person person = new Person();
        person.setId(1);
        person.setSurname("Петров");
        person.setName("Петр");
        person.setPatronymic("Петрович");
        person.setPosition(WorkPositions.ENGINEER_ASKV);
        return person;
    }

    private Person juniorEngineer(){
        Person person = new Person();
        person.setId(2);
        person.setSurname("Иванов");
        person.setName("Иван");
        person.setPatronymic("Иванович");
        person.setPosition(WorkPositions.JUNIOR_ENGINEER);
        return person;
    }

    private Person headOfArea(){
        Person person = new Person();
        person.setId(3);
        person.setSurname("Васильев");
        person.setName("Василий");
        person.setPatronymic("Васильевич");
        person.setPosition(WorkPositions.HEAD_OF_AREA);
        return person;
    }

    private Person headOfDepartment(){
        Person person = new Person();
        person.setId(4);
        person.setSurname("Семёнов");
        person.setName("Семён");
        person.setPatronymic("Семёнович");
        person.setPosition(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);
        return person;
    }

    private String getFullNameOfHeadOfDepartment(){
        Person person = new Person();
        person.setId(4);
        person.setSurname("Семёнов");
        person.setName("Семён");
        person.setPatronymic("Семёнович");
        person.setPosition(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);
        return person.getFullName();
    }

    private Person electronicEngineer(){
        Person person = new Person();
        person.setId(5);
        person.setSurname("Павлов");
        person.setName("Павел");
        person.setPatronymic("Павлович");
        person.setPosition(WorkPositions.ELECTRONIC_ENGINEER);
        return person;
    }

    private ArrayList<Person>getTestList(){
        ArrayList<Person>persons = new ArrayList<>();
        persons.add(this.engineer());
        persons.add(this.juniorEngineer());
        persons.add(this.headOfArea());
        persons.add(this.headOfDepartment());
        return persons;
    }

    private ArrayList<Person> getAllFromDB() {
        ArrayList<Person>persons = new ArrayList<>();
        String sql = "SELECT * FROM persons";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Person person = new Person();
                    person.setId(resultSet.getInt("id"));
                    person.setSurname(resultSet.getString("surname"));
                    person.setName(resultSet.getString("name"));
                    person.setPatronymic(resultSet.getString("patronymic"));
                    person.setPosition(resultSet.getString("position"));
                    persons.add(person);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return persons;
    }

    private long howLong(java.util.Date start, Date end){
        return end.getTime() - start.getTime();
    }

    private boolean isBackgroundTaskIsRun() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return this.service.backgroundTaskIsRun();
    }

    @BeforeEach
    void setUp() {
        Date start = new Date();
        this.service.init();
        this.service.clear();
        while (this.isBackgroundTaskIsRun());
        ArrayList<Person>testArray = this.getTestList();
        this.service.addInCurrentThread(testArray);
        Date end = new Date();
        System.out.println("setUp() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();
        ArrayList<Person>testArray = this.getTestList();

        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());

        System.out.println("getAll() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllNames() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        String[]testArray = new String[testList.size() + 1];
        int index = 0;
        testArray[index++] = EMPTY_ARRAY;
        for (Person p : testList){
            testArray[index++] = p.getFullName();
        }

        assertArrayEquals(testArray, this.service.getAllNames());
        testList = this.getAllFromDB();

        String[]allNamesFromDB = new String[testList.size() + 1];
        index = 0;
        allNamesFromDB[index++] = EMPTY_ARRAY;
        for (Person p : testList){
            allNamesFromDB[index++] = p.getFullName();
        }

        assertArrayEquals(testArray, allNamesFromDB);

        System.out.println("getAllInStrings() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void getNamesOfHeads() {
        Date start = new Date();
        String[]testArray = new String[]{EMPTY_ARRAY, this.getFullNameOfHeadOfDepartment()};

        assertArrayEquals(testArray, this.service.getNamesOfHeads());
        System.out.println("getNamesOfHeads() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        testList.add(this.electronicEngineer());

        ArrayList<Person>persons = this.service.add(this.electronicEngineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.add(this.electronicEngineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.add(null);
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("add() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void addInCurrentThread() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        testList.add(this.electronicEngineer());

        this.service.addInCurrentThread(this.electronicEngineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.addInCurrentThread(this.electronicEngineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.addInCurrentThread((Person) null);
        assertIterableEquals(testList, this.service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());
        System.out.println("addInCurrentThread(Person) duration = " + this.howLong(start, new Date()) + " mills");

        this.setUp();
        start = new Date();
        testList = this.getTestList();
        testList.add(this.electronicEngineer());

        ArrayList<Person>personsToAdd = new ArrayList<>();
        personsToAdd.add(this.electronicEngineer());
        personsToAdd.add(this.engineer());
        personsToAdd.add(null);

        this.service.addInCurrentThread(personsToAdd);
        assertIterableEquals(testList, this.service.getAll());
        this.service.addInCurrentThread((ArrayList<Person>) null);
        assertIterableEquals(testList, this.service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("addInCurrentThread(ArrayList<Person>) duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        testList.remove(this.headOfArea());

        ArrayList<Person>persons = this.service.remove(this.headOfArea());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.remove(this.headOfArea());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.remove(null);
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("remove() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        int index = testList.indexOf(this.engineer());
        testList.set(index, this.electronicEngineer());

        ArrayList<Person>persons = this.service.set(this.engineer(), this.electronicEngineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.set(this.engineer(), this.engineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.set(null, this.electronicEngineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.set(this.juniorEngineer(), this.electronicEngineer());
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.set(this.juniorEngineer(), null);
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        persons = this.service.set(null, null);
        assertIterableEquals(testList, persons);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("set() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void setInCurrentThread() {
        Date start = new Date();
        ArrayList<Person>testList = this.getTestList();
        int index = testList.indexOf(this.engineer());
        testList.set(index, this.electronicEngineer());

        this.service.setInCurrentThread(this.engineer(), this.electronicEngineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.setInCurrentThread(this.engineer(), this.engineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.setInCurrentThread(null, this.electronicEngineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.setInCurrentThread(this.juniorEngineer(), this.electronicEngineer());
        assertIterableEquals(testList, this.service.getAll());
        this.service.setInCurrentThread(this.juniorEngineer(), null);
        assertIterableEquals(testList, this.service.getAll());
        this.service.setInCurrentThread(null, null);
        assertIterableEquals(testList, this.service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("setInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();

        ArrayList<Person>testList = this.getTestList();
        int index = testList.indexOf(this.engineer());

        assertEquals(this.engineer(), this.service.get(index));
        assertNull(this.service.get(-1));
        assertNull(this.service.get(526));

        System.out.println("get() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        ArrayList<Person>fromDB = this.getAllFromDB();
        assertNotEquals(0, this.service.getAll().size());
        assertNotEquals(0, fromDB.size());
        this.service.clear();
        assertEquals(0, this.service.getAll().size());
        while (this.isBackgroundTaskIsRun());
        fromDB = this.getAllFromDB();
        assertEquals(0, fromDB.size());

        System.out.println("clear() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();

        ArrayList<Person>testList = new ArrayList<>();
        testList.add(this.headOfDepartment());
        testList.add(this.electronicEngineer());

        this.service.rewriteInCurrentThread(testList);
        assertEquals(testList, this.service.getAll());
        assertEquals(testList, this.getAllFromDB());

        System.out.println("rewriteInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();

        ArrayList<Person>testArray = DefaultPersons.get();
        this.service.resetToDefaultInCurrentThread();
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());

        System.out.println("resetToDefaultInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }
}