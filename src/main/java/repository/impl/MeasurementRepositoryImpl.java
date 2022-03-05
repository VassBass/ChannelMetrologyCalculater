package repository.impl;

import measurements.Measurement;
import repository.MeasurementRepository;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeasurementRepositoryImpl extends Repository implements MeasurementRepository {
    private static final Logger LOGGER = Logger.getLogger(MeasurementRepository.class.getName());

    public MeasurementRepositoryImpl(){super();}

    @Override
    protected void init(){
        String sql = "CREATE TABLE IF NOT EXISTS measurements ("
                + "id integer NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", value text NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
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
    public ArrayList<Measurement> getAll() {
        ArrayList<Measurement>measurements = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM measurements";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String name = resultSet.getString("name");
                String value = resultSet.getString("value");
                int id = resultSet.getInt("id");
                Measurement measurement = new Measurement(name, value);
                measurement.setId(id);
                measurements.add(measurement);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return measurements;
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Measurement> measurements) {
        String sql = measurements == null ? "DELETE FROM measurements;" : null;

        LOGGER.fine("Get connection with DB");
        if (sql != null){
            try (Connection connection = getConnection()) {
                Statement statement = connection.createStatement();

                LOGGER.fine("Send request");
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }else {
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                sql = "DELETE FROM measurements;";
                statementClear.execute(sql);

                if (!measurements.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO measurements ('name', 'value') VALUES (?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (Measurement measurement : measurements) {
                        statement.setString(1, measurement.getName());
                        statement.setString(2, measurement.getValue());
                        statement.execute();
                    }

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}