package updater;

import measurements.Measurement;
import model.*;
import org.sqlite.JDBC;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToVersion5_3 {
    private static final Logger LOGGER = Logger.getLogger(ToVersion5_3.class.getName());

    private final String channelsFilePath = "Support/Lists/Channels.dat";
    private final String sensorsFilePath = "Support/Lists/Sensors.dat";
    private final String personsFilePath = "Support/Lists/Persons.dat";
    private final String calibratorsFilePath = "Support/Lists/Calibrators.dat";
    private final String measurementsFilePath = "Support/Lists/Measurements.dat";
    private final String controlPointsValuesFilePath = "Support/Lists/control_points_values.dat";
    private final String departmentsFilePath = "Support/Lists/Departments.dat";
    private final String areasFilePath = "Support/Lists/Areas.dat";
    private final String processesFilePath = "Support/Lists/Processes.dat";
    private final String installationsFilePath = "Support/Lists/Installations.dat";
    private final String dbUrl = "jdbc:sqlite:Support/Data.db";

    private ArrayList<Channel>channels = new ArrayList<>();
    private ArrayList<Sensor>sensors = new ArrayList<>();
    private ArrayList<Worker>persons = new ArrayList<>();
    private ArrayList<Calibrator>calibrators = new ArrayList<>();
    private ArrayList<Measurement>measurements = new ArrayList<>();
    private ArrayList<ControlPointsValues>controlPointsValues = new ArrayList<>();
    private ArrayList<String>departments = new ArrayList<>();
    private ArrayList<String>areas = new ArrayList<>();
    private ArrayList<String>processes = new ArrayList<>();
    private ArrayList<String>installations = new ArrayList<>();

    public void update(){
        this.channels = this.readChannels();
        this.sensors = this.readSensors();
        this.persons = this.readPersons();
        this.calibrators = this.readCalibrators();
        this.measurements = this.readMeasurements();
        this.controlPointsValues = this.readControlPointsValues();
        this.departments = this.readDepartments();
        this.areas = this.readAreas();
        this.processes = this.readProcesses();
        this.installations = this.readInstallations();

        this.createTables();
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        return DriverManager.getConnection(this.dbUrl);
    }

    private void createTables(){
        LOGGER.info("Create SQL tables ...");

        String sqlCreateDepartmentTable = "CREATE TABLE IF NOT EXISTS departments ("
                + "department text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"department\")"
                + ");";
        String sqlCreateAreaTable = "CREATE TABLE IF NOT EXISTS areas ("
                + "area text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"area\")"
                + ");";
        String sqlCreateProcessTable = "CREATE TABLE IF NOT EXISTS processes ("
                + "process text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"process\")"
                + ");";
        String sqlCreateInstallationTable = "CREATE TABLE IF NOT EXISTS installations ("
                + "installation text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"installation\")"
                + ");";
        String sqlCreateSensorTable = "CREATE TABLE IF NOT EXISTS sensors ("
                + "name text NOT NULL UNIQUE"
                + ", type text NOT NULL"
                + ", range_min real"
                + ", range_max real"
                + ", number text"
                + ", value text"
                + ", measurement text NOT NULL"
                + ", error_formula text NOT NULL"
                + ", PRIMARY KEY (\"name\")"
                + ");";
        String sqlCreateMeasurementTable = "CREATE TABLE IF NOT EXISTS measurements ("
                + "name text NOT NULL"
                + ", value text NOT NULL"
                + ");";
        String sqlCreateCalibratorTable = "CREATE TABLE IF NOT EXISTS calibrators ("
                + "name text NOT NULL UNIQUE"
                + ", type text NOT NULL"
                + ", number text NOT NULL"
                + ", measurement text NOT NULL"
                + ", value text"
                + ", error_formula text NOT NULL"
                + ", certificate text NOT NULL"
                + ", range_min real"
                + ", range_max real"
                + ", PRIMARY KEY (\"name\")"
                + ");";
        String sqlCreateChannelTable = "CREATE TABLE IF NOT EXISTS channels ("
                + "code text NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", department text"
                + ", area text"
                + ", process text"
                + ", installation text"
                + ", technology_number text"
                + ", protocol_number text"
                + ", reference text"
                + ", date text NOT NULL"
                + ", suitability text NOT NULL"
                + ", measurement text NOT NULL"
                + ", sensor text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real"
                + ", range_max real"
                + ", allowable_error_percent real"
                + ", allowable_error_value real"
                + ", PRIMARY KEY (\"code\")"
                + ");";
        String sqlCreateCPVTable = "CREATE TABLE IF NOT EXISTS control_points ("
                + "sensor_type text NOT NULL"
                + ", values text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ");";

        LOGGER.info("Create SQL tables GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            LOGGER.info("Create SQL tables SEND REQUESTS");
            Statement statement = connection.createStatement();

            statement.execute(sqlCreateDepartmentTable);
            statement.execute(sqlCreateAreaTable);
            statement.execute(sqlCreateProcessTable);
            statement.execute(sqlCreateInstallationTable);
            statement.execute(sqlCreateSensorTable);

            LOGGER.info("Create SQL tables CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Create SQL tables ERROR", ex);
        }

        LOGGER.info("Create SQL tables SUCCESS");
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Channel> readChannels() {
        LOGGER.info("Read channels ...");
        ArrayList<Channel>channels = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.channelsFilePath))){
            channels = (ArrayList<Channel>) reader.readObject();
            if (channels != null) {
                LOGGER.info("Read channels SUCCESS");
            }else {
                LOGGER.info("Read channels FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read channels ERROR", ex);
        }
        return channels;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Sensor> readSensors() {
        LOGGER.info("Read sensors ...");
        ArrayList<Sensor>sensors = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.sensorsFilePath))){
            sensors = (ArrayList<Sensor>) reader.readObject();
            if (sensors != null) {
                LOGGER.info("Read sensors SUCCESS");
            }else {
                LOGGER.info("Read sensors FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read sensors ERROR", ex);
        }
        return sensors;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Worker> readPersons() {
        LOGGER.info("Read persons ...");
        ArrayList<Worker>persons = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.personsFilePath))){
            persons = (ArrayList<Worker>) reader.readObject();
            if (persons != null) {
                LOGGER.info("Read persons SUCCESS");
            }else {
                LOGGER.info("Read persons FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read persons ERROR", ex);
        }
        return persons;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Calibrator> readCalibrators() {
        LOGGER.info("Read calibrators ...");
        ArrayList<Calibrator>calibrators = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.calibratorsFilePath))){
            calibrators = (ArrayList<Calibrator>) reader.readObject();
            if (calibrators != null) {
                LOGGER.info("Read calibrators SUCCESS");
            }else {
                LOGGER.info("Read calibrators FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read calibrators ERROR", ex);
        }
        return calibrators;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Measurement> readMeasurements() {
        LOGGER.info("Read measurements ...");
        ArrayList<Measurement>measurements = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.measurementsFilePath))){
            measurements = (ArrayList<Measurement>) reader.readObject();
            if (measurements != null) {
                LOGGER.info("Read measurements SUCCESS");
            }else {
                LOGGER.info("Read measurements FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read measurements ERROR", ex);
        }
        return measurements;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<ControlPointsValues> readControlPointsValues() {
        LOGGER.info("Read controlPointsValues ...");
        ArrayList<ControlPointsValues>controlPointsValues = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.controlPointsValuesFilePath))){
            controlPointsValues = (ArrayList<ControlPointsValues>) reader.readObject();
            if (controlPointsValues != null) {
                LOGGER.info("Read controlPointsValues SUCCESS");
            }else {
                LOGGER.info("Read controlPointsValues FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read controlPointsValues ERROR", ex);
        }
        return controlPointsValues;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> readDepartments() {
        LOGGER.info("Read departments ...");
        ArrayList<String>departments = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.departmentsFilePath))){
            departments = (ArrayList<String>) reader.readObject();
            if (departments != null) {
                LOGGER.info("Read departments SUCCESS");
            }else {
                LOGGER.info("Read departments FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read departments ERROR", ex);
        }
        return departments;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> readAreas() {
        LOGGER.info("Read areas ...");
        ArrayList<String>areas = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.areasFilePath))){
            areas = (ArrayList<String>) reader.readObject();
            if (areas != null) {
                LOGGER.info("Read areas SUCCESS");
            }else {
                LOGGER.info("Read areas FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read areas ERROR", ex);
        }
        return areas;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> readProcesses() {
        LOGGER.info("Read processes ...");
        ArrayList<String>processes = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.processesFilePath))){
            processes = (ArrayList<String>) reader.readObject();
            if (processes != null) {
                LOGGER.info("Read processes SUCCESS");
            }else {
                LOGGER.info("Read processes FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read processes ERROR", ex);
        }
        return processes;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> readInstallations() {
        LOGGER.info("Read installations ...");
        ArrayList<String>installations = null;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(this.installationsFilePath))){
            installations = (ArrayList<String>) reader.readObject();
            if (installations != null) {
                LOGGER.info("Read installations SUCCESS");
            }else {
                LOGGER.info("Read installations FILE EMPTY");
            }
        }catch (IOException | ClassNotFoundException ex){
            LOGGER.log(Level.SEVERE, "Read installations ERROR", ex);
        }
        return installations;
    }
}
