package service.repository.repos.calibrator;

import model.Calibrator;
import model.Measurement;
import model.builder.CalibratorBuilder;
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

public class CalibratorRepositorySQLite implements CalibratorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalibratorRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;
    private final JsonObjectMapper jsonMapper = JacksonJsonObjectMapper.getInstance();

    public CalibratorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(CalibratorRepository.class);
        this.connector = connector;
    }

    @Override
    public Collection<Calibrator> getAll() {
        List<Calibrator>calibrators = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                CalibratorBuilder calibratorBuilder = new CalibratorBuilder();
                calibratorBuilder.setType(resultSet.getString("type"));
                calibratorBuilder.setName(resultSet.getString("name"));
                calibratorBuilder.setNumber(resultSet.getString("number"));
                calibratorBuilder.setRangeMin(resultSet.getDouble("range_min"));
                calibratorBuilder.setRangeMax(resultSet.getDouble("range_max"));
                calibratorBuilder.setMeasurementValue(resultSet.getString("value"));
                calibratorBuilder.setMeasurementName(resultSet.getString("measurement"));
                calibratorBuilder.setErrorFormula(resultSet.getString("error_formula"));

                String certificateJson = resultSet.getString("certificate");
                Calibrator.Certificate certificate = jsonMapper.JsonToObject(certificateJson, Calibrator.Certificate.class);
                if (certificate != null) calibratorBuilder.setCertificate(certificate);

                calibrators.add(calibratorBuilder.build());
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return calibrators;
    }

    @Override
    public String[] getAllNames(@Nonnull Measurement measurement) {
        List<String>calibrators = new ArrayList<>();

        String sql = String.format("SELECT name FROM %s WHERE measurement = '%s';", tableName, measurement.getName());
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()) {
                calibrators.add(resultSet.getString("name"));
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return calibrators.toArray(new String[0]);
    }

    @Override
    public Calibrator get(@Nonnull String name) {
        String sql = String.format("SELECT * FROM %s WHERE name = '%s' LIMIT 1;", tableName, name);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                CalibratorBuilder calibratorBuilder = new CalibratorBuilder();
                calibratorBuilder.setType(resultSet.getString("type"));
                calibratorBuilder.setName(resultSet.getString("name"));
                calibratorBuilder.setNumber(resultSet.getString("number"));
                calibratorBuilder.setRangeMin(resultSet.getDouble("range_min"));
                calibratorBuilder.setRangeMax(resultSet.getDouble("range_max"));
                calibratorBuilder.setMeasurementValue(resultSet.getString("value"));
                calibratorBuilder.setMeasurementName(resultSet.getString("measurement"));
                calibratorBuilder.setErrorFormula(resultSet.getString("error_formula"));

                String certificateJson = resultSet.getString("certificate");
                Calibrator.Certificate certificate = jsonMapper.JsonToObject(certificateJson, Calibrator.Certificate.class);
                if (certificate != null) calibratorBuilder.setCertificate(certificate);

                return calibratorBuilder.build();
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Calibrator calibrator) {
        String sql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, calibrator.getName());
            statement.setString(2, calibrator.getType());
            statement.setString(3, calibrator.getNumber());
            statement.setString(4, calibrator.getMeasurement());
            statement.setString(5, calibrator.getValue());
            statement.setString(6, calibrator.getErrorFormula());
            statement.setString(7, jsonMapper.objectToJson(calibrator.getCertificate()));
            statement.setDouble(8, calibrator.getRangeMin());
            statement.setDouble(9, calibrator.getRangeMax());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Calibrator calibrator) {
        String sql = String.format("DELETE FROM %s WHERE name = '%s';", tableName, calibrator.getName());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        String sql = String.format("DELETE FROM %s WHERE value = '%s';", tableName, measurementValue);
        try (Statement statement = connector.getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator) {
        String sql = String.format("UPDATE %s SET " +
                "name = '%s'" +
                ", type = '%s'" +
                ", number = '%s'" +
                ", measurement = '%s'" +
                ", value = '%s'" +
                ", error_formula = '%s'" +
                ", certificate = '%s'" +
                ", range_min = %s" +
                ", range_max = %s" +
                " WHERE name = '%s';",
                tableName,
                newCalibrator.getName(),
                newCalibrator.getType(),
                newCalibrator.getNumber(),
                newCalibrator.getMeasurement(),
                newCalibrator.getValue(),
                newCalibrator.getErrorFormula(),
                jsonMapper.objectToJson(newCalibrator.getCertificate()),
                newCalibrator.getRangeMin(),
                newCalibrator.getRangeMax(),
                oldCalibrator.getName());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return true;

        String sql = String.format("UPDATE %s SET value = '%s' WHERE value = '%s';", tableName, newValue, oldValue);
        try (Statement statement = connector.getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Calibrator> calibrators) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!calibrators.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                        + "VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Calibrator calibrator : calibrators) {
                    if (calibrator == null) continue;

                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            calibrator.getName(),
                            calibrator.getType(),
                            calibrator.getNumber(),
                            calibrator.getMeasurement(),
                            calibrator.getValue(),
                            calibrator.getErrorFormula(),
                            jsonMapper.objectToJson(calibrator.getCertificate()),
                            calibrator.getRangeMin(),
                            calibrator.getRangeMax());
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean importData(@Nonnull Collection<Calibrator> newCalibrators, @Nonnull Collection<Calibrator> calibratorsForChange) {
        if (!calibratorsForChange.isEmpty()){
            for (Calibrator c : calibratorsForChange){
                if (c == null) continue;

                String sql = String.format("UPDATE %s SET "
                        + "type = ?, number = ?, measurement = ?, value = ?, error_formula = ?, certificate = ?, range_min = ?, range_max = ? "
                        + "WHERE name = ?;", tableName);
                try (PreparedStatement statement = connector.getPreparedStatement(sql)){
                    statement.setString(1, c.getType());
                    statement.setString(2, c.getNumber());
                    statement.setString(3, c.getMeasurement());
                    statement.setString(4, c.getValue());
                    statement.setString(5, c.getErrorFormula());
                    statement.setString(6, jsonMapper.objectToJson(c.getCertificate()));
                    statement.setDouble(7, c.getRangeMin());
                    statement.setDouble(8, c.getRangeMax());
                    statement.setString(9, c.getName());

                    statement.execute();
                } catch (SQLException e) {
                    LOGGER.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newCalibrators.isEmpty()){
            String sql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                    + "VALUES ", tableName);
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = connector.getStatement()) {
                for (Calibrator calibrator : newCalibrators) {
                    if (calibrator == null) continue;

                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            calibrator.getName(),
                            calibrator.getType(),
                            calibrator.getNumber(),
                            calibrator.getMeasurement(),
                            calibrator.getValue(),
                            calibrator.getErrorFormula(),
                            jsonMapper.objectToJson(calibrator.getCertificate()),
                            calibrator.getRangeMin(),
                            calibrator.getRangeMax());
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isExists(@Nonnull Calibrator calibrator) {
        String sql = String.format("SELECT name FROM %s WHERE name = '%s' LIMIT 1;", tableName, calibrator.getName());
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }
}