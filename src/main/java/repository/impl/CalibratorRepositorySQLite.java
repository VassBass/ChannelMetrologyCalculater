package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Calibrator;
import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.CalibratorRepository;
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

public class CalibratorRepositorySQLite extends RepositoryJDBC implements CalibratorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalibratorRepositorySQLite.class);

    public CalibratorRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public CalibratorRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    @Override
    public boolean createTable() {
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
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("calibrators");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<Calibrator> getAll() {
        List<Calibrator>calibrators = new ArrayList<>();
        String sql = "SELECT * FROM calibrators;";

        try (ResultSet resultSet = getResultSet(sql)){

            while (resultSet.next()){
                Calibrator calibrator = new Calibrator();
                calibrator.setType(resultSet.getString("type"));
                calibrator.setName(resultSet.getString("name"));
                calibrator.setNumber(resultSet.getString("number"));
                calibrator.setRangeMin(resultSet.getDouble("range_min"));
                calibrator.setRangeMax(resultSet.getDouble("range_max"));
                calibrator.setValue(resultSet.getString("value"));
                calibrator.setMeasurement(resultSet.getString("measurement"));
                calibrator.setErrorFormula(resultSet.getString("error_formula"));
                calibrator.setCertificate(Calibrator.Certificate.fromString(resultSet.getString("certificate")));

                calibrators.add(calibrator);
            }

        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return calibrators;
    }

    @Override
    public String[] getAllNames(@Nonnull Measurement measurement) {
        List<String>calibrators = new ArrayList<>();

        String sql = "SELECT name FROM calibrators WHERE measurement = '" + measurement.getName() + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()) {
                calibrators.add(resultSet.getString("name"));
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return calibrators.toArray(new String[0]);
    }

    @Override
    public Optional<Calibrator> get(@Nonnull String name) {
        String sql = "SELECT * FROM calibrators WHERE name = '" + name + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Calibrator calibrator = new Calibrator();
                calibrator.setType(resultSet.getString("type"));
                calibrator.setName(resultSet.getString("name"));
                calibrator.setNumber(resultSet.getString("number"));
                calibrator.setRangeMin(resultSet.getDouble("range_min"));
                calibrator.setRangeMax(resultSet.getDouble("range_max"));
                calibrator.setValue(resultSet.getString("value"));
                calibrator.setMeasurement(resultSet.getString("measurement"));
                calibrator.setErrorFormula(resultSet.getString("error_formula"));
                calibrator.setCertificate(Calibrator.Certificate.fromString(resultSet.getString("certificate")));

                return Optional.of(calibrator);
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean add(@Nonnull Calibrator calibrator) {
        String sql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, calibrator.getName());
            statement.setString(2, calibrator.getType());
            statement.setString(3, calibrator.getNumber());
            statement.setString(4, calibrator.getMeasurement());
            statement.setString(5, calibrator.getValue());
            statement.setString(6, calibrator.getErrorFormula());
            statement.setString(7, calibrator.getCertificate().toString());
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
        String sql = "DELETE FROM calibrators WHERE name = '" + calibrator.getName() + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        String sql = "DELETE FROM calibrators WHERE value = '" + measurementValue + "';";
        try (Statement statement = getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator) {
        String sql = "UPDATE calibrators SET "
                + "name = '" + newCalibrator.getName() + "', "
                + "type = '" + newCalibrator.getType() + "', "
                + "number = '" + newCalibrator.getNumber() + "', "
                + "measurement = '" + newCalibrator.getMeasurement() + "', "
                + "value = '" + newCalibrator.getValue() + "', "
                + "error_formula = '" + newCalibrator.getErrorFormula() + "', "
                + "certificate = '" + newCalibrator.getCertificate() + "', "
                + "range_min = " + newCalibrator.getRangeMin() + ", "
                + "range_max = " + newCalibrator.getRangeMax() + " "
                + "WHERE name = '" + oldCalibrator.getName() + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return true;

        String sql = "UPDATE calibrators SET value = '" + newValue + "' WHERE value = '" + oldValue + "';";
        try (Statement statement = getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM calibrators;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Calibrator> calibrators) {
        String sql = "DELETE FROM calibrators;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);

            if (!calibrators.isEmpty()) {
                String insertSql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Calibrator calibrator : calibrators) {
                    if (calibrator == null) continue;

                    sqlBuilder.append("('").append(calibrator.getName()).append("', ")
                            .append("'").append(calibrator.getType()).append("', ")
                            .append("'").append(calibrator.getNumber()).append("', ")
                            .append("'").append(calibrator.getMeasurement()).append("', ")
                            .append("'").append(calibrator.getValue()).append("', ")
                            .append("'").append(calibrator.getErrorFormula()).append("', ")
                            .append("'").append(calibrator.getCertificate()).append("', ")
                            .append(calibrator.getRangeMin()).append(", ")
                            .append(calibrator.getRangeMax()).append("),");
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

                String sql = "UPDATE calibrators SET "
                        + "type = ?, number = ?, measurement = ?, value = ?, error_formula = ?, certificate = ?, range_min = ?, range_max = ? "
                        + "WHERE name = ?;";
                try (PreparedStatement statement = getPreparedStatement(sql)){
                    statement.setString(1, c.getType());
                    statement.setString(2, c.getNumber());
                    statement.setString(3, c.getMeasurement());
                    statement.setString(4, c.getValue());
                    statement.setString(5, c.getErrorFormula());
                    statement.setString(6, c.getCertificate().toString());
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
            String sql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                    + "VALUES ";
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = getStatement()) {
                for (Calibrator calibrator : newCalibrators) {
                    if (calibrator == null) continue;

                    sqlBuilder.append("('").append(calibrator.getName()).append("', ")
                            .append("'").append(calibrator.getType()).append("', ")
                            .append("'").append(calibrator.getNumber()).append("', ")
                            .append("'").append(calibrator.getMeasurement()).append("', ")
                            .append("'").append(calibrator.getValue()).append("', ")
                            .append("'").append(calibrator.getErrorFormula()).append("', ")
                            .append("'").append(calibrator.getCertificate()).append("', ")
                            .append(calibrator.getRangeMin()).append(", ")
                            .append(calibrator.getRangeMax()).append("),");
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
        String sql = "SELECT name FROM calibrators WHERE name = '" + calibrator.getName() + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }
}