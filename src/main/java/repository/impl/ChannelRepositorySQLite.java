package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Channel;
import model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ChannelRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ChannelRepositorySQLite extends RepositoryJDBC implements ChannelRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelRepositorySQLite.class);
    private static ChannelRepositorySQLite instance;

    private ChannelRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public ChannelRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    public static ChannelRepositorySQLite getInstance() {
        if (instance == null) instance = new ChannelRepositorySQLite();
        return instance;
    }

    @Override
    public boolean createTable(){
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
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("channels");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Optional<Channel> get(@Nonnull String code) {
        LOGGER.info("Reading channel with code = {} from DB", code);
        String sql = "SELECT * FROM channels WHERE code = '" + code + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Channel channel = new Channel(code);
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
                channel.setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"));
                channel.setAllowableError(resultSet.getDouble("allowable_error_percent"),
                        resultSet.getDouble("allowable_error_value"));

                return Optional.of(channel);
            }else {
                LOGGER.info("Channel with code = {} not found", code);
                return Optional.empty();
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Channel> getAll() {
        List<Channel>channels = new ArrayList<>();

        String sql = "SELECT * FROM channels;";
        LOGGER.info("Reading all channels from DB");
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Channel channel = new Channel(resultSet.getString("code"));
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
                channel.setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"));
                channel.setAllowableError(resultSet.getDouble("allowable_error_percent"),
                        resultSet.getDouble("allowable_error_value"));

                channels.add(channel);
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return channels;
    }

    @Override
    public boolean add(@Nonnull Channel channel) {
        String sql = "INSERT INTO channels (code, name, department, area, process, installation, technology_number" +
                ", protocol_number, reference, date, suitability, measurement_value, sensor, frequency, range_min, range_max" +
                ", allowable_error_percent, allowable_error_value) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
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
            statement.setString(12, channel.getMeasurementValue());
            statement.setString(13, channel.getSensor().toString());
            statement.setDouble(14, channel.getFrequency());
            statement.setDouble(15, channel.getRangeMin());
            statement.setDouble(16, channel.getRangeMax());
            statement.setDouble(17, channel.getAllowableErrorPercent());
            statement.setDouble(18, channel.getAllowableError());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Channel = {} was added successfully", channel.getName());
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Channel channel) {
        String sql = "DELETE FROM channels WHERE code = '" + channel.getCode() + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Channel = {} was removed successfully", channel.getName());
                return true;
            }else {
                LOGGER.info("Channel with code = {} not found", channel.getCode());
                return false;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeBySensor(@Nonnull Sensor sensor) {
        Collection<Channel>channels = getAll();
        List<String> codes = new ArrayList<>();
        String sensorName = sensor.getName();
        for (Channel c : channels) {
            String channelSensorName = c.getSensor().getName();
            if (channelSensorName.equals(sensorName)) {
                codes.add(c.getCode());
            }
        }

        String sql = "DELETE FROM channels WHERE code = ?;";
        int result = 0;
        for (String code : codes){
            try (PreparedStatement statement = getPreparedStatement(sql)){
                statement.setString(1, code);
                statement.execute();
                result++;
            }catch (SQLException e){
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }

        LOGGER.info("Removed {} channels with sensor = {}", result, sensor.getName());
        return true;
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        String sql = "DELETE FROM channels WHERE measurement_value = '" + measurementValue + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            LOGGER.info("Removed {} channels with measurementValue = {}", result, measurementValue);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Channel> channels) {
        String sql = "DELETE FROM channels;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Channels list in DB was cleared successfully");

            if (!channels.isEmpty()) {
                String insertSql = "INSERT INTO channels (code, name, department, area, process, installation, technology_number"
                        + ", protocol_number, reference, date, suitability, measurement_value, sensor, frequency, range_min, range_max"
                        + ", allowable_error_percent, allowable_error_value) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Channel channel : channels) {
                    if (channel == null) continue;

                    sqlBuilder.append("('").append(channel.getCode()).append("', ")
                            .append("'").append(channel.getName()).append("', ")
                            .append("'").append(channel.getDepartment()).append("', ")
                            .append("'").append(channel.getArea()).append("', ")
                            .append("'").append(channel.getProcess()).append("', ")
                            .append("'").append(channel.getInstallation()).append("', ")
                            .append("'").append(channel.getTechnologyNumber()).append("', ")
                            .append("'").append(channel.getNumberOfProtocol()).append("', ")
                            .append("'").append(channel.getReference()).append("', ")
                            .append("'").append(channel.getDate()).append("', ")
                            .append("'").append(channel.isSuitability()).append("', ")
                            .append("'").append(channel.getMeasurementValue()).append("', ")
                            .append("'").append(channel.getSensor()).append("', ")
                            .append(channel.getFrequency()).append(", ")
                            .append(channel.getRangeMin()).append(", ")
                            .append(channel.getRangeMax()).append(", ")
                            .append(channel.getAllowableErrorPercent()).append(", ")
                            .append(channel.getAllowableError()).append("),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old channels has been rewritten to the new one:\n{}", channels);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    private boolean contains(int[]array, int i){
        for (int ii : array){
            if (i == ii) return true;
        }
        return false;
    }

    @Override
    public boolean changeSensor(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor, int ... ignored) {
        List<Channel>changedChannels = new ArrayList<>();

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

        Collection<Channel>channels = getAll();
        for (Channel channel : channels) {
            if (channel.getSensor().equals(oldSensor)) {
                changedChannels.add(channel);
            }
        }

        int result = 0;
        try (Statement statement = getStatement()){
            for (Channel channel : changedChannels) {
                String sql = "UPDATE channels SET sensor = '" + sensor + "' "
                        + "WHERE code = '" + channel.getCode() + "';";
                statement.execute(sql);
                result++;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }

        LOGGER.info("Changed sensor of {} channels from {} to {}", result, oldSensor, sensor);
        return true;
    }

    @Override
    public boolean changeSensors(@Nonnull List<Sensor> sensors) {
        Collection<Channel>channels = getAll();
        for (Sensor sensor : sensors){
            if (sensor == null) continue;

            for (Channel channel : channels){
                if (channel.getSensor().equals(sensor)){
                    channel.setSensor(sensor);
                }
            }
        }

        return rewrite(channels);
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        List<String>needChangeSensorCodes = new ArrayList<>();
        List<Sensor>sensors = new ArrayList<>();
        Collection<Channel>channels = getAll();
        for (Channel c : channels){
            Sensor s = c.getSensor();
            if (s.getMeasurement().equals(oldValue)){
                s.setMeasurement(newValue);
                sensors.add(s);
                needChangeSensorCodes.add(c.getCode());
            }
        }

        int result = 0;
        try (Statement statement = getStatement()){
            String sql = "UPDATE channels SET measurement_value = '" + newValue + "' WHERE measurement_value = '" + oldValue + "';";
            statement.execute(sql);
            for (int c = 0;c<needChangeSensorCodes.size();c++){
                String code = needChangeSensorCodes.get(c);
                Sensor sensor = sensors.get(c);
                sql = "UPDATE channels SET sensor = '" + sensor + "' WHERE code = '" + code + "';";
                statement.execute(sql);
                result++;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }

        LOGGER.info("Changed measurementValue of {} channels from {} to {}", result, oldValue, newValue);
        return true;
    }

    @Override
    public boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel) {
        String sql = "UPDATE channels SET "
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
                + "measurement_value = '" + newChannel.getMeasurementValue() + "', "
                + "sensor = '" + newChannel.getSensor() + "', "
                + "frequency = " + newChannel.getFrequency() + ", "
                + "range_min = " + newChannel.getRangeMin() + ", "
                + "range_max = " + newChannel.getRangeMax() + ", "
                + "allowable_error_percent = " + newChannel.getAllowableErrorPercent() + ", "
                + "allowable_error_value = " + newChannel.getAllowableError() + " "
                + "WHERE code = '" + oldChannel.getCode() + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Channel:\n{}\nwas replaced by channel:\n{}\nsuccessfully", oldChannel, newChannel);
                return true;
            }else {
                LOGGER.info("Channel with code \"{}\" not found", oldChannel.getCode());
                return false;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM channels;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Channels list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean importData(@Nonnull Collection<Channel> newChannels, @Nonnull Collection<Channel> channelsForChange) {
        int changeResult = 0;
        int addResult = 0;
        if (!channelsForChange.isEmpty()){
            for (Channel c : channelsForChange){
                if (c == null) continue;

                String sql = "UPDATE channels SET "
                        + "name = ?, department = ?, area = ?, process = ?, installation = ?, technology_number = ?, protocol_number = ?, "
                        + "reference = ?, date = ?, suitability = ?, measurement_value = ?, sensor = ?, frequency = ?, range_min = ?, "
                        + "range_max = ?, allowable_error_percent = ?, allowable_error_value = ? "
                        + "WHERE code = ?;";
                try (PreparedStatement statement = getPreparedStatement(sql)){
                    statement.setString(1, c.getName());
                    statement.setString(2, c.getDepartment());
                    statement.setString(3, c.getArea());
                    statement.setString(4, c.getProcess());
                    statement.setString(5, c.getInstallation());
                    statement.setString(6, c.getTechnologyNumber());
                    statement.setString(7, c.getNumberOfProtocol());
                    statement.setString(8, c.getReference());
                    statement.setString(9, c.getDate());
                    statement.setString(10, String.valueOf(c.isSuitability()));
                    statement.setString(11, c.getMeasurementValue());
                    statement.setString(12, c.getSensor().toString());
                    statement.setDouble(13, c.getFrequency());
                    statement.setDouble(14, c.getRangeMin());
                    statement.setDouble(15, c.getRangeMax());
                    statement.setDouble(16, c.getAllowableErrorPercent());
                    statement.setDouble(17, c.getAllowableError());

                    statement.setString(18, c.getCode());

                    statement.execute();
                    changeResult++;
                } catch (SQLException e) {
                    LOGGER.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newChannels.isEmpty()){
            String insertSql = "INSERT INTO channels ('code', 'name', 'department', 'area', 'process', 'installation', 'technology_number'"
                    + ", 'protocol_number', 'reference', 'date', 'suitability', 'measurement_value', 'sensor', 'frequency', 'range_min', 'range_max'"
                    + ", 'allowable_error_percent', 'allowable_error_value') "
                    + "VALUES ";
            StringBuilder sqlBuilder = new StringBuilder(insertSql);
            try (Statement statement = getStatement()) {
                for (Channel channel : newChannels) {
                    if (channel == null) continue;

                    sqlBuilder.append("('").append(channel.getCode()).append("', ")
                            .append("'").append(channel.getName()).append("', ")
                            .append("'").append(channel.getDepartment()).append("', ")
                            .append("'").append(channel.getArea()).append("', ")
                            .append("'").append(channel.getProcess()).append("', ")
                            .append("'").append(channel.getInstallation()).append("', ")
                            .append("'").append(channel.getTechnologyNumber()).append("', ")
                            .append("'").append(channel.getNumberOfProtocol()).append("', ")
                            .append("'").append(channel.getReference()).append("', ")
                            .append("'").append(channel.getDate()).append("', ")
                            .append("'").append(channel.isSuitability()).append("', ")
                            .append("'").append(channel.getMeasurementValue()).append("', ")
                            .append("'").append(channel.getSensor()).append("', ")
                            .append(channel.getFrequency()).append(", ")
                            .append(channel.getRangeMin()).append(", ")
                            .append(channel.getRangeMax()).append(", ")
                            .append(channel.getAllowableErrorPercent()).append(", ")
                            .append(channel.getAllowableError()).append("),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                addResult = statement.executeUpdate(sqlBuilder.toString());
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }

        LOGGER.info("Channels import was successful");
        LOGGER.info("Changed = {} | Added = {}", changeResult, addResult);
        return true;
    }

    @Override
    public boolean isExist(@Nonnull String code) {
        String sql = "SELECT code FROM channels WHERE code = '" + code + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }

    @Override
    public boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode) {
        if (oldChannelCode.equals(newChannelCode)) return false;

        String sql = "SELECT code FROM channels WHERE code = '" + newChannelCode + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }
}