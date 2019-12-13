package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.server.db.DbUtils;
import com.bsuir.lagunovskaya.clinic.server.db.JDBCTemplate;
import com.bsuir.lagunovskaya.clinic.server.db.processors.PreparedStatementPostProcessor;
import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DateBasePatientDAO implements PatientDAO {

    private static DateBasePatientDAO INSTANCE = null;

    private DateBasePatientDAO() {
    }

    public static DateBasePatientDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DateBasePatientDAO();
        }
        return INSTANCE;
    }
    @Override
    public Patient getPatientById(Integer id) {
        String selectPatientInfoQuery = "SELECT * FROM patient WHERE id = " + id;
        return new JDBCTemplate().executeSelect(selectPatientInfoQuery, new ResultSetProcessor<Patient>() {
            @Override
            public Patient processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int patientId = resultSet.getInt("id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("login");
                    String surname = resultSet.getString("surname");
                    String name = resultSet.getString("name");
                    String phoneNumber = resultSet.getString("phone_number");
                    Date birthDate = resultSet.getDate("birth_date");
                    String address = resultSet.getString("address");
                    int departmentId = resultSet.getInt("department_id");
                    Patient patient = new Patient(login, password, departmentId);
                    patient.setId(patientId);
                    patient.setSurname(surname);
                    patient.setName(name);
                    patient.setPhoneNumber(phoneNumber);
                    patient.setAddress(address);
                    patient.setBirthDate(birthDate);
                    return patient;
                }

                return null;
            }
        });
    }

    @Override
    public Patient getPatientByLogin(String login) {
        String selectPatientIdByLogin = "SELECT id FROM patient WHERE login = '" + login + "'";
        Integer patientIdByLogin = new JDBCTemplate().executeSelect(selectPatientIdByLogin, new ResultSetProcessor<Integer>() {
            @Override
            public Integer processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
                return null;
            }
        });
        if (patientIdByLogin != null) {
            return getPatientById(patientIdByLogin);
        } else {
            return null;
        }
    }

    @Override
    public Patient createPatient(final Patient patient) {
        Integer nextPatientId = DbUtils.generateNextIdForTable("patient");
        patient.setId(nextPatientId);
        String insertPatientInfoQuery = "INSERT INTO `patient` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `address`, `department_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        new JDBCTemplate().executeUpdate(insertPatientInfoQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, patient.getId());
                preparedStatement.setString(2, patient.getLogin());
                preparedStatement.setString(3, patient.getPassword());
                preparedStatement.setString(4, patient.getSurname());
                preparedStatement.setString(5, patient.getName());
                preparedStatement.setString(6, patient.getPhoneNumber());
                Date birthDate = patient.getBirthDate() == null ? new Date() : patient.getBirthDate();
                preparedStatement.setDate(7, new java.sql.Date(birthDate.getTime()));
                preparedStatement.setString(8, patient.getAddress());
                preparedStatement.setInt(9, patient.getClinicDepartmentId());
            }
        });
        return patient;
    }

    @Override
    public void updatePatient(final Patient patient) {
        String updatePatientInfoQuery = "UPDATE `patient` SET `login` = ?, `password` = ?, `surname` = ?, `name` = ?, `phone_number` = ?, `birth_date` = ?, `address` = ?, `department_id` = ? " +
                "WHERE id = ?";
        new JDBCTemplate().executeUpdate(updatePatientInfoQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, patient.getLogin());
                preparedStatement.setString(2, patient.getPassword());
                preparedStatement.setString(3, patient.getSurname());
                preparedStatement.setString(4, patient.getName());
                preparedStatement.setString(5, patient.getPhoneNumber());
                Date birthDate = patient.getBirthDate() == null ? new Date() : patient.getBirthDate();
                preparedStatement.setDate(6, new java.sql.Date(birthDate.getTime()));
                preparedStatement.setString(7, patient.getAddress());
                preparedStatement.setInt(8, patient.getClinicDepartmentId());
                preparedStatement.setInt(9, patient.getId());
            }
        });
    }

    @Override
    public void deletePatientById(final Integer id) {
        String deletePatientByIdQuery = "DELETE FROM `patient` WHERE `id` = ?";
        new JDBCTemplate().executeUpdate(deletePatientByIdQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        });
    }

    @Override
    public Collection<Patient> getAllPatients() {
        String selectAllPatientIdsQuery = "SELECT id FROM patient";
        List<Integer> allPatientIds = new JDBCTemplate().executeSelect(selectAllPatientIdsQuery, new ResultSetProcessor<List<Integer>>() {
            @Override
            public List<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                List<Integer> allPatientIdsInsideSql = new ArrayList<>();
                while (resultSet.next()) {
                    allPatientIdsInsideSql.add(resultSet.getInt("id"));
                }

                return allPatientIdsInsideSql;
            }
        });
        List<Patient> allPatients = new ArrayList<>();
        for (Integer patientId : allPatientIds) {
            allPatients.add(getPatientById(patientId));
        }
        return allPatients;
    }
}
