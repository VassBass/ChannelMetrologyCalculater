package updater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.VariableConverter;
import measurements.Measurement;
import model.*;
import org.sqlite.JDBC;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToVersion5_3 {
    private static final Logger LOGGER = Logger.getLogger(ToVersion5_3.class.getName());

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

        this.rewriteChannelsToDB();
        this.rewriteSensorsToDB();
        this.rewritePersonsToDB();
        this.rewriteCalibratorsToDB();
        this.rewriteMeasurementsToDB();
        this.rewriteCPVToDB();
        this.rewriteDepartmentsToDB();
        this.rewriteAreasToDB();
        this.rewriteProcessesToDB();
        this.rewriteInstallationsToDB();
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        String dbUrl = "jdbc:sqlite:Support/Data.db";
        return DriverManager.getConnection(dbUrl);
    }

    private void createTables(){
        LOGGER.fine("Create SQL tables ...");

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
                + ", points text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ");";
        String sqlCreatePersonTable = "CREATE TABLE IF NOT EXISTS persons ("
                + "id integer NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", surname text NOT NULL"
                + ", patronymic text"
                + ", position text NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                + ");";

        LOGGER.fine("Create SQL tables GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            LOGGER.fine("Create SQL tables SEND REQUESTS");
            Statement statement = connection.createStatement();

            statement.execute(sqlCreateDepartmentTable);
            statement.execute(sqlCreateAreaTable);
            statement.execute(sqlCreateProcessTable);
            statement.execute(sqlCreateInstallationTable);
            statement.execute(sqlCreateSensorTable);
            statement.execute(sqlCreateCalibratorTable);
            statement.execute(sqlCreateChannelTable);
            statement.execute(sqlCreateCPVTable);
            statement.execute(sqlCreateMeasurementTable);
            statement.execute(sqlCreatePersonTable);

            LOGGER.fine("Create SQL tables CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Create SQL tables ERROR", ex);
        }

        LOGGER.info("Create SQL tables SUCCESS");
    }

    private void rewriteChannelsToDB(){
        LOGGER.fine("Rewrite channels ...");
        String sql = "INSERT INTO channels ("
                + "'code', 'name', 'department', 'area', 'process', 'installation', 'technology_number', 'protocol_number', 'reference'"
                + ", 'date', 'suitability', 'measurement', 'sensor', 'frequency', 'range_min', 'range_max'"
                + ", 'allowable_error_percent', 'allowable_error_value'"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        LOGGER.fine("Rewrite channels GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.info("Rewrite channels SEND REQUESTS");

            for (Channel channel : this.channels){
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

                String date = VariableConverter.dateToString(channel.getDate());
                String suitability = String.valueOf(channel.isSuitability());
                String measurement = writer.writeValueAsString(channel.getMeasurement());
                String sensor = writer.writeValueAsString(channel.getSensor());

                statement.setString(1, channel.getCode());
                statement.setString(2, channel.getName());
                statement.setString(3, channel.getDepartment());
                statement.setString(4, channel.getArea());
                statement.setString(5, channel.getProcess());
                statement.setString(6, channel.getInstallation());
                statement.setString(7, channel.getTechnologyNumber());
                statement.setString(8, channel.getNumberOfProtocol());
                statement.setString(9, channel.getReference());
                statement.setString(10, date);
                statement.setString(11, suitability);
                statement.setString(12, measurement);
                statement.setString(13, sensor);
                statement.setDouble(14, channel.getFrequency());
                statement.setDouble(15, channel.getRangeMin());
                statement.setDouble(16, channel.getRangeMax());
                statement.setDouble(17, channel.getAllowableErrorPercent());
                statement.setDouble(18, channel.getAllowableError());

                statement.execute();
            }

            LOGGER.fine("Rewrite channels CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException | JsonProcessingException ex){
            LOGGER.log(Level.SEVERE, "Rewrite channels ERROR", ex);
        }
        LOGGER.info("Rewrite channels SUCCESS");
    }

    private void rewriteSensorsToDB(){
        LOGGER.fine("Rewrite sensors ...");
        String sql = "INSERT INTO sensors ("
                + "'name', 'type', 'range_min', 'range_max', 'number', 'value', 'measurement', 'error_formula'"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        LOGGER.fine("Rewrite sensors GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite sensors SEND REQUESTS");

            for (Sensor sensor : this.sensors){
                statement.setString(1, sensor.getName());
                statement.setString(2, sensor.getType());
                statement.setDouble(3, sensor.getRangeMin());
                statement.setDouble(4, sensor.getRangeMax());
                statement.setString(5, sensor.getNumber());
                statement.setString(6, sensor.getValue());
                statement.setString(7, sensor.getMeasurement());
                statement.setString(8, sensor.getErrorFormula());

                statement.execute();
            }

            LOGGER.fine("Rewrite sensors CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite sensors ERROR", ex);
        }
        LOGGER.info("Rewrite sensors SUCCESS");
    }

    private void rewritePersonsToDB(){
        LOGGER.fine("Rewrite persons ...");
        String sql = "INSERT INTO persons ("
                + "'name', 'surname', 'patronymic', 'position'"
                + ") VALUES (?, ?, ?, ?)";

        LOGGER.fine("Rewrite persons GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite persons SEND REQUESTS");

            for (Worker person : this.persons){
                statement.setString(1, person.getName());
                statement.setString(2, person.getSurname());
                statement.setString(3, person.getPatronymic());
                statement.setString(4, person.getPosition());

                statement.execute();
            }

            LOGGER.fine("Rewrite persons CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite persons ERROR", ex);
        }
        LOGGER.info("Rewrite persons SUCCESS");
    }

    private void rewriteCalibratorsToDB(){
        LOGGER.fine("Rewrite calibrators ...");
        String sql = "INSERT INTO calibrators ("
                + "'name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max'"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        LOGGER.fine("Rewrite calibrators GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite calibrators SEND REQUESTS");

            for (Calibrator calibrator : this.calibrators){
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

                String certificate = writer.writeValueAsString(calibrator.getCertificate());

                statement.setString(1, calibrator.getName());
                statement.setString(2, calibrator.getType());
                statement.setString(3, calibrator.getNumber());
                statement.setString(4, calibrator.getMeasurement());
                statement.setString(5, calibrator.getValue());
                statement.setString(6, calibrator.getErrorFormula());
                statement.setString(7, certificate);
                statement.setDouble(8, calibrator.getRangeMin());
                statement.setDouble(9, calibrator.getRangeMax());

                statement.execute();
            }

            LOGGER.fine("Rewrite calibrators CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException | JsonProcessingException ex){
            LOGGER.log(Level.SEVERE, "Rewrite calibrators ERROR", ex);
        }
        LOGGER.info("Rewrite calibrators SUCCESS");
    }

    private void rewriteCPVToDB(){
        LOGGER.fine("Rewrite control points values ...");
        String sql = "INSERT INTO control_points ("
                + "'sensor_type', 'points', 'range_min', 'range_max'"
                + ") VALUES (?, ?, ?, ?)";

        LOGGER.fine("Rewrite control points values GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite control points values SEND REQUESTS");

            for (ControlPointsValues cpv : this.controlPointsValues){
                String points = VariableConverter.arrayToString(cpv.getValues());

                statement.setString(1, cpv.getSensorType());
                statement.setString(2, points);
                statement.setDouble(3, cpv.getRangeMin());
                statement.setDouble(4, cpv.getRangeMax());

                statement.execute();
            }

            LOGGER.fine("Rewrite control points values CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite control points values ERROR", ex);
        }
        LOGGER.info("Rewrite control points values SUCCESS");
    }

    private void rewriteMeasurementsToDB(){
        LOGGER.fine("Rewrite measurements ...");
        String sql = "INSERT INTO measurements ("
                + "'name', 'value'"
                + ") VALUES (?, ?)";

        LOGGER.fine("Rewrite measurements GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite measurements SEND REQUESTS");

            for (Measurement measurement : this.measurements){
                statement.setString(1, measurement.getName());
                statement.setString(2, measurement.getValue());

                statement.execute();
            }

            LOGGER.fine("Rewrite measurements CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite measurements ERROR", ex);
        }
        LOGGER.info("Rewrite measurements SUCCESS");
    }

    private void rewriteDepartmentsToDB(){
        LOGGER.fine("Rewrite departments ...");
        String sql = "INSERT INTO departments ("
                + "'department'"
                + ") VALUES (?)";

        LOGGER.fine("Rewrite departments GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite departments SEND REQUESTS");

            for (String department : this.departments){
                statement.setString(1, department);
                statement.execute();
            }

            LOGGER.fine("Rewrite departments CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite departments ERROR", ex);
        }
        LOGGER.info("Rewrite departments SUCCESS");
    }

    private void rewriteAreasToDB(){
        LOGGER.fine("Rewrite areas ...");
        String sql = "INSERT INTO areas ("
                + "'area'"
                + ") VALUES (?)";

        LOGGER.fine("Rewrite areas GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite areas SEND REQUESTS");

            for (String area : this.areas){
                statement.setString(1, area);
                statement.execute();
            }

            LOGGER.fine("Rewrite areas CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite areas ERROR", ex);
        }
        LOGGER.info("Rewrite areas SUCCESS");
    }

    private void rewriteProcessesToDB(){
        LOGGER.fine("Rewrite processes ...");
        String sql = "INSERT INTO processes ("
                + "'process'"
                + ") VALUES (?)";

        LOGGER.fine("Rewrite processes GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite processes SEND REQUESTS");

            for (String process : this.processes){
                statement.setString(1, process);
                statement.execute();
            }

            LOGGER.fine("Rewrite processes CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite processes ERROR", ex);
        }
        LOGGER.info("Rewrite processes SUCCESS");
    }

    private void rewriteInstallationsToDB(){
        LOGGER.fine("Rewrite installations ...");
        String sql = "INSERT INTO installations ("
                + "'installation'"
                + ") VALUES (?)";

        LOGGER.fine("Rewrite installations GET SQL CONNECTION");
        try (Connection connection = this.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            LOGGER.fine("Rewrite installations SEND REQUESTS");

            for (String installation : this.installations){
                statement.setString(1, installation);
                statement.execute();
            }

            LOGGER.fine("Rewrite installations CLOSE SQL CONNECTION");
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Rewrite installations ERROR", ex);
        }
        LOGGER.info("Rewrite installations SUCCESS");
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Channel> readChannels() {
        LOGGER.fine("Read channels ...");
        ArrayList<Channel>channels = null;
        String channelsFilePath = "Support/Lists/Channels.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(channelsFilePath))){
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
        LOGGER.fine("Read sensors ...");
        ArrayList<Sensor>sensors = null;
        String sensorsFilePath = "Support/Lists/Sensors.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(sensorsFilePath))){
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
        LOGGER.fine("Read persons ...");
        ArrayList<Worker>persons = null;
        String personsFilePath = "Support/Lists/Persons.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(personsFilePath))){
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
        LOGGER.fine("Read calibrators ...");
        ArrayList<Calibrator>calibrators = null;
        String calibratorsFilePath = "Support/Lists/Calibrators.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(calibratorsFilePath))){
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
        LOGGER.fine("Read measurements ...");
        ArrayList<Measurement>measurements = null;
        String measurementsFilePath = "Support/Lists/Measurements.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(measurementsFilePath))){
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
        LOGGER.fine("Read controlPointsValues ...");
        ArrayList<ControlPointsValues>controlPointsValues = null;
        String controlPointsValuesFilePath = "Support/Lists/control_points_values.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(controlPointsValuesFilePath))){
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
        LOGGER.fine("Read departments ...");
        ArrayList<String>departments = null;
        String departmentsFilePath = "Support/Lists/Departments.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(departmentsFilePath))){
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
        LOGGER.fine("Read areas ...");
        ArrayList<String>areas = null;
        String areasFilePath = "Support/Lists/Areas.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(areasFilePath))){
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
        LOGGER.fine("Read processes ...");
        ArrayList<String>processes = null;
        String processesFilePath = "Support/Lists/Processes.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(processesFilePath))){
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
        LOGGER.fine("Read installations ...");
        ArrayList<String>installations = null;
        String installationsFilePath = "Support/Lists/Installations.dat";
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(installationsFilePath))){
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