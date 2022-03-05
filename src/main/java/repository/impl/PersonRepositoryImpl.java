package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import model.Person;
import repository.PersonRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepositoryImpl extends Repository implements PersonRepository {
    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());

    public PersonRepositoryImpl(){super();}
    public PersonRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        String sql = "CREATE TABLE IF NOT EXISTS persons ("
                + "id integer NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", surname text NOT NULL"
                + ", patronymic text"
                + ", position text NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                + ");";

        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            statement.execute(sql);

            LOGGER.fine("Close connection");
            statement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Person> getAll() {
        ArrayList<Person>persons = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM persons";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));
                persons.add(person);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return persons;
    }

    @Override
    public void add(Person person) {
        new BackgroundAction().add(person);
    }

    @Override
    public void remove(Person person) {
        new BackgroundAction().remove(person);
    }

    @Override
    public void set(Person oldPerson, Person newPerson) {
        new BackgroundAction().set(oldPerson, newPerson);
    }

    @Override
    public void clear() {
        new BackgroundAction().clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Person> persons) {
        if (persons != null) {
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM persons;";
                statementClear.execute(sql);

                if (!persons.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO areas ('id', 'name', 'surname', 'patronymic', 'position')" +
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
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void rewrite(ArrayList<Person> persons) {
        new BackgroundAction().rewrite(persons);
    }

    private class BackgroundAction extends SwingWorker<Void, Void> {
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
            this.execute();
        }

        @Override
        protected Void doInBackground() throws Exception {
            String sql = null;
            switch (this.action){
                case ADD:
                    sql = "REPLACE INTO persons (id, surname, name, patronymic, position) "
                            + "VALUES("
                            + "'" + this.person.getId() + "'"
                            + ", '" + this.person.getSurname() + "'"
                            + ", '" + this.person.getName() + "'"
                            + ", '" + this.person.getPatronymic() + "'"
                            + ", '" + this.person.getPosition() + "'"
                            + ");";
                    break;
                case REMOVE:
                    sql = "DELETE FROM persons WHERE id = '" + this.person.getId() + "';";
                    break;
                case CLEAR:
                    sql = "DELETE FROM persons;";
                    break;
            }
            if (sql != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    Statement statement = connection.createStatement();

                    LOGGER.fine("Send request");
                    statement.execute(sql);

                    LOGGER.fine("Close connections");
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }else if (this.list != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()){
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    sql = "DELETE FROM persons;";
                    statementClear.execute(sql);

                    if (!this.list.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO areas ('id', 'name', 'surname', 'patronymic', 'position')" +
                                " VALUES (?, ?, ?, ?, ?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (Person person : this.list) {
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
                }catch (SQLException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }else if (this.old != null){
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()){
                    LOGGER.fine("Send request to delete");
                    Statement statementClear = connection.createStatement();
                    sql = "DELETE FROM persons WHERE id = '" + this.old.getId() + "';";
                    statementClear.execute(sql);

                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO areas ('id', 'name', 'surname', 'patronymic', 'position')" +
                    " VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, this.person.getId());
                    statement.setString(2, this.person.getName());
                    statement.setString(3, this.person.getSurname());
                    statement.setString(4, this.person.getPatronymic());
                    statement.setString(5, this.person.getPosition());
                    statement.execute(sql);

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }catch (SQLException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }
    }
}
