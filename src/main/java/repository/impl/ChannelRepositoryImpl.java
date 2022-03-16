package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import constants.Action;
import converters.VariableConverter;
import model.Channel;
import model.Measurement;
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
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelRepositoryImpl extends Repository implements ChannelRepository {
    private static final Logger LOGGER = Logger.getLogger(ChannelRepository.class.getName());
    
    private final ArrayList<Channel>channels = new ArrayList<>();

    public ChannelRepositoryImpl(){super();}
    public ChannelRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
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
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read channels from DB");
            sql = "SELECT * FROM channels";
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
                channel.setMeasurement(Measurement.fromString(resultSet.getString("measurement")));
                channel.setSensor(Sensor.fromString(resultSet.getString("sensor")));
                channel.setFrequency(resultSet.getDouble("frequency"));
                channel.setRangeMin(resultSet.getDouble("range_min"));
                channel.setRangeMax(resultSet.getDouble("range_max"));
                double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
                this.channels.add(channel);
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
    public ArrayList<Channel> getAll() {
        return this.channels;
    }

    @Override
    public void add(Channel channel) {
        if (channel != null && !this.channels.contains(channel)) {
            this.channels.add(channel);
            new BackgroundAction().add(channel);
        }
    }

    @Override
    public void remove(Channel channel) {
        if (channel != null && this.channels.remove(channel)) {
            new BackgroundAction().remove(channel.getCode());
        }
    }

    @Override
    public void removeBySensorInCurrentThread(Sensor sensor) {
        ArrayList<Integer>indexes = new ArrayList<>();
        String sensorName = sensor.getName();
        for (int c=0;c<this.channels.size();c++){
            String channelSensor = this.channels.get(c).getSensor().getName();
            if (channelSensor.equals(sensorName)){
                indexes.add(c);
            }
        }
        Collections.reverse(indexes);
        for (int index : indexes){
            this.channels.remove(index);
        }
        new BackgroundAction().rewriteChannels(this.channels);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel> channels) {
        if (channels != null && !channels.isEmpty()) {
            this.channels.clear();
            this.channels.addAll(channels);
            new BackgroundAction().rewriteChannels(channels);
        }
    }

    @Override
    public void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        for (Channel channel : this.channels){
            if (channel.getSensor().equals(oldSensor)){
                double minRange = channel.getSensor().getRangeMin();
                double maxRange = channel.getSensor().getRangeMax();
                String value = channel.getSensor().getValue();
                newSensor.setRange(minRange, maxRange);
                newSensor.setValue(value);
                channel.setSensor(newSensor);
            }
        }
        new BackgroundAction().rewriteChannels(this.channels);
    }

    @Override
    public void changeSensorsInCurrentThread(ArrayList<Sensor> sensors) {
        for (Sensor sensor : sensors){
            for (Channel channel : this.channels){
                if (channel.getSensor().equals(sensor)){
                    channel.setSensor(sensor);
                }
            }
        }
        new BackgroundAction().rewriteChannels(this.channels);
    }

    @Override
    public void set(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null && newChannel != null
                && this.channels.contains(oldChannel) && !this.channels.contains(newChannel)) {
            int index = this.channels.indexOf(oldChannel);
            this.channels.set(index, newChannel);
            new BackgroundAction().set(oldChannel, newChannel);
        }
    }

    @Override
    public void clear() {
        this.channels.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.channels);
    }

    @Override
    public void importData(ArrayList<Channel> newChannels, ArrayList<Channel> channelsForChange) {
        for (Channel channel : channelsForChange){
            int index = this.channels.indexOf(channel);
            if (index >= 0) this.channels.set(index, channel);
        }
        this.channels.addAll(newChannels);
        new BackgroundAction().rewriteChannels(this.channels);
    }

    @Override
    public boolean isExist(String code) {
        Channel channel = new Channel();
        channel.setCode(code);
        return code != null && this.channels.contains(channel);
    }

    @Override
    public boolean isExist(String oldChannelCode, String newChannelCode) {
        if (oldChannelCode != null && newChannelCode != null) {
            Channel oldChannel = new Channel();
            oldChannel.setCode(oldChannelCode);

            int oldIndex = this.channels.indexOf(oldChannel);
            for (int index = 0; index < this.channels.size(); index++) {
                String channelCode = this.channels.get(index).getCode();
                if (channelCode.equals(newChannelCode) && index != oldIndex) return true;
            }
        }
        return false;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
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
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addChannel(this.channel);
                case REMOVE:
                    return this.removeChannel(this.channelCode);
                case CLEAR:
                    return this.clearChannels();
                case SET:
                    return this.setChannel(this.old, this.channel);
                case EXPORT:
                    return this.exportChannels(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            channels.remove(this.channel);
                            break;
                        case REMOVE:
                            if (!channels.contains(this.channel)) channels.add(this.channel);
                            break;
                        case SET:
                            channels.remove(this.channel);
                            if (!channels.contains(this.old)) channels.add(this.old);
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

        private boolean addChannel(Channel channel){
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
                statement.setString(12, channel.getMeasurement().toString());
                statement.setString(13, channel.getSensor().toString());
                statement.setDouble(14, channel.getFrequency());
                statement.setDouble(15, channel.getRangeMin());
                statement.setDouble(16, channel.getRangeMax());
                statement.setDouble(17, channel.getAllowableErrorPercent());
                statement.setDouble(18, channel.getAllowableError());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removeChannel(String channelCode){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM channels WHERE code = '" + channelCode + "';";
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

        private boolean clearChannels(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM channels;";
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

        private boolean setChannel(Channel oldChannel, Channel newChannel){
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
                statement.setString(12, newChannel.getMeasurement().toString());
                statement.setString(13, newChannel.getSensor().toString());
                statement.setDouble(14, newChannel.getFrequency());
                statement.setDouble(15, newChannel.getRangeMin());
                statement.setDouble(16, newChannel.getRangeMax());
                statement.setDouble(17, newChannel.getAllowableErrorPercent());
                statement.setDouble(18, newChannel.getAllowableError());
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

        public void rewriteChannels(ArrayList<Channel>channels){
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
                        statement.setString(12, channel.getMeasurement().toString());
                        statement.setString(13, channel.getSensor().toString());
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
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private boolean exportChannels(ArrayList<Channel>channels){
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
                    preparedStatement.setString(12, channel.getMeasurement().toString());
                    preparedStatement.setString(13, channel.getSensor().toString());
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