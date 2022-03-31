package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import constants.WorkPositions;
import model.Person;
import repository.PersonRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepositoryImpl extends Repository<Person> implements PersonRepository {
    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());

    private static final String EMPTY_ARRAY = "<Порожньо>";

    private boolean backgroundTaskRunning = false;

    public PersonRepositoryImpl(){super();}
    public PersonRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS persons ("
                    + "name text NOT NULL"
                    + ", surname text NOT NULL"
                    + ", patronymic text"
                    + ", position text NOT NULL"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request");
            sql = "SELECT * FROM persons";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Person person = new Person();
                    person.setName(resultSet.getString("name"));
                    person.setSurname(resultSet.getString("surname"));
                    person.setPatronymic(resultSet.getString("patronymic"));
                    person.setPosition(resultSet.getString("position"));
                    this.mainList.add(person);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Person> getAll() {
        return this.mainList;
    }

    @Override
    public String[] getAllNamesWithFirstEmptyString() {
        int length = this.mainList.size() + 1;
        String[] persons = new String[length];
        persons[0] = EMPTY_ARRAY;
        for (int x = 0; x< this.mainList.size(); x++){
            int y = x+1;
            persons[y] = this.mainList.get(x).getFullName();
        }
        return persons;
    }

    @Override
    public String[] getNamesOfHeadsWithFirstEmptyString() {
        ArrayList<String>heads = new ArrayList<>();
        heads.add(EMPTY_ARRAY);
        for (Person worker : this.mainList){
            if (worker.getPosition().equals(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP)){
                heads.add(worker.getFullName());
            }
        }
        return heads.toArray(new String[0]);
    }

    @Override
    public Person get(int index) {
        return index < 0 | index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(Person person) {
        if (person != null && !this.mainList.contains(person)) {
            this.mainList.add(person);
            new BackgroundAction().add(person);
        }
    }

    @Override
    public void addInCurrentThread(ArrayList<Person> persons) {
        if (persons != null && !persons.isEmpty()) {
            for (Person person : persons) {
                if (person != null && !this.mainList.contains(person)){
                    this.mainList.add(person);
                }
            }
            new BackgroundAction().addPersons(persons);
        }
    }

    @Override
    public void addInCurrentThread(Person person) {
        if (person != null && !this.mainList.contains(person)) {
            this.mainList.add(person);
            new BackgroundAction().addPerson(person);
        }
    }

    @Override
    public void remove(Person person) {
        if (person != null && this.mainList.remove(person)) {
            new BackgroundAction().remove(person);
        }
    }

    @Override
    public void set(Person oldPerson, Person newPerson) {
        if (oldPerson != null && newPerson != null
                && this.mainList.contains(oldPerson)) {
            int oldIndex = this.mainList.indexOf(oldPerson);
            int newIndex = this.mainList.indexOf(newPerson);
            if (newIndex == -1 || oldIndex == newIndex) {
                this.mainList.set(oldIndex, newPerson);
                new BackgroundAction().set(oldPerson, newPerson);
            }
        }
    }

    @Override
    public void setInCurrentThread(Person oldPerson, Person newPerson) {
        if (oldPerson != null && newPerson != null
                && this.mainList.contains(oldPerson) && !this.mainList.contains(newPerson)) {
            int index = this.mainList.indexOf(oldPerson);
            this.mainList.set(index, newPerson);
            new BackgroundAction().setPerson(oldPerson, newPerson);
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Person> persons) {
        if (persons != null && !persons.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(persons);
            new BackgroundAction().rewritePersons(persons);
        }
    }

    @Override
    public void rewrite(ArrayList<Person> persons) {
        if (persons != null && !persons.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(persons);
            new BackgroundAction().rewrite(persons);
        }
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private Person person, old;
        private ArrayList<Person>list;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(Person person){
            this.person = person;
            this.action = Action.ADD;
            this.start();
        }

        void remove(Person person){
            this.person = person;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<Person>list){
            this.list = list;
            this.action = list == null ? Action.CLEAR : Action.REWRITE;
            this.start();
        }

        void set(Person oldPerson, Person newPerson){
            this.old = oldPerson;
            this.person = newPerson;
            this.action = Action.SET;
            this.start();
        }

        private void start(){
            Application.setBusy(true);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (saveMessage != null) saveMessage.setVisible(true);
                }
            });
            backgroundTaskRunning = true;
            this.execute();
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addPerson(this.person);
                case REMOVE:
                    return this.removePerson(this.person);
                case CLEAR:
                    return this.clearPersons();
                case SET:
                    return this.setPerson(this.old, this.person);
                case REWRITE:
                    return this.rewritePersons(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            mainList.remove(this.person);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.person)) mainList.add(this.person);
                            break;
                        case SET:
                            mainList.remove(this.person);
                            if (!mainList.contains(this.old)) mainList.add(this.old);
                            break;
                    }
                    String message = "Виникла помилка! Данні не збереглися! Спробуйте будь-ласка ще раз!";
                    if (Application.context != null) JOptionPane.showMessageDialog(Application.context.mainScreen, message, "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addPerson(Person person){
            String sql = "INSERT INTO persons ('surname', 'name', 'patronymic', 'position') "
                        + "VALUES(?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                LOGGER.fine("Send request");
                statement.setString(1, person.getSurname());
                statement.setString(2, person.getName());
                statement.setString(3, person.getPatronymic());
                statement.setString(4, person.getPosition());
                statement.execute();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        void addPersons(ArrayList<Person>persons){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to add");
                for (Person person : persons){
                    if (person != null) {
                        String sql = "INSERT INTO persons (name, surname, patronymic, position)"
                                + "SELECT "
                                + "'" + person.getName() + "', "
                                + "'" + person.getSurname() + "', "
                                + "'" + person.getPatronymic() + "', "
                                + "'" + person.getPosition() + "' "
                                + "WHERE NOT EXISTS(SELECT 1 FROM persons " +
                                "WHERE name = '" + person.getName() + "' "
                                + "AND surname = '" + person.getSurname() + "' "
                                + "AND patronymic = '" + person.getPatronymic() + "' "
                                + "AND position = '" + person.getPosition() + "'"
                                + ");";
                        statement.execute(sql);
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }

        private boolean removePerson(Person person){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM persons " +
                        "WHERE name = '" + person.getName() + "' "
                        + "AND surname = '" + person.getSurname() + "' "
                        + "AND patronymic = '" + person.getPatronymic() + "' "
                        + "AND position = '" + person.getPosition() + "'"
                        + ";";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean clearPersons(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM persons;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        boolean setPerson(Person oldPerson, Person newPerson){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql = "UPDATE persons SET "
                        + "name = '" + newPerson.getName() + "', "
                        + "surname = '" + newPerson.getSurname() + "', "
                        + "patronymic = '" + newPerson.getPatronymic() + "', "
                        + "position = '" + newPerson.getPosition() + "' "
                        + "WHERE name = '" + oldPerson.getName() + "' "
                        + "AND surname = '" + oldPerson.getSurname() + "' "
                        + "AND patronymic = '" + oldPerson.getPatronymic() + "' "
                        + "AND position = '" + oldPerson.getPosition() + "'"
                        + ";";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public boolean rewritePersons(ArrayList<Person>persons){
            String clearSql = "DELETE FROM persons;";
            String insertSql = "INSERT INTO persons ('name', 'surname', 'patronymic', 'position')" +
                    " VALUES (?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statementClear = connection.createStatement();
                PreparedStatement statement = connection.prepareStatement(insertSql)) {
                LOGGER.fine("Send request to clear");
                statementClear.execute(clearSql);

                if (!persons.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (Person person : persons) {
                        statement.setString(1, person.getName());
                        statement.setString(2, person.getSurname());
                        statement.setString(3, person.getPatronymic());
                        statement.setString(4, person.getPosition());
                        statement.execute();
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }
    }
}