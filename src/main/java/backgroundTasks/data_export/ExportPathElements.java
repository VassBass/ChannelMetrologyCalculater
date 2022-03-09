package backgroundTasks.data_export;

import application.Application;
import org.sqlite.JDBC;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportPathElements extends SwingWorker<Void, Void> {
    private static final Logger LOGGER = Logger.getLogger(ExportPathElements.class.getName());

    private static final String EXPORT_SUCCESS = "Дані вдало експортовані";
    private static final String EXPORT = "Експорт";

    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(){
        Calendar date = Calendar.getInstance();
        return "export_path_elements ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].db";
    }

    private void exportDepartments(ArrayList<String>departments){
        String dbUrl = "jdbc:sqlite:Support/Export/" + this.fileName();
        String sql = "CREATE TABLE IF NOT EXISTS departments ("
                + "department text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"department\")"
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
            sql = "INSERT INTO departments ('department') "
                    + "VALUES(?);";
            preparedStatement = connection.prepareStatement(sql);
            for (String department : departments) {
                preparedStatement.setString(1, department);
                preparedStatement.execute();
            }

            LOGGER.fine("Close connection");
            statement.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
            try {
                if (statement != null) statement.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
    }

    private void exportAreas(ArrayList<String>areas){
        String dbUrl = "jdbc:sqlite:Support/Export/" + this.fileName();
        String sql = "CREATE TABLE IF NOT EXISTS areas ("
                + "area text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"area\")"
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
            sql = "INSERT INTO areas ('area') "
                    + "VALUES(?);";
            preparedStatement = connection.prepareStatement(sql);
            for (String area : areas) {
                preparedStatement.setString(1, area);
                preparedStatement.execute();
            }

            LOGGER.fine("Close connection");
            statement.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
            try {
                if (statement != null) statement.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
    }

    private void exportProcesses(ArrayList<String>processes){
        String dbUrl = "jdbc:sqlite:Support/Export/" + this.fileName();
        String sql = "CREATE TABLE IF NOT EXISTS processes ("
                + "process text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"process\")"
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
            sql = "INSERT INTO processes ('process') "
                    + "VALUES(?);";
            preparedStatement = connection.prepareStatement(sql);
            for (String process : processes) {
                preparedStatement.setString(1, process);
                preparedStatement.execute();
            }

            LOGGER.fine("Close connection");
            statement.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
            try {
                if (statement != null) statement.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
    }

    private void exportInstallations(ArrayList<String>installations){
        String dbUrl = "jdbc:sqlite:Support/Export/" + this.fileName();
        String sql = "CREATE TABLE IF NOT EXISTS installations ("
                + "installation text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"installation\")"
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
            sql = "INSERT INTO installations ('installation') "
                    + "VALUES(?);";
            preparedStatement = connection.prepareStatement(sql);
            for (String installation : installations) {
                preparedStatement.setString(1, installation);
                preparedStatement.execute();
            }

            LOGGER.fine("Close connection");
            statement.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
            try {
                if (statement != null) statement.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
    }

    public ExportPathElements(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.exportDepartments(Application.context.departmentService.getAll());
        this.exportAreas(Application.context.areaService.getAll());
        this.exportProcesses(Application.context.processService.getAll());
        this.exportInstallations(Application.context.installationService.getAll());
        return null;
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        JOptionPane.showMessageDialog(this.mainScreen, EXPORT_SUCCESS, EXPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}