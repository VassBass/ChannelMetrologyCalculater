package repository.repos.channel;

import model.dto.Channel;
import model.dto.builder.ChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.ChannelRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChannelRepositorySQLite implements ChannelRepository {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    public ChannelRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(ChannelRepository.class);
        this.connector = connector;
        new ChannelRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    public Collection<Channel> getAll() {
        List<Channel>channels = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                channels.add(new ChannelBuilder(resultSet.getString("code"))
                        .setName(resultSet.getString("name"))
                        .setDepartment(resultSet.getString("department"))
                        .setArea(resultSet.getString("area"))
                        .setProcess(resultSet.getString("process"))
                        .setInstallation(resultSet.getString("installation"))
                        .setTechnologyNumber(resultSet.getString("technology_number"))
                        .setNumberOfProtocol(resultSet.getString("protocol_number"))
                        .setReference(resultSet.getString("reference"))
                        .setDate(resultSet.getString("date"))
                        .setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")))
                        .setFrequency(resultSet.getDouble("frequency"))
                        .setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"))
                        .setMeasurementName(resultSet.getString("measurement_name"))
                        .setMeasurementValue(resultSet.getString("measurement_value"))
                        .setAllowableErrorInPercent(resultSet.getDouble("allowable_error_percent"))
                        .setAllowableErrorInValue(resultSet.getDouble("allowable_error_value"))
                        .build());
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return channels;
    }

    @Override
    public Channel get(@Nonnull String code) {
        String sql = String.format("SELECT * FROM %s WHERE code = '%s' LIMIT 1;", tableName, code);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                return new ChannelBuilder(resultSet.getString("code"))
                        .setName(resultSet.getString("name"))
                        .setDepartment(resultSet.getString("department"))
                        .setArea(resultSet.getString("area"))
                        .setProcess(resultSet.getString("process"))
                        .setInstallation(resultSet.getString("installation"))
                        .setTechnologyNumber(resultSet.getString("technology_number"))
                        .setNumberOfProtocol(resultSet.getString("protocol_number"))
                        .setReference(resultSet.getString("reference"))
                        .setDate(resultSet.getString("date"))
                        .setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")))
                        .setFrequency(resultSet.getDouble("frequency"))
                        .setRange(resultSet.getDouble("range_min"), resultSet.getDouble("range_max"))
                        .setMeasurementName(resultSet.getString("measurement_name"))
                        .setMeasurementValue(resultSet.getString("measurement_value"))
                        .setAllowableErrorInPercent(resultSet.getDouble("allowable_error_percent"))
                        .setAllowableErrorInValue(resultSet.getDouble("allowable_error_value"))
                        .build();
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Channel channel) {
        String sql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                ", protocol_number, reference, date, suitability, measurement_name, measurement_value, frequency" +
                ", range_min, range_max, allowable_error_percent, allowable_error_value) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", tableName);
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
            statement.setString(12, channel.getMeasurementName());
            statement.setString(13, channel.getMeasurementValue());
            statement.setDouble(14, channel.getFrequency());
            statement.setDouble(15, channel.getRangeMin());
            statement.setDouble(16, channel.getRangeMax());
            statement.setDouble(17, channel.getAllowableErrorPercent());
            statement.setDouble(18, channel.getAllowableErrorValue());

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
                        + ", protocol_number, reference, date, suitability, measurement_name, measurement_value, frequency" +
                        ", range_min, range_max, allowable_error_percent, allowable_error_value) "
                        + "VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Channel channel : channels) {
                    if (channel == null) continue;

                    String values = String.format(
                            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s),",
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
                            channel.getMeasurementName(),
                            channel.getMeasurementValue(),
                            channel.getFrequency(),
                            channel.getRangeMin(),
                            channel.getRangeMax(),
                            channel.getAllowableErrorPercent(),
                            channel.getAllowableErrorValue()
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
                ", measurement_name = '%s'" +
                ", measurement_value = '%s'" +
                ", frequency = %s" +
                ", range_min = %s" +
                ", range_max = %s" +
                ", allowable_error_percent = %s" +
                ", allowable_error_value = %s" +
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
                newChannel.getMeasurementName(),
                newChannel.getMeasurementValue(),
                newChannel.getFrequency(),
                newChannel.getRangeMin(),
                newChannel.getRangeMax(),
                newChannel.getAllowableErrorPercent(),
                newChannel.getAllowableErrorValue(),
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
                        + "name = ?, department = ?, area = ?, process = ?, installation = ?, technology_number = ?, protocol_number = ?"
                        + ", reference = ?, date = ?, suitability = ?, measurement_name = ?, measurement_value = ?"
                        + ", frequency = ?, range_min = ?, range_max = ?, allowable_error_percent = ?, allowable_error_value = ?"
                        + " WHERE code = ?;", tableName);
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
                    statement.setString(11, c.getMeasurementName());
                    statement.setString(12, c.getMeasurementValue());
                    statement.setDouble(13, c.getFrequency());
                    statement.setDouble(14, c.getRangeMin());
                    statement.setDouble(15, c.getRangeMax());
                    statement.setDouble(16, c.getAllowableErrorPercent());
                    statement.setDouble(17, c.getAllowableErrorValue());

                    statement.setString(18, c.getCode());

                    statement.execute();
                } catch (SQLException e) {
                    logger.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newChannels.isEmpty()){
            String insertSql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                    ", protocol_number, reference, date, suitability, measurement_name, measurement_value, frequency" +
                    ", range_min, range_max, allowable_error_percent, allowable_error_value)" +
                    " VALUES ", tableName);
            StringBuilder sqlBuilder = new StringBuilder(insertSql);
            try (Statement statement = connector.getStatement()) {
                for (Channel channel : newChannels) {
                    if (channel == null) continue;

                    String values = String.format(
                            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s),",
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
                            channel.getMeasurementName(),
                            channel.getMeasurementValue(),
                            channel.getFrequency(),
                            channel.getRangeMin(),
                            channel.getRangeMax(),
                            channel.getAllowableErrorPercent(),
                            channel.getAllowableErrorValue()
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

    @Override
    public Collection<Channel> search(SearchParams params) {
        return new ArrayList<>();
    }
}