package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import constants.Action;
import model.Measurement;
import model.Calibrator;
import org.sqlite.JDBC;
import repository.CalibratorRepository;
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

public class CalibratorRepositoryImpl extends Repository<Calibrator> implements CalibratorRepository {
    private static final Logger LOGGER = Logger.getLogger(CalibratorRepository.class.getName());

    public CalibratorRepositoryImpl(){super();}
    public CalibratorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS calibrators ("
                    + "name text NOT NULL UNIQUE"
                    + ", type text NOT NULL"
                    + ", number text NOT NULL"
                    + ", measurement text NOT NULL"
                    + ", value text NOT NULL"
                    + ", error_formula text NOT NULL"
                    + ", certificate text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"name\")"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read calibrators from DB");
            sql = "SELECT * FROM calibrators";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Calibrator calibrator = new Calibrator();
                calibrator.setName(resultSet.getString("name"));
                calibrator.setType(resultSet.getString("type"));
                calibrator.setNumber(resultSet.getString("number"));
                calibrator.setMeasurement(resultSet.getString("measurement"));
                calibrator.setValue(resultSet.getString("value"));
                calibrator.setErrorFormula(resultSet.getString("error_formula"));
                String certificateString = resultSet.getString("certificate");
                calibrator.setCertificate(Calibrator.Certificate.fromString(certificateString));
                calibrator.setRangeMin(resultSet.getDouble("range_min"));
                calibrator.setRangeMax(resultSet.getDouble("range_max"));
                this.mainList.add(calibrator);
            }

            LOGGER.fine("Close connection");
            resultSet.close();
            statement.close();
        } catch (SQLException | JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        return this.mainList;
    }

    @Override
    public String[] getAllNames(Measurement measurement) {
        ArrayList<String>cal = new ArrayList<>();
        for (Calibrator c : this.mainList){
            if (c.getMeasurement().equals(measurement.getName())){
                cal.add(c.getName());
            }
        }
        return cal.toArray(new String[0]);
    }

    @Override
    public Calibrator get(String name) {
        Calibrator calibrator = new Calibrator();
        calibrator.setName(name);
        int index = this.mainList.indexOf(calibrator);
        return index >= 0 ? this.mainList.get(index) : null;
    }

    @Override
    public Calibrator get(int index) {
        return index < 0 | index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(Calibrator calibrator) {
        if (calibrator != null && !this.mainList.contains(calibrator)) {
            this.mainList.add(calibrator);
            new BackgroundAction().add(calibrator);
        }
    }

    @Override
    public void addInCurrentThread(Calibrator calibrator) {
        if (calibrator != null && !this.mainList.contains(calibrator)) {
            this.mainList.add(calibrator);
            new BackgroundAction().addCalibrator(calibrator);
        }
    }

    @Override
    public void remove(Calibrator calibrator) {
        if (calibrator != null && this.mainList.remove(calibrator)){
            new BackgroundAction().remove(calibrator);
        }
    }

    @Override
    public void remove(int index) {
        if (index >= 0 && index < this.mainList.size()){
            new BackgroundAction().remove(this.mainList.get(index));
            this.mainList.remove(index);
        }
    }

    @Override
    public void set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null && newCalibrator != null
                && this.mainList.contains(oldCalibrator) && !this.mainList.contains(newCalibrator)){
            int index = this.mainList.indexOf(oldCalibrator);
            this.mainList.set(index, newCalibrator);
            new BackgroundAction().set(oldCalibrator, newCalibrator);
        }
    }

    @Override
    public void setInCurrentThread(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null && newCalibrator != null
                && this.mainList.contains(oldCalibrator) && !this.mainList.contains(newCalibrator)){
            int index = this.mainList.indexOf(oldCalibrator);
            this.mainList.set(index, newCalibrator);
            new BackgroundAction().setCalibrator(oldCalibrator, newCalibrator);
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator> calibrators) {
        if (calibrators != null && !calibrators.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(calibrators);
            new BackgroundAction().rewriteCalibrators(calibrators);
        }
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.mainList);
    }

    @Override
    public void rewrite(ArrayList<Calibrator> calibrators) {
        if (calibrators != null && !calibrators.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(calibrators);
            new BackgroundAction().rewrite(calibrators);
        }
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private Calibrator calibrator, old;
        private ArrayList<Calibrator>list;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(Calibrator calibrator){
            this.calibrator = calibrator;
            this.action = Action.ADD;
            this.start();
        }

        void remove(Calibrator calibrator){
            this.calibrator = calibrator;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<Calibrator>list){
            this.list = list;
            this.action = list == null ? Action.CLEAR : Action.REWRITE;
            this.start();
        }

        void set(Calibrator oldCalibrator, Calibrator newCalibrator){
            this.old = oldCalibrator;
            this.calibrator = newCalibrator;
            this.action = Action.SET;
            this.start();
        }

        void export(ArrayList<Calibrator>calibrators){
            this.list = calibrators;
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
                    return this.addCalibrator(this.calibrator);
                case REMOVE:
                    return this.removeCalibrator(this.calibrator);
                case CLEAR:
                    return this.clearCalibrators();
                case SET:
                    return this.setCalibrator(this.old, this.calibrator);
                case REWRITE:
                    return this.rewriteCalibrators(this.list);
                case EXPORT:
                    return this.exportCalibrators(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            mainList.remove(this.calibrator);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.calibrator)) mainList.add(this.calibrator);
                            break;
                        case SET:
                            mainList.remove(this.calibrator);
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

        boolean addCalibrator(Calibrator calibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){

                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, calibrator.getName());
                statement.setString(2, calibrator.getType());
                statement.setString(3, calibrator.getNumber());
                statement.setString(4, calibrator.getMeasurement());
                statement.setString(5, calibrator.getValue());
                statement.setString(6, calibrator.getErrorFormula());
                statement.setString(7, calibrator.getCertificate().toString());
                statement.setDouble(8, calibrator.getRangeMin());
                statement.setDouble(9, calibrator.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removeCalibrator(Calibrator calibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM calibrators WHERE name = '" + calibrator.getName() + "';";
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

        private boolean clearCalibrators(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM calibrators;";
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

        boolean setCalibrator(Calibrator oldCalibrator, Calibrator newCalibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM calibrators WHERE name = '" + oldCalibrator.getName() + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newCalibrator.getName());
                statement.setString(2, newCalibrator.getType());
                statement.setString(3, newCalibrator.getNumber());
                statement.setString(4, newCalibrator.getMeasurement());
                statement.setString(5, newCalibrator.getValue());
                statement.setString(6, newCalibrator.getErrorFormula());
                statement.setString(7, newCalibrator.getCertificate().toString());
                statement.setDouble(8, newCalibrator.getRangeMin());
                statement.setDouble(9, newCalibrator.getRangeMax());
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

        public boolean rewriteCalibrators(ArrayList<Calibrator>calibrators){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM calibrators;";
                statementClear.execute(sql);

                if (!calibrators.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (Calibrator calibrator : calibrators) {
                        statement.setString(1, calibrator.getName());
                        statement.setString(2, calibrator.getType());
                        statement.setString(3, calibrator.getNumber());
                        statement.setString(4, calibrator.getMeasurement());
                        statement.setString(5, calibrator.getValue());
                        statement.setString(6, calibrator.getErrorFormula());
                        statement.setString(7, calibrator.getCertificate().toString());
                        statement.setDouble(8, calibrator.getRangeMin());
                        statement.setDouble(9, calibrator.getRangeMax());
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

        private boolean exportCalibrators(ArrayList<Calibrator>calibrators){
            Calendar date = Calendar.getInstance();
            String fileName = "export_calibrators ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS calibrators ("
                    + "name text NOT NULL UNIQUE"
                    + ", type text NOT NULL"
                    + ", number text NOT NULL"
                    + ", measurement text NOT NULL"
                    + ", value text NOT NULL"
                    + ", error_formula text NOT NULL"
                    + ", certificate text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"name\")"
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
                sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                for (Calibrator calibrator : calibrators) {
                    preparedStatement.setString(1, calibrator.getName());
                    preparedStatement.setString(2, calibrator.getType());
                    preparedStatement.setString(3, calibrator.getNumber());
                    preparedStatement.setString(4, calibrator.getMeasurement());
                    preparedStatement.setString(5, calibrator.getValue());
                    preparedStatement.setString(6, calibrator.getErrorFormula());
                    preparedStatement.setString(7, calibrator.getCertificate().toString());
                    preparedStatement.setDouble(8, calibrator.getRangeMin());
                    preparedStatement.setDouble(9, calibrator.getRangeMax());
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