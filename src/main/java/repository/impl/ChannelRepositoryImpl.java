package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import constants.Action;
import converters.VariableConverter;
import measurements.Measurement;
import model.Channel;
import model.Sensor;
import org.sqlite.JDBC;
import repository.ChannelRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelRepositoryImpl extends Repository implements ChannelRepository {
    private static final Logger LOGGER = Logger.getLogger(ChannelRepository.class.getName());

    public ChannelRepositoryImpl(){super();}
    public ChannelRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        String sql = "CREATE TABLE IF NOT EXISTS channels ("
                + "code text NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", department text"
                + ", area text"
                + ", process text"
                + ", installation text"
                + ", technology_number text NOT NULL"
                + ", protocol_number text"
                + ", reference text"
                + ", date text"
                + ", suitability text NOT NULL"
                + ", measurement text NOT NULL"
                + ", sensor text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", allowable_error_percent real NOT NULL"
                + ", allowable_error_value real NOT NULL"
                + ", PRIMARY KEY (\"code\")"
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
    public ArrayList<Channel> getAll() {
        ArrayList<Channel>channels = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM channels";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Channel channel = new Channel();
                channel.setCode(resultSet.getString("code"));
                channel.setName(resultSet.getString("name"));
                channel.setDepartment(resultSet.getString("department"));
                channel.setArea(resultSet.getString("area"));
                channel.setProcess(resultSet.getString("process"));
                channel.setInstallation(resultSet.getString("installation"));
                channel.setTechnologyNumber(resultSet.getString("technology_number"));
                channel.setNumberOfProtocol(resultSet.getString("protocol_number"));
                channel.setReference(resultSet.getString("reference"));
                channel.setDate(VariableConverter.stringToDate(resultSet.getString("date")));
                channel.setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")));
                String measurementString = resultSet.getString("measurement");
                Measurement measurement = new ObjectMapper().readValue(measurementString, Measurement.class);
                channel.setMeasurement(measurement);
                String sensorString = resultSet.getString("sensor");
                Sensor sensor = new ObjectMapper().readValue(sensorString, Sensor.class);
                channel.setSensor(sensor);
                channel.setFrequency(resultSet.getDouble("frequency"));
                channel.setRangeMin(resultSet.getDouble("range_min"));
                channel.setRangeMax(resultSet.getDouble("range_max"));
                double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
                channels.add(channel);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException | JsonProcessingException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return channels;
    }

    @Override
    public void add(Channel channel) {
        new BackgroundAction().add(channel);
    }

    @Override
    public void remove(String channelCode) {
        new BackgroundAction().remove(channelCode);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel> channels) {
        new BackgroundAction().rewriteChannels(channels);
    }

    @Override
    public void set(Channel oldChannel, Channel newChannel) {
        new BackgroundAction().set(oldChannel, newChannel);
    }

    @Override
    public void clear() {
        new BackgroundAction().clear();
    }

    @Override
    public void export(ArrayList<Channel> channels) {
        new BackgroundAction().export(channels);
    }

    private class BackgroundAction extends SwingWorker<Void, Void> {
        private Channel channel, old;
        private String channelCode;
        private ArrayList<Channel>list;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(Channel channel){
            this.channel = channel;
            this.action = Action.ADD;
            this.start();
        }

        void remove(String channelCode){
            this.channelCode = channelCode;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void set(Channel oldChannel, Channel newChannel){
            this.old = oldChannel;
            this.channel = newChannel;
            this.action = Action.SET;
            this.start();
        }

        void export(ArrayList<Channel>channels){
            this.list = channels;
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
                    this.addChannel(this.channel);
                    break;
                case REMOVE:
                    this.removeChannel(this.channelCode);
                    break;
                case CLEAR:
                    this.clearChannels();
                    break;
                case SET:
                    this.setChannel(this.old, this.channel);
                    break;
                case EXPORT:
                    this.exportChannels(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addChannel(Channel channel){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM channels WHERE code = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, channel.getCode());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                        ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement', 'sensor', 'frequency', 'range_min', 'range_max'" +
                        ", 'allowable_error_percent', 'allowable_error_value') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, channel.getCode());
                statement.setString(2, channel.getName());
                statement.setString(3, channel.getDepartment());
                statement.setString(4, channel.getArea());
                statement.setString(5, channel.getProcess());
                statement.setString(6, channel.getInstallation());
                statement.setString(7, channel.getTechnologyNumber());
                statement.setString(8, channel.getNumberOfProtocol());
                statement.setString(9,channel.getReference());
                statement.setString(10, VariableConverter.dateToString(channel.getDate()));
                statement.setString(11, String.valueOf(channel.isSuitability()));
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String measurement = writer.writeValueAsString(channel.getMeasurement());
                statement.setString(12, measurement);
                String sensor = writer.writeValueAsString(channel.getSensor());
                statement.setString(13, sensor);
                statement.setDouble(14, channel.getFrequency());
                statement.setDouble(15, channel.getRangeMin());
                statement.setDouble(16, channel.getRangeMax());
                statement.setDouble(17, channel.getAllowableErrorPercent());
                statement.setDouble(18, channel.getAllowableError());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeChannel(String channelCode){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM channels WHERE code = '" + channelCode + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearChannels(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM channels;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void setChannel(Channel oldChannel, Channel newChannel){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM channels WHERE code = '" + oldChannel.getCode() + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                        ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement', 'sensor', 'frequency', 'range_min', 'range_max'" +
                        ", 'allowable_error_percent', 'allowable_error_value') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newChannel.getCode());
                statement.setString(2, newChannel.getName());
                statement.setString(3, newChannel.getDepartment());
                statement.setString(4, newChannel.getArea());
                statement.setString(5, newChannel.getProcess());
                statement.setString(6, newChannel.getInstallation());
                statement.setString(7, newChannel.getTechnologyNumber());
                statement.setString(8, newChannel.getNumberOfProtocol());
                statement.setString(9,newChannel.getReference());
                statement.setString(10, VariableConverter.dateToString(newChannel.getDate()));
                statement.setString(11, String.valueOf(newChannel.isSuitability()));
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String measurement = writer.writeValueAsString(newChannel.getMeasurement());
                statement.setString(12, measurement);
                String sensor = writer.writeValueAsString(newChannel.getSensor());
                statement.setString(13, sensor);
                statement.setDouble(14, newChannel.getFrequency());
                statement.setDouble(15, newChannel.getRangeMin());
                statement.setDouble(16, newChannel.getRangeMax());
                statement.setDouble(17, newChannel.getAllowableErrorPercent());
                statement.setDouble(18, newChannel.getAllowableError());
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void rewriteChannels(ArrayList<Channel>channels){
            if (channels != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    String sql = "DELETE FROM channels;";
                    statementClear.execute(sql);

                    if (!channels.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                                ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement', 'sensor', 'frequency', 'range_min', 'range_max'" +
                                ", 'allowable_error_percent', 'allowable_error_value') "
                                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (Channel channel : channels) {
                            statement.setString(1, channel.getCode());
                            statement.setString(2, channel.getName());
                            statement.setString(3, channel.getDepartment());
                            statement.setString(4, channel.getArea());
                            statement.setString(5, channel.getProcess());
                            statement.setString(6, channel.getInstallation());
                            statement.setString(7, channel.getTechnologyNumber());
                            statement.setString(8, channel.getNumberOfProtocol());
                            statement.setString(9,channel.getReference());
                            statement.setString(10, VariableConverter.dateToString(channel.getDate()));
                            statement.setString(11, String.valueOf(channel.isSuitability()));
                            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            String measurement = writer.writeValueAsString(channel.getMeasurement());
                            statement.setString(12, measurement);
                            String sensor = writer.writeValueAsString(channel.getSensor());
                            statement.setString(13, sensor);
                            statement.setDouble(14, channel.getFrequency());
                            statement.setDouble(15, channel.getRangeMin());
                            statement.setDouble(16, channel.getRangeMax());
                            statement.setDouble(17, channel.getAllowableErrorPercent());
                            statement.setDouble(18, channel.getAllowableError());
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

        private void exportChannels(ArrayList<Channel>channels){
            Calendar date = Calendar.getInstance();
            String fileName = "export_channels ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS channels ("
                    + "code text NOT NULL UNIQUE"
                    + ", name text NOT NULL"
                    + ", department text"
                    + ", area text"
                    + ", process text"
                    + ", installation text"
                    + ", technology_number text NOT NULL"
                    + ", protocol_number text"
                    + ", reference text"
                    + ", date text"
                    + ", suitability text NOT NULL"
                    + ", measurement text NOT NULL"
                    + ", sensor text NOT NULL"
                    + ", frequency real NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", allowable_error_percent real NOT NULL"
                    + ", allowable_error_value real NOT NULL"
                    + ", PRIMARY KEY (\"code\")"
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
                sql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                        ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement', 'sensor', 'frequency', 'range_min', 'range_max'" +
                        ", 'allowable_error_percent', 'allowable_error_value') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                for (Channel channel : channels) {
                    preparedStatement.setString(1, channel.getCode());
                    preparedStatement.setString(2, channel.getName());
                    preparedStatement.setString(3, channel.getDepartment());
                    preparedStatement.setString(4, channel.getArea());
                    preparedStatement.setString(5, channel.getProcess());
                    preparedStatement.setString(6, channel.getInstallation());
                    preparedStatement.setString(7, channel.getTechnologyNumber());
                    preparedStatement.setString(8, channel.getNumberOfProtocol());
                    preparedStatement.setString(9,channel.getReference());
                    preparedStatement.setString(10, VariableConverter.dateToString(channel.getDate()));
                    preparedStatement.setString(11, String.valueOf(channel.isSuitability()));
                    ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String measurement = writer.writeValueAsString(channel.getMeasurement());
                    preparedStatement.setString(12, measurement);
                    String sensor = writer.writeValueAsString(channel.getSensor());
                    preparedStatement.setString(13, sensor);
                    preparedStatement.setDouble(14, channel.getFrequency());
                    preparedStatement.setDouble(15, channel.getRangeMin());
                    preparedStatement.setDouble(16, channel.getRangeMax());
                    preparedStatement.setDouble(17, channel.getAllowableErrorPercent());
                    preparedStatement.setDouble(18, channel.getAllowableError());
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