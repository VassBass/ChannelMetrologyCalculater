package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import constants.Action;
import model.Channel;
import model.Sensor;
import repository.ChannelRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelRepositoryImpl extends Repository<Channel> implements ChannelRepository {
    private static final Logger LOGGER = Logger.getLogger(ChannelRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public ChannelRepositoryImpl(){super();}
    public ChannelRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
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
                    + ", measurement_value text NOT NULL"
                    + ", sensor text NOT NULL"
                    + ", frequency real NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", allowable_error_percent real NOT NULL"
                    + ", allowable_error_value real NOT NULL"
                    + ", PRIMARY KEY (\"code\")"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read channels from DB");
            sql = "SELECT * FROM channels";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
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
                    channel.setDate(resultSet.getString("date"));
                    channel.setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")));
                    channel.setMeasurementValue(resultSet.getString("measurement_value"));
                    channel.setSensor(Sensor.fromString(resultSet.getString("sensor")));
                    channel.setFrequency(resultSet.getDouble("frequency"));
                    channel.setRangeMin(resultSet.getDouble("range_min"));
                    channel.setRangeMax(resultSet.getDouble("range_max"));
                    double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                    double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                    channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
                    this.mainList.add(channel);
                }
            }
        } catch (SQLException | JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public Channel get(String code) {
        Channel channel = new Channel();
        channel.setCode(code);
        int index = this.mainList.indexOf(channel);
        return index >= 0 ? this.mainList.get(index) : null;
    }

    @Override
    public ArrayList<Channel> getAll() {
        return this.mainList;
    }

    @Override
    public void add(Channel channel) {
        if (channel != null && !this.mainList.contains(channel)) {
            this.mainList.add(channel);
            new BackgroundAction().add(channel);
        }
    }

    @Override
    public void addInCurrentThread(Channel channel) {
        if (channel != null && !this.mainList.contains(channel)) {
            this.mainList.add(channel);
            new BackgroundAction().addChannel(channel);
        }
    }

    @Override
    public void remove(Channel channel) {
        if (channel != null && this.mainList.remove(channel)) {
            new BackgroundAction().remove(channel.getCode());
        }
    }

    @Override
    public void removeBySensorInCurrentThread(Sensor sensor) {
        if (sensor != null) {
            ArrayList<Integer> indexes = new ArrayList<>();
            String sensorName = sensor.getName();
            for (int c = 0; c < this.mainList.size(); c++) {
                String channelSensor = this.mainList.get(c).getSensor().getName();
                if (channelSensor.equals(sensorName)) {
                    indexes.add(c);
                }
            }
            Collections.reverse(indexes);
            for (int index : indexes) {
                this.mainList.remove(index);
            }
            new BackgroundAction().rewriteChannels(this.mainList);
        }
    }

    @Override
    public void removeByMeasurementValueInCurrentThread(String measurementValue) {
        if (measurementValue != null) {
            ArrayList<Integer>indexes = new ArrayList<>();
            for (int index = 0;index<this.mainList.size();index++){
                String measurement = this.mainList.get(index)._getMeasurementValue();
                if (measurement.equals(measurementValue)) indexes.add(index);
            }
            Collections.reverse(indexes);
            for (int index : indexes){
                this.mainList.remove(index);
            }

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                String sql = "DELETE FROM channels WHERE measurement_value = '" + measurementValue + "';";
                LOGGER.fine("Send request to delete");
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel> channels) {
        if (channels != null && !channels.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(channels);
            new BackgroundAction().rewriteChannels(channels);
        }
    }

    private boolean contains(int[]array, int i){
        for (int ii : array){
            if (i == ii) return true;
        }
        return false;
    }

    @Override
    public void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor, int ... ignored) {
        if (oldSensor != null && newSensor != null) {
            ArrayList<Channel>changedChannels = new ArrayList<>();
            Sensor sensor = new Sensor();
            sensor.setType(contains(ignored, Sensor.TYPE) ? oldSensor.getType() : newSensor.getType());
            sensor.setName(contains(ignored, Sensor.NAME) ? oldSensor.getName() : newSensor.getName());
            double minRange = contains(ignored, Sensor.RANGE) ? oldSensor.getRangeMin() : newSensor.getRangeMin();
            double maxRange = contains(ignored, Sensor.RANGE) ? oldSensor.getRangeMax() : newSensor.getRangeMax();
            sensor.setRange(minRange, maxRange);
            sensor.setNumber(contains(ignored, Sensor.NUMBER) ? oldSensor.getNumber() : newSensor.getNumber());
            sensor.setValue(contains(ignored, Sensor.VALUE) ? oldSensor.getValue() : newSensor.getValue());
            sensor.setMeasurement(contains(ignored, Sensor.MEASUREMENT) ? oldSensor.getMeasurement() : newSensor.getMeasurement());
            sensor.setErrorFormula(contains(ignored, Sensor.ERROR_FORMULA) ? oldSensor.getErrorFormula() : newSensor.getErrorFormula());
            for (Channel channel : this.mainList) {
                if (channel.getSensor().equals(oldSensor)) {
                    channel.setSensor(sensor);
                    changedChannels.add(channel);
                }
            }

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql;
                for (Channel channel : changedChannels) {
                    sql = "UPDATE channels SET "
                            + "sensor = '" + sensor + "' "
                            + "WHERE code = '" + channel.getCode() + "';";
                    statement.execute(sql);
                }
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void changeSensorsInCurrentThread(ArrayList<Sensor> sensors) {
        if (sensors != null && !sensors.isEmpty())
        for (Sensor sensor : sensors){
            for (Channel channel : this.mainList){
                if (channel.getSensor().equals(sensor)){
                    channel.setSensor(sensor);
                }
            }
        }
        new BackgroundAction().rewriteChannels(this.mainList);
    }

    @Override
    public void changeMeasurementValueInCurrentThread(String oldValue, String newValue) {
        ArrayList<Integer>needChangeSensor = new ArrayList<>();
        if (oldValue != null && newValue != null){
            for (int index = 0;index<this.mainList.size();index++){
                Channel channel = this.mainList.get(index);
                if (channel._getMeasurementValue().equals(oldValue)){
                    channel.setMeasurementValue(newValue);
                }
                if (channel.getSensor().getMeasurement().equals(oldValue)){
                    channel.getSensor().setMeasurement(newValue);
                    needChangeSensor.add(index);
                }
            }

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql = "UPDATE channels SET measurement_value = '" + newValue + "' WHERE measurement_value = '" + oldValue + "';";
                statement.execute(sql);
                for (int index : needChangeSensor){
                    Sensor sensor = this.mainList.get(index).getSensor();
                    String code = this.mainList.get(index).getCode();
                    sql = "UPDATE channels SET sensor = '" + sensor + "' WHERE code = '" + code + "';";
                    statement.execute(sql);
                }
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void set(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null && newChannel != null
                && this.mainList.contains(oldChannel)) {
            int oldIndex = this.mainList.indexOf(oldChannel);
            int newIndex = this.mainList.indexOf(newChannel);
            if (newIndex == -1 || oldIndex == newIndex) {
                this.mainList.set(oldIndex, newChannel);
                new BackgroundAction().set(oldChannel, newChannel);
            }
        }
    }

    @Override
    public void setInCurrentThread(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null && newChannel != null
                && this.mainList.contains(oldChannel) && !this.mainList.contains(newChannel)) {
            int index = this.mainList.indexOf(oldChannel);
            this.mainList.set(index, newChannel);
            new BackgroundAction().setChannel(oldChannel, newChannel);
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Channel> newChannels, ArrayList<Channel> channelsForChange) {
        if (channelsForChange != null) {
            for (Channel channel : channelsForChange) {
                int index = this.mainList.indexOf(channel);
                if (index >= 0) this.mainList.set(index, channel);
            }
        }
        if (newChannels != null) this.mainList.addAll(newChannels);
        new BackgroundAction().rewriteChannels(this.mainList);
    }

    @Override
    public boolean isExist(@Nonnull String code) {
        Channel channel = new Channel();
        channel.setCode(code);
        return this.mainList.contains(channel);
    }

    @Override
    public boolean isExist(String oldChannelCode, String newChannelCode) {
        if (oldChannelCode != null && newChannelCode != null) {
            Channel oldChannel = new Channel();
            oldChannel.setCode(oldChannelCode);

            int oldIndex = this.mainList.indexOf(oldChannel);
            if (oldIndex < 0) return true;
            for (int index = 0; index < this.mainList.size(); index++) {
                String channelCode = this.mainList.get(index).getCode();
                if (channelCode.equals(newChannelCode) && index != oldIndex) return true;
            }
        }else return true;
        return false;
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private Channel channel, old;
        private String channelCode;
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
                    return this.addChannel(this.channel);
                case REMOVE:
                    return this.removeChannel(this.channelCode);
                case CLEAR:
                    return this.clearChannels();
                case SET:
                    return this.setChannel(this.old, this.channel);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            mainList.remove(this.channel);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.channel)) mainList.add(this.channel);
                            break;
                        case SET:
                            mainList.remove(this.channel);
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
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addChannel(Channel channel){
            String sql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                    ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement_value', 'sensor', 'frequency', 'range_min', 'range_max'" +
                    ", 'allowable_error_percent', 'allowable_error_value') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                LOGGER.fine("Send request");
                statement.setString(1, channel.getCode());
                statement.setString(2, channel.getName());
                statement.setString(3, channel.getDepartment());
                statement.setString(4, channel.getArea());
                statement.setString(5, channel.getProcess());
                statement.setString(6, channel.getInstallation());
                statement.setString(7, channel.getTechnologyNumber());
                statement.setString(8, channel.getNumberOfProtocol());
                statement.setString(9,channel.getReference());
                statement.setString(10, channel.getDate());
                statement.setString(11, String.valueOf(channel.isSuitability()));
                statement.setString(12, channel._getMeasurementValue());
                statement.setString(13, channel.getSensor().toString());
                statement.setDouble(14, channel.getFrequency());
                statement.setDouble(15, channel.getRangeMin());
                statement.setDouble(16, channel.getRangeMax());
                statement.setDouble(17, channel.getAllowableErrorPercent());
                statement.setDouble(18, channel.getAllowableError());
                statement.execute();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean removeChannel(String channelCode){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM channels WHERE code = '" + channelCode + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean clearChannels(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM channels;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        boolean setChannel(Channel oldChannel, Channel newChannel){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql =  "UPDATE channels SET "
                        + "code = '" + newChannel.getCode() + "', "
                        + "name = '" + newChannel.getName() + "', "
                        + "department = '" + newChannel.getDepartment() + "', "
                        + "area = '" + newChannel.getArea() + "', "
                        + "process = '" + newChannel.getProcess() + "', "
                        + "installation = '" + newChannel.getInstallation() + "', "
                        + "technology_number = '" + newChannel.getTechnologyNumber() + "', "
                        + "protocol_number = '" + newChannel.getNumberOfProtocol() + "', "
                        + "reference = '" + newChannel.getReference() + "', "
                        + "date = '" + newChannel.getDate() + "', "
                        + "suitability = '" + newChannel.isSuitability() + "', "
                        + "measurement_value = '" + newChannel._getMeasurementValue() + "', "
                        + "sensor = '" + newChannel.getSensor() + "', "
                        + "frequency = " + newChannel.getFrequency() + ", "
                        + "range_min = " + newChannel.getRangeMin() + ", "
                        + "range_max = " + newChannel.getRangeMax() + ", "
                        + "allowable_error_percent = " + newChannel.getAllowableErrorPercent() + ", "
                        + "allowable_error_value = " + newChannel.getAllowableError() + " "
                        + "WHERE code = '" + oldChannel.getCode() + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public void rewriteChannels(ArrayList<Channel>channels){
            String clearSql = "DELETE FROM channels;";
            String insertSql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'" +
                    ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement_value', 'sensor', 'frequency', 'range_min', 'range_max'" +
                    ", 'allowable_error_percent', 'allowable_error_value') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statementClear = connection.createStatement();
                PreparedStatement statement = connection.prepareStatement(insertSql)) {
                LOGGER.fine("Send request to clear");
                statementClear.execute(clearSql);

                if (!channels.isEmpty()) {
                    LOGGER.fine("Send requests to add");
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
                        statement.setString(10, channel.getDate());
                        statement.setString(11, String.valueOf(channel.isSuitability()));
                        statement.setString(12, channel._getMeasurementValue());
                        statement.setString(13, channel.getSensor().toString());
                        statement.setDouble(14, channel.getFrequency());
                        statement.setDouble(15, channel.getRangeMin());
                        statement.setDouble(16, channel.getRangeMax());
                        statement.setDouble(17, channel.getAllowableErrorPercent());
                        statement.setDouble(18, channel.getAllowableError());
                        statement.execute();
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}