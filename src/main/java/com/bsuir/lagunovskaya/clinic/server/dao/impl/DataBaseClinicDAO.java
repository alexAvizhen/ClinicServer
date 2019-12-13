package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.db.DbUtils;
import com.bsuir.lagunovskaya.clinic.server.db.JDBCTemplate;
import com.bsuir.lagunovskaya.clinic.server.db.processors.PreparedStatementPostProcessor;
import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseClinicDAO implements ClinicDAO {

    private static DataBaseClinicDAO INSTANCE = null;

    private DataBaseClinicDAO() {
    }

    public static DataBaseClinicDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseClinicDAO();
        }
        return INSTANCE;
    }

    @Override
    public Clinic createClinic(final Clinic clinic) {
        Integer generatedId = DbUtils.generateNextIdForTable("clinic");
        String createQuery = "INSERT INTO `clinic` (`id`, `address`, `description`) VALUES (?, ?, ?)";
        clinic.setId(generatedId);
        new JDBCTemplate().executeUpdate(createQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, clinic.getId());
                preparedStatement.setString(2, clinic.getAddress());
                preparedStatement.setString(3, clinic.getDescription());
            }
        });
        return clinic;
    }

    @Override
    public Clinic getClinicById(Integer id) {
        String selectQuery = "SELECT * FROM clinic where id = " + id;
        return new JDBCTemplate().executeSelect(selectQuery, new ResultSetProcessor<Clinic>() {
            @Override
            public Clinic processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int clinicId = resultSet.getInt("id");
                    String address = resultSet.getString("address");
                    String description = resultSet.getString("description");
                    Clinic clinic = new Clinic(address, description);
                    clinic.setId(clinicId);
                    return clinic;
                }
                return null;
            }
        });
    }

    @Override
    public void updateClinic(final Clinic clinic) {
        if (clinic.getId() == null) {
            return;
        }
        String sqlToUpdateClinic = "UPDATE `clinic` SET `address` = ?, `description` = ? WHERE `id` = ?";
        new JDBCTemplate().executeUpdate(sqlToUpdateClinic, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, clinic.getAddress());
                preparedStatement.setString(2, clinic.getDescription());
                preparedStatement.setInt(3, clinic.getId());
            }
        });
    }
}
