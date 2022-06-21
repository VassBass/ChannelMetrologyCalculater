package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Calibrator;
import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.CalibratorRepository;
import repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CalibratorRepositoryImpl extends Repository implements CalibratorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalibratorRepository.class);

    public CalibratorRepositoryImpl(){
        setPropertiesFromFile();
        createTable();
    }
    public CalibratorRepositoryImpl(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    @Override
    public void createTable() {
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
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        ArrayList<Calibrator>calibrators = new ArrayList<>();
        String sql = "SELECT * FROM calibrators;";

        LOGGER.info("Reading all calibrators from DB");
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
    public String[] getAllNames(Measurement measurement) {
        if (measurement == null) return null;

        ArrayList<String>calibrators = new ArrayList<>();

        LOGGER.info("Reading all calibrators names from DB");
        String sql = "SELECT * FROM calibrators WHERE measurement = '" + measurement + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            calibrators.add(resultSet.getString("name"));
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return calibrators.toArray(new String[0]);
    }

    @Override
    public Calibrator get(String name) {
        if (name == null) return null;

        LOGGER.info("Reading calibrator with name = {} from DB", name);
        String sql = "SELECT * FROM calibrators WHERE name = '" + name + "';";
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

                return calibrator;
            }else {
                LOGGER.info("Calibrator with name = {} not found", name);
                return null;
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return null;
        }
    }

    @Override
    public boolean add(Calibrator calibrator) {
        if (calibrator == null) return false;

        String sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
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
            int result = statement.executeUpdate();

            if (result > 0) LOGGER.info("Calibrator = {} was added successfully", calibrator.getName());
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(String name) {
        if (name == null) return false;

        String sql = "DELETE FROM calibrators WHERE name = '" + name + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Calibrator = {} was removed successfully", name);
            }else {
                LOGGER.info("Calibrator with name = {} not found", name);
            }
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementValue(String measurementValue) {
        if (measurementValue == null) return false;

        String sql = "DELETE FROM calibrators WHERE value = '" + measurementValue + "';";
        try (Statement statement = getStatement()) {
            int result = statement.executeUpdate(sql);
            LOGGER.info("Removed {} calibrators with measurementValue = {}", result, measurementValue);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator == null || newCalibrator == null) return false;
        if (oldCalibrator.isMatch(newCalibrator)) return true;

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
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Calibrator:\n{}\nwas replaced by calibrator:\n{}\nsuccessfully", oldCalibrator, newCalibrator);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(String oldValue, String newValue) {
        if (oldValue == null || newValue == null) return false;
        if (oldValue.equals(newValue)) return true;

        String sql = "UPDATE calibrators SET value = '" + newValue + "' WHERE value = '" + oldValue + "';";
        try (Statement statement = getStatement()) {
            int result = statement.executeUpdate(sql);
            LOGGER.info("Changed measurementValue of {} calibrators from {} to {}", result, oldValue, newValue);
            return true;
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
            LOGGER.info("Calibrators list in DB was cleared successfully");
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(ArrayList<Calibrator> calibrators) {
        if (calibrators == null) return false;

        String sql = "DELETE FROM calibrators;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Calibrators list in DB was cleared successfully");

            if (!calibrators.isEmpty()) {
                String insertSql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Calibrator calibrator : calibrators) {
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

            LOGGER.info("The list of old calibrators has been rewritten to the new one:\n{}", calibrators);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean importData(ArrayList<Calibrator> newCalibrators, ArrayList<Calibrator> calibratorsForChange) {
        int changeResult = 0;
        int addResult;
        if (calibratorsForChange != null && !calibratorsForChange.isEmpty()){
            for (Calibrator c : calibratorsForChange){
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
                    changeResult++;
                } catch (SQLException e) {
                    LOGGER.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (newCalibrators != null && !newCalibrators.isEmpty()){
            String sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                    + "VALUES ";
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = getStatement()) {
                for (Calibrator calibrator : newCalibrators) {
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
                addResult = statement.executeUpdate(sqlBuilder.toString());

                LOGGER.info("Calibrators import was successful");
                LOGGER.info("Changed = {} | Added = {}", changeResult, addResult);
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isExists(Calibrator calibrator) {
        String sql = "SELECT * FROM calibrators WHERE name = '" + calibrator.getName() + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }
}