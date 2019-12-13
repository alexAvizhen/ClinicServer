package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
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

public class DateBaseDoctorDAO implements DoctorDAO {

    private static DateBaseDoctorDAO INSTANCE = null;

    private DateBaseDoctorDAO() {
    }

    public static DateBaseDoctorDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DateBaseDoctorDAO();
        }
        return INSTANCE;
    }

    @Override
    public Doctor getDoctorById(Integer id) {
        String selectDoctorInfoQuery = "SELECT * FROM doctor WHERE id = " + id;
        return new JDBCTemplate().executeSelect(selectDoctorInfoQuery, new ResultSetProcessor<Doctor>() {
            @Override
            public Doctor processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int doctorId = resultSet.getInt("id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("login");
                    String surname = resultSet.getString("surname");
                    String name = resultSet.getString("name");
                    String phoneNumber = resultSet.getString("phone_number");
                    Date birthDate = resultSet.getDate("birth_date");
                    int departmentId = resultSet.getInt("department_id");
                    Doctor doctor = new Doctor(login, password, departmentId);
                    doctor.setId(doctorId);
                    doctor.setSurname(surname);
                    doctor.setName(name);
                    doctor.setPhoneNumber(phoneNumber);
                    doctor.setBirthDate(birthDate);
                    return doctor;
                }

                return null;
            }
        });
    }

    @Override
    public Doctor getDoctorByLogin(String login) {
        String selectDoctorIdByLogin = "SELECT id FROM doctor WHERE login = '" + login + "'";
        Integer doctorIdByLogin = new JDBCTemplate().executeSelect(selectDoctorIdByLogin, new ResultSetProcessor<Integer>() {
            @Override
            public Integer processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
                return null;
            }
        });
        if (doctorIdByLogin != null) {
            return getDoctorById(doctorIdByLogin);
        } else {
            return null;
        }
    }
    @Override
    public Doctor createDoctor(final Doctor doctor) {
        Integer nextDoctorId = DbUtils.generateNextIdForTable("doctor");
        doctor.setId(nextDoctorId);
        String insertDoctorInfoQuery = "INSERT INTO `doctor` (`id`, `login`, `password`, `surname`, `name`, `phone_number`, `birth_date`, `department_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        new JDBCTemplate().executeUpdate(insertDoctorInfoQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, doctor.getId());
                preparedStatement.setString(2, doctor.getLogin());
                preparedStatement.setString(3, doctor.getPassword());
                preparedStatement.setString(4, doctor.getSurname());
                preparedStatement.setString(5, doctor.getName());
                preparedStatement.setString(6, doctor.getPhoneNumber());
                Date birthDate = doctor.getBirthDate() == null ? new Date() : doctor.getBirthDate();
                preparedStatement.setDate(7, new java.sql.Date(birthDate.getTime()));
                preparedStatement.setInt(8, doctor.getClinicDepartmentId());
            }
        });
        return doctor;
    }

    @Override
    public void updateDoctor(final Doctor doctor) {
        String updateDoctorInfoQuery = "UPDATE `doctor` SET `login` = ?, `password` = ?, `surname` = ?, `name` = ?, `phone_number` = ?, `birth_date` = ?, `department_id` = ? " +
                "WHERE id = ?";
        new JDBCTemplate().executeUpdate(updateDoctorInfoQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, doctor.getLogin());
                preparedStatement.setString(2, doctor.getPassword());
                preparedStatement.setString(3, doctor.getSurname());
                preparedStatement.setString(4, doctor.getName());
                preparedStatement.setString(5, doctor.getPhoneNumber());
                Date birthDate = doctor.getBirthDate() == null ? new Date() : doctor.getBirthDate();
                preparedStatement.setDate(6, new java.sql.Date(birthDate.getTime()));
                preparedStatement.setInt(7, doctor.getClinicDepartmentId());
                preparedStatement.setInt(8, doctor.getId());
            }
        });
    }

    @Override
    public void deleteDoctorById(final Integer id) {
        String deleteDoctorByIdQuery = "DELETE FROM `doctor` WHERE `id` = ?";
        new JDBCTemplate().executeUpdate(deleteDoctorByIdQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        });
    }

    @Override
    public Collection<Doctor> getAllDoctors() {
        String selectAllDoctorIdsQuery = "SELECT id FROM doctor";
        List<Integer> allDoctorIds = new JDBCTemplate().executeSelect(selectAllDoctorIdsQuery, new ResultSetProcessor<List<Integer>>() {
            @Override
            public List<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                List<Integer> allDoctorIdsInsideSql = new ArrayList<>();
                while (resultSet.next()) {
                    allDoctorIdsInsideSql.add(resultSet.getInt("id"));
                }

                return allDoctorIdsInsideSql;
            }
        });
        List<Doctor> allDoctors = new ArrayList<>();
        for (Integer doctorId : allDoctorIds) {
            allDoctors.add(getDoctorById(doctorId));
        }
        return allDoctors;
    }
}
