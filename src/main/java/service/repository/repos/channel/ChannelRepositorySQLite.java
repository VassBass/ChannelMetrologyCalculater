package service.repository.repos.channel;

import model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ChannelRepositorySQLite implements ChannelRepository {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;
    protected final JsonObjectMapper jsonMapper = JacksonJsonObjectMapper.getInstance();

    public ChannelRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(ChannelRepository.class);
        this.connector = connector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Channel> getAll() {
        List<Channel>channels = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
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
                channel.setSensorName(resultSet.getString("sensor_name"));
                channel.setFrequency(resultSet.getDouble("frequency"));
                channel.setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"));
                channel.setMeasurementValue(resultSet.getString("measurement_value"));

                double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                channel.setAllowableError(allowableErrorPercent, allowableErrorValue);

                String controlPointsJson = resultSet.getString("control_points");
                channel.setControlPoints(jsonMapper.JsonToObject(controlPointsJson, Map.class));

                channels.add(channel);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return channels;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Channel get(@Nonnull String code) {
        String sql = String.format("SELECT * FROM %s WHERE code = '%s' LIMIT 1;", tableName, code);
        try (ResultSet resultSet = connector.getResultSet(sql)){
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
                channel.setSensorName(resultSet.getString("sensor_name"));
                channel.setFrequency(resultSet.getDouble("frequency"));
                channel.setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"));
                channel.setMeasurementValue(resultSet.getString("measurement_value"));

                double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                channel.setAllowableError(allowableErrorPercent, allowableErrorValue);

                String controlPointsJson = resultSet.getString("control_points");
                channel.setControlPoints(jsonMapper.JsonToObject(controlPointsJson, Map.class));

                return channel;
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Channel channel) {
        String sql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                ", protocol_number, reference, date, suitability, measurement_value, sensor_name, frequency, range_min, range_max" +
                ", allowable_error_percent, allowable_error_value, control_points) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
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
            statement.setString(13, channel.getSensorName());
            statement.setDouble(14, channel.getFrequency());
            statement.setDouble(15, channel.getRangeMin());
            statement.setDouble(16, channel.getRangeMax());
            statement.setDouble(17, channel.getAllowableErrorPercent());
            statement.setDouble(18, channel.getAllowableError());
            statement.setString(19, jsonMapper.objectToJson(channel.getControlPoints()));

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Channel channel) {
        String sql = String.format("DELETE FROM %s WHERE code = '%s';", tableName, channel.getCode());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeBySensorName(@Nonnull String sensorName) {
        String sql = String.format("DELETE FROM %s WHERE sensor_name = '%s';", tableName, sensorName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            return true;
        } catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        String sql = String.format("DELETE FROM %s WHERE measurement_value = '%s';", tableName, measurementValue);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Channel> channels) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!channels.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number"
                        + ", protocol_number, reference, date, suitability, measurement_value, sensor_name, frequency, range_min, range_max"
                        + ", allowable_error_percent, allowable_error_value, control_points) "
                        + "VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Channel channel : channels) {
                    if (channel == null) continue;

                    String values = String.format(
                            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s'),",
                            channel.getCode(),
                            channel.getName(),
                            channel.getDepartment(),
                            channel.getArea(),
                            channel.getProcess(),
                            channel.getInstallation(),
                            channel.getTechnologyNumber(),
                            channel.getNumberOfProtocol(),
                            channel.getReference(),
                            channel.getDate(),
                            channel.isSuitability(),
                            channel.getMeasurementValue(),
                            channel.getSensorName(),
                            channel.getFrequency(),
                            channel.getRangeMin(),
                            channel.getRangeMax(),
                            channel.getAllowableErrorPercent(),
                            channel.getAllowableError(),
                            jsonMapper.objectToJson(channel.getControlPoints())
                    );
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeSensorName(@Nonnull String oldSensor, @Nonnull String newSensor) {
        String sql = String.format("UPDATE %s SET sensor_name = '%s' WHERE sensor_name = '%s';",
                tableName, newSensor, oldSensor);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        String sql = String.format("UPDATE %s SET measurement_value = '%s' WHERE measurement_value = '%s';", tableName, newValue, oldValue);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel) {
        String sql = String.format("UPDATE %s SET " +
                "code = '%s'" +
                ", name = '%s'" +
                ", department = '%s'" +
                ", area = '%s'" +
                ", process = '%s'" +
                ", installation = '%s'" +
                ", technology_number = '%s'" +
                ", protocol_number = '%s'" +
                ", reference = '%s'" +
                ", date = '%s'" +
                ", suitability = '%s'" +
                ", measurement_value = '%s'" +
                ", sensor_name = '%s'" +
                ", frequency = %s" +
                ", range_min = %s" +
                ", range_max = %s" +
                ", allowable_error_percent = %s" +
                ", allowable_error_value = %s" +
                ", control_points = '%s'" +
                " WHERE code = '%s';",
                tableName,
                newChannel.getCode(),
                newChannel.getName(),
                newChannel.getDepartment(),
                newChannel.getArea(),
                newChannel.getProcess(),
                newChannel.getInstallation(),
                newChannel.getTechnologyNumber(),
                newChannel.getNumberOfProtocol(),
                newChannel.getReference(),
                newChannel.getDate(),
                newChannel.isSuitability(),
                newChannel.getMeasurementValue(),
                newChannel.getSensorName(),
                newChannel.getFrequency(),
                newChannel.getRangeMin(),
                newChannel.getRangeMax(),
                newChannel.getAllowableErrorPercent(),
                newChannel.getAllowableError(),
                jsonMapper.objectToJson(newChannel.getControlPoints()),
                oldChannel.getCode()
        );
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean importData(@Nonnull Collection<Channel> newChannels, @Nonnull Collection<Channel> channelsForChange) {
        if (!channelsForChange.isEmpty()){
            for (Channel c : channelsForChange){
                if (c == null) continue;

                String sql = String.format("UPDATE %s SET "
                        + "name = ?, department = ?, area = ?, process = ?, installation = ?, technology_number = ?, protocol_number = ?, "
                        + "reference = ?, date = ?, suitability = ?, measurement_value = ?, sensor_name = ?, frequency = ?, range_min = ?, "
                        + "range_max = ?, allowable_error_percent = ?, allowable_error_value = ?, control_points = ? "
                        + "WHERE code = ?;", tableName);
                try (PreparedStatement statement = connector.getPreparedStatement(sql)){
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
                    statement.setString(12, c.getSensorName());
                    statement.setDouble(13, c.getFrequency());
                    statement.setDouble(14, c.getRangeMin());
                    statement.setDouble(15, c.getRangeMax());
                    statement.setDouble(16, c.getAllowableErrorPercent());
                    statement.setDouble(17, c.getAllowableError());
                    statement.setString(18, jsonMapper.objectToJson(c.getControlPoints()));

                    statement.setString(19, c.getCode());

                    statement.execute();
                } catch (SQLException e) {
                    logger.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newChannels.isEmpty()){
            String insertSql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                    ", protocol_number, reference, date, suitability, measurement_value, sensor_name, frequency, range_min" +
                    ", range_max, allowable_error_percent, allowable_error_value, control_points)" +
                    " VALUES ", tableName);
            StringBuilder sqlBuilder = new StringBuilder(insertSql);
            try (Statement statement = connector.getStatement()) {
                for (Channel channel : newChannels) {
                    if (channel == null) continue;

                    String values = String.format(
                            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s'),",
                            channel.getCode(),
                            channel.getName(),
                            channel.getDepartment(),
                            channel.getArea(),
                            channel.getProcess(),
                            channel.getInstallation(),
                            channel.getTechnologyNumber(),
                            channel.getNumberOfProtocol(),
                            channel.getReference(),
                            channel.getDate(),
                            channel.isSuitability(),
                            channel.getMeasurementValue(),
                            channel.getSensorName(),
                            channel.getFrequency(),
                            channel.getRangeMin(),
                            channel.getRangeMax(),
                            channel.getAllowableErrorPercent(),
                            channel.getAllowableError(),
                            jsonMapper.objectToJson(channel.getControlPoints())
                    );
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            } catch (SQLException e) {
                logger.warn("Exception was thrown!", e);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isExist(@Nonnull String code) {
        String sql = String.format("SELECT code FROM %s WHERE code = '%s' LIMIT 1;", tableName, code);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return true;
        }
    }

    @Override
    public boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode) {
        if (oldChannelCode.equals(newChannelCode)) return false;

        String sql = String.format("SELECT code FROM %s WHERE code = '%s' LIMIT 1;", tableName, newChannelCode);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return true;
        }
    }
}