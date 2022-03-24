package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import constants.WorkPositions;
import model.Person;
import org.sqlite.JDBC;
import repository.PersonRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepositoryImpl extends Repository<Person> implements PersonRepository {
    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());

    private static final String EMPTY_ARRAY = "<Порожньо>";

    public PersonRepositoryImpl(){super();}
    public PersonRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS persons ("
                    + "id integer NOT NULL UNIQUE"
                    + ", name text NOT NULL"
                    + ", surname text NOT NULL"
                    + ", patronymic text"
                    + ", position text NOT NULL"
                    + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request");
            sql = "SELECT * FROM persons";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));
                this.mainList.add(person);
            }

            LOGGER.fine("Close connection");
            resultSet.close();
            statement.close();
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
    public String[] getAllNames() {
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
    public String[] getNamesOfHeads() {
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
                && this.mainList.contains(oldPerson) && !this.mainList.contains(newPerson)) {
            int index = this.mainList.indexOf(oldPerson);
            this.mainList.set(index, newPerson);
            new BackgroundAction().set(oldPerson, newPerson);
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
    public void export() {
        new BackgroundAction().export(this.mainList);
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

        void export(ArrayList<Person>persons){
            this.list = persons;
            this.action = Action.EXPORT;
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
            this.execute();
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addPerson(this.person);
                case REMOVE:
                    return this.removePerson(this.person.getId());
                case CLEAR:
                    return this.clearPersons();
                case SET:
                    return this.setPerson(this.old, this.person);
                case REWRITE:
                    return this.rewritePersons(this.list);
                case EXPORT:
                    return this.exportPersons(this.list);
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
                    JOptionPane.showMessageDialog(Application.context.mainScreen, message, "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addPerson(Person person){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM persons WHERE id = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, person.getId());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO persons ('id', 'surname', 'name', 'patronymic', 'position') "
                        + "VALUES(?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, person.getId());
                statement.setString(2, person.getSurname());
                statement.setString(3, person.getName());
                statement.setString(4, person.getPatronymic());
                statement.setString(5, person.getPosition());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removePerson(int id){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM persons WHERE id = '" + id + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean clearPersons(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM persons;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        boolean setPerson(Person oldPerson, Person newPerson){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM persons WHERE id = '" + oldPerson.getId() + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO areas ('id', 'name', 'surname', 'patronymic', 'position')" +
                        " VALUES (?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, newPerson.getId());
                statement.setString(2, newPerson.getName());
                statement.setString(3, newPerson.getSurname());
                statement.setString(4, newPerson.getPatronymic());
                statement.setString(5, newPerson.getPosition());
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        public boolean rewritePersons(ArrayList<Person>persons){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM persons;";
                statementClear.execute(sql);

                if (!persons.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO persons ('id', 'name', 'surname', 'patronymic', 'position')" +
                            " VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (Person person : persons) {
                        statement.setInt(1, person.getId());
                        statement.setString(2, person.getName());
                        statement.setString(3, person.getSurname());
                        statement.setString(4, person.getPatronymic());
                        statement.setString(5, person.getPosition());
                        statement.execute();
                    }

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean exportPersons(ArrayList<Person>persons){
            Calendar date = Calendar.getInstance();
            String fileName = "export_persons ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS persons ("
                    + "id integer NOT NULL UNIQUE"
                    + ", name text NOT NULL"
                    + ", surname text NOT NULL"
                    + ", patronymic text"
                    + ", position text NOT NULL"
                    + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                    + ");";

            Connection connection = null;
            Statement statement = null;
            PreparedStatement preparedStatement = null;
            try {
                LOGGER.fine("Get connection with DB");
                DriverManager.registerDriver(new JDBC());
                connection = DriverManager.getConnection(dbUrl);
                statement = connection.createStatement();

                LOGGER.fine("Send requests to create table");
                statement.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO persons ('id', 'name', 'surname', 'patronymic', 'position')" +
                        " VALUES (?, ?, ?, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                for (Person person : persons) {
                    preparedStatement.setInt(1, person.getId());
                    preparedStatement.setString(2, person.getName());
                    preparedStatement.setString(3, person.getSurname());
                    preparedStatement.setString(4, person.getPatronymic());
                    preparedStatement.setString(5, person.getPosition());
                    preparedStatement.execute();
                }

                LOGGER.fine("Close connection");
                statement.close();
                preparedStatement.close();
                connection.close();
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
                try {
                    if (statement != null) statement.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ignored) {}
                return false;
            }
        }
    }
}