package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import constants.Action;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalibratorRepositoryImpl extends Repository implements CalibratorRepository {
    private static final Logger LOGGER = Logger.getLogger(CalibratorRepository.class.getName());

    public CalibratorRepositoryImpl(){super();}
    public CalibratorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
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
    public ArrayList<Calibrator> getAll() {
        ArrayList<Calibrator>calibrators = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM calibrators";
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
                Calibrator.Certificate certificate = new ObjectMapper().readValue(certificateString, Calibrator.Certificate.class);
                calibrator.setCertificate(certificate);
                calibrator.setRangeMin(resultSet.getDouble("range_min"));
                calibrator.setRangeMax(resultSet.getDouble("range_max"));
                calibrators.add(calibrator);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException | JsonProcessingException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return calibrators;
    }

    @Override
    public void add(Calibrator calibrator) {
        new BackgroundAction().add(calibrator);
    }

    @Override
    public void remove(String calibratorName) {
        new BackgroundAction().remove(calibratorName);
    }

    @Override
    public void set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        new BackgroundAction().set(oldCalibrator, newCalibrator);
    }

    @Override
    public void clear() {
        new BackgroundAction().clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator> calibrators) {
        new BackgroundAction().rewriteCalibrators(calibrators);
    }

    @Override
    public void export(ArrayList<Calibrator>calibrators) {
        new BackgroundAction().export(calibrators);
    }

    @Override
    public void rewrite(ArrayList<Calibrator> calibrators) {
        new BackgroundAction().rewrite(calibrators);
    }

    private class BackgroundAction extends SwingWorker<Void, Void> {
        private Calibrator calibrator, old;
        private String calibratorName;
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

        void remove(String calibratorName){
            this.calibratorName = calibratorName;
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
        protected Void doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    this.addCalibrator(this.calibrator);
                    break;
                case REMOVE:
                    this.removeCalibrator(this.calibratorName);
                    break;
                case CLEAR:
                    this.clearCalibrators();
                    break;
                case SET:
                    this.setCalibrator(this.old, this.calibrator);
                    break;
                case REWRITE:
                    this.rewriteCalibrators(this.list);
                    break;
                case EXPORT:
                    this.exportCalibrators(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addCalibrator(Calibrator calibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM calibrators WHERE name = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, calibrator.getName());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, calibrator.getName());
                statement.setString(2, calibrator.getType());
                statement.setString(3, calibrator.getNumber());
                statement.setString(4, calibrator.getMeasurement());
                statement.setString(5, calibrator.getValue());
                statement.setString(6, calibrator.getErrorFormula());
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String certificate = writer.writeValueAsString(calibrator.getCertificate());
                statement.setString(7, certificate);
                statement.setDouble(8, calibrator.getRangeMin());
                statement.setDouble(9, calibrator.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeCalibrator(String calibratorName){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM calibrators WHERE name = '" + calibratorName + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearCalibrators(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM calibrators;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void setCalibrator(Calibrator oldCalibrator, Calibrator newCalibrator){
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
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String certificate = writer.writeValueAsString(newCalibrator.getCertificate());
                statement.setString(7, certificate);
                statement.setDouble(8, newCalibrator.getRangeMin());
                statement.setDouble(9, newCalibrator.getRangeMax());
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void rewriteCalibrators(ArrayList<Calibrator>calibrators){
            if (calibrators != null) {
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
                            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            String certificate = writer.writeValueAsString(calibrator.getCertificate());
                            statement.setString(7, certificate);
                            statement.setDouble(8, calibrator.getRangeMin());
                            statement.setDouble(9, calibrator.getRangeMax());
                            statement.execute();
                        }

                        LOGGER.fine("Close connections");
                        statementClear.close();
                        statement.close();
                    }
                } catch (SQLException | JsonProcessingException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
        }

        private void exportCalibrators(ArrayList<Calibrator>calibrators){
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
                    ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String certificate = writer.writeValueAsString(calibrator.getCertificate());
                    preparedStatement.setString(7, certificate);
                    preparedStatement.setDouble(8, calibrator.getRangeMin());
                    preparedStatement.setDouble(9, calibrator.getRangeMax());
                    preparedStatement.execute();
                }

                LOGGER.fine("Close connection");
                statement.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException | JsonProcessingException ex) {
                LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
                try {
                    if (statement != null) statement.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
