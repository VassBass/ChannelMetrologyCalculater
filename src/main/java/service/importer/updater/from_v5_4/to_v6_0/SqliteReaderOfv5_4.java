package service.importer.updater.from_v5_4.to_v6_0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.importer.Model;
import service.importer.ModelHolder;
import service.importer.Reader;
import service.importer.SqliteConnector;

import javax.annotation.Nonnull;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static service.importer.ModelField.*;

public class SqliteReaderOfv5_4 implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(SqliteReaderOfv5_4.class);

    @Override
    public List<ModelHolder> read(@Nonnull File file, @Nonnull Model ... models) {
        List<ModelHolder> result = new ArrayList<>();

        try (Connection connection = SqliteConnector.getDBConnection(file)) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                for (Model model : models) {
                    switch (model) {
                        case MEASUREMENT:
                            appendMeasurements(result, statement);
                            break;
                        case CHANNEL:
                            appendChannels(result, statement);
                            break;
                        case SENSOR :
                            appendSensors(result, statement);
                            break;
                        case CONTROL_POINTS:
                            appendControlPoints(result, statement);
                            break;
                        case DEPARTMENT:
                            appendDepartments(result, statement);
                            break;
                        case AREA:
                            appendAreas(result, statement);
                            break;
                        case PROCESS:
                            appendProcesses(result, statement);
                            break;
                        case INSTALLATION:
                            appendInstallations(result, statement);
                            break;
                        case PERSON:
                            appendPersons(result, statement);
                            break;
                        case CALIBRATOR:
                            appendCalibrators(result, statement);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown", e);
        }

        return result;
    }

    /**
     * Extract measurements values from DB and appends to list
     * @param list to extract fields of measurements.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendMeasurements(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM measurements;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.MEASUREMENT);

            modelHolder.setField(MEASUREMENT_NAME, resultSet.getString("name"));
            modelHolder.setField(MEASUREMENT_VALUE, resultSet.getString("value"));
            modelHolder.setField(MEASUREMENT_FACTORS, resultSet.getString("factors"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract channels values from DB and appends to list
     * @param list to extract fields of channels.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendChannels(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM channels;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.CHANNEL);

            modelHolder.setField(CHANNEL_CODE, resultSet.getString("code"));
            modelHolder.setField(CHANNEL_NAME, resultSet.getString("name"));
            modelHolder.setField(CHANNEL_MEASUREMENT_VALUE, resultSet.getString("measurement_value"));
            modelHolder.setField(CHANNEL_DEPARTMENT, resultSet.getString("department"));
            modelHolder.setField(CHANNEL_AREA, resultSet.getString("area"));
            modelHolder.setField(CHANNEL_PROCESS, resultSet.getString("process"));
            modelHolder.setField(CHANNEL_INSTALLATION, resultSet.getString("installation"));
            modelHolder.setField(CHANNEL_DATE, resultSet.getString("date"));
            modelHolder.setField(CHANNEL_FREQUENCY, String.valueOf(resultSet.getDouble("frequency")));
            modelHolder.setField(CHANNEL_TECHNOLOGY_NUMBER, resultSet.getString("technology_number"));
            modelHolder.setField(CHANNEL_PROTOCOL_NUMBER, resultSet.getString("protocol_number"));
            modelHolder.setField(CHANNEL_RANGE_MIN, String.valueOf(resultSet.getDouble("range_min")));
            modelHolder.setField(CHANNEL_RANGE_MAX, String.valueOf(resultSet.getDate("range_max")));
            modelHolder.setField(CHANNEL_REFERENCE, resultSet.getString("reference"));
            modelHolder.setField(CHANNEL_SUITABILITY, resultSet.getString("suitability"));
            modelHolder.setField(CHANNEL_ALLOWABLE_ERROR_PERCENT, String.valueOf(resultSet.getDouble("allowable_error_percent")));
            modelHolder.setField(CHANNEL_ALLOWABLE_ERROR_VALUE, String.valueOf(resultSet.getDouble("allowable_error_value")));

            list.add(modelHolder);
        }
    }

    /**
     * Extract sensors values from DB and appends to list
     * @param list to extract fields of sensors.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendSensors(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM sensors;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.SENSOR);

            modelHolder.setField(SENSOR_NAME, resultSet.getString("name"));
            modelHolder.setField(SENSOR_TYPE, resultSet.getString("type"));
            modelHolder.setField(SENSOR_SERIAL_NUMBER, resultSet.getString("number"));
            modelHolder.setField(SENSOR_MEASUREMENT_NAME, resultSet.getString("measurement"));
            modelHolder.setField(SENSOR_MEASUREMENT_VALUE, resultSet.getString("value"));
            modelHolder.setField(SENSOR_ERROR_FORMULA, resultSet.getString("error_formula"));
            modelHolder.setField(SENSOR_RANGE_MIN, resultSet.getString("range_min"));
            modelHolder.setField(SENSOR_RANGE_MAX, resultSet.getString("range_max"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract departments values from DB and appends to list
     * @param list to extract fields of departments.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendDepartments(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM departments;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.DEPARTMENT);

            modelHolder.setField(DEPARTMENT, resultSet.getString("department"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract areas values from DB and appends to list
     * @param list to extract fields of areas.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendAreas(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM areas;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.AREA);

            modelHolder.setField(AREA, resultSet.getString("area"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract processes values from DB and appends to list
     * @param list to extract fields of processes.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendProcesses(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM processes;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.PROCESS);

            modelHolder.setField(PROCESS, resultSet.getString("process"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract installations values from DB and appends to list
     * @param list to extract fields of installations.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendInstallations(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM installations;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.INSTALLATION);

            modelHolder.setField(INSTALLATION, resultSet.getString("installation"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract control_points values from DB and appends to list
     * @param list to extract fields of control_points.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendControlPoints(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM control_points;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.CONTROL_POINTS);

            modelHolder.setField(CONTROL_POINTS_SENSOR_TYPE, resultSet.getString("sensor_type"));
            modelHolder.setField(CONTROL_POINTS_VALUES, resultSet.getString("points"));
            modelHolder.setField(CONTROL_POINTS_RANGE_MIN, resultSet.getString("range_min"));
            modelHolder.setField(CONTROL_POINTS_RANGE_MAX, resultSet.getString("range_max"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract persons values from DB and appends to list
     * @param list to extract fields of persons.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendPersons(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM persons;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.PERSON);

            modelHolder.setField(PERSON_NAME, resultSet.getString("name"));
            modelHolder.setField(PERSON_SURNAME, resultSet.getString("surname"));
            modelHolder.setField(PERSON_PATRONYMIC, resultSet.getString("patronymic"));
            modelHolder.setField(PERSON_POSITION, resultSet.getString("position"));

            list.add(modelHolder);
        }
    }

    /**
     * Extract calibrators values from DB and appends to list
     * @param list to extract fields of calibrators.
     * @param statement to connection with DB
     * @throws SQLException see {@link Statement}
     */
    private void appendCalibrators(@Nonnull List<ModelHolder> list, @Nonnull Statement statement) throws SQLException {
        String sql = "SELECT * FROM calibrators;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            ModelHolder modelHolder = new ModelHolder(Model.CALIBRATOR);

            modelHolder.setField(CALIBRATOR_NAME, resultSet.getString("name"));
            modelHolder.setField(CALIBRATOR_TYPE, resultSet.getString("type"));
            modelHolder.setField(CALIBRATOR_NUMBER, resultSet.getString("number"));
            modelHolder.setField(CALIBRATOR_MEASUREMENT_NAME, resultSet.getString("measurement"));
            modelHolder.setField(CALIBRATOR_MEASUREMENT_VALUE, resultSet.getString("value"));
            modelHolder.setField(CALIBRATOR_ERROR_FORMULA, resultSet.getString("error_formula"));
            modelHolder.setField(CALIBRATOR_CERTIFICATE, resultSet.getString("certificate"));
            modelHolder.setField(CALIBRATOR_RANGE_MIN, String.valueOf(resultSet.getDouble("range_min")));
            modelHolder.setField(CALIBRATOR_RANGE_MAX, String.valueOf(resultSet.getDouble("range_max")));

            list.add(modelHolder);
        }
    }
}
