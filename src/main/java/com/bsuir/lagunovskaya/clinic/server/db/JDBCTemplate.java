package com.bsuir.lagunovskaya.clinic.server.db;

import com.bsuir.lagunovskaya.clinic.server.ClinicServer;
import com.bsuir.lagunovskaya.clinic.server.db.processors.PreparedStatementPostProcessor;
import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {


    private final String jdbcDriverStr;
    private final String jdbcURL;
    private final String dbLogin;
    private final String dbPassword;

    public JDBCTemplate() {
        this.jdbcDriverStr = ClinicServer.properties.getProperty("jdbc.driver");
        this.jdbcURL = ClinicServer.properties.getProperty("jdbc.url");
        this.dbLogin = ClinicServer.properties.getProperty("db.login");
        this.dbPassword = ClinicServer.properties.getProperty("db.password");

    }

    public <T> T executeSelect(String selectQuery, ResultSetProcessor<T> resultSetProcessor) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL, dbLogin, dbPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            return resultSetProcessor.processResultSet(resultSet);
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public void executeUpdate(String updateQuery, PreparedStatementPostProcessor statementPostProcessor) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL, dbLogin, dbPassword);

            preparedStatement = connection.prepareStatement(updateQuery);
            statementPostProcessor.fillParamsOfPreparedStatement(preparedStatement);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

    public void runScript(String pathToScript) {
        Connection connection = null;
        try {
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL, dbLogin, dbPassword);
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(pathToScript));
            scriptRunner.runScript(reader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
