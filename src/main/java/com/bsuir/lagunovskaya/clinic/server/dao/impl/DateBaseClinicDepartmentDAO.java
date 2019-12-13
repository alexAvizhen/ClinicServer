package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.db.DbUtils;
import com.bsuir.lagunovskaya.clinic.server.db.JDBCTemplate;
import com.bsuir.lagunovskaya.clinic.server.db.processors.PreparedStatementPostProcessor;
import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DateBaseClinicDepartmentDAO implements ClinicDepartmentDAO {

    private static DateBaseClinicDepartmentDAO INSTANCE = null;

    private DateBaseClinicDepartmentDAO() {
    }

    public static DateBaseClinicDepartmentDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DateBaseClinicDepartmentDAO();
        }
        return INSTANCE;
    }

    @Override
    public ClinicDepartment createClinicDepartment(final ClinicDepartment clinicDepartment) {
        Integer nextDepartmentId = DbUtils.generateNextIdForTable("clinic_department");
        clinicDepartment.setId(nextDepartmentId);
        String queryToCreateDepartment = "INSERT INTO `clinic_department` (`id`, `name`, `clinic_id`) VALUES (?, ?, ?)";
        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        jdbcTemplate.executeUpdate(queryToCreateDepartment, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, clinicDepartment.getId());
                preparedStatement.setString(2, clinicDepartment.getName());
                preparedStatement.setInt(3, clinicDepartment.getClinicId());

            }
        });
        insertDepartmentStreets(clinicDepartment, jdbcTemplate);
        return clinicDepartment;
    }

    private void insertDepartmentStreets(final ClinicDepartment clinicDepartment, JDBCTemplate jdbcTemplate) {
        String queryToSetDepartmentStreet = "INSERT INTO `department_street` (`department_id`, `street`) VALUES (?, ?)";
        for (final String street : clinicDepartment.getStreets()) {
            jdbcTemplate.executeUpdate(queryToSetDepartmentStreet, new PreparedStatementPostProcessor() {
                @Override
                public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setInt(1, clinicDepartment.getId());
                    preparedStatement.setString(2, street);
                }
            });
        }
    }

    @Override
    public void updateClinicDepartment(final ClinicDepartment clinicDepartment) {
        if (clinicDepartment.getId() == null) {
            return;
        }
        String deleteStreetsQuery = "DELETE FROM `department_street` WHERE `department_id` = ?";
        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        jdbcTemplate.executeUpdate(deleteStreetsQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, clinicDepartment.getId());
            }
        });
        String updateDepartmentQuery = "UPDATE `clinic_department` SET `name` = ?, `clinic_id` = ? WHERE `id` = ?";
        jdbcTemplate.executeUpdate(updateDepartmentQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, clinicDepartment.getName());
                preparedStatement.setInt(2, clinicDepartment.getClinicId());
                preparedStatement.setInt(3, clinicDepartment.getId());
            }
        });
        insertDepartmentStreets(clinicDepartment, jdbcTemplate);
    }

    @Override
    public ClinicDepartment getClinicDepartmentById(Integer id) {
        String selectClinicDepartmentInfoQuery = "SELECT id, name, clinic_id FROM clinic_department where id = " + id;
        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        ClinicDepartment clinicDepartment = jdbcTemplate.executeSelect(selectClinicDepartmentInfoQuery, new ResultSetProcessor<ClinicDepartment>() {
            @Override
            public ClinicDepartment processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int departmentId = resultSet.getInt("id");
                    String departmentName = resultSet.getString("name");
                    int departmentClinicId = resultSet.getInt("clinic_id");

                    ClinicDepartment clinicDepartment = new ClinicDepartment(departmentClinicId, departmentName, new ArrayList<String>());
                    clinicDepartment.setId(departmentId);
                    return clinicDepartment;
                }
                return null;
            }
        });
        String getDepartmentStreetsQuery = "SELECT street FROM department_street where department_id = " + clinicDepartment.getId();
        List<String> departmentStreets = jdbcTemplate.executeSelect(getDepartmentStreetsQuery, new ResultSetProcessor<List<String>>() {
            @Override
            public List<String> processResultSet(ResultSet resultSet) throws SQLException {
                List<String> departmentStreets = new ArrayList<>();
                while (resultSet.next()) {
                    departmentStreets.add(resultSet.getString("street"));
                }

                return departmentStreets;
            }
        });
        clinicDepartment.setStreets(departmentStreets);
        String getDepartmentDoctorsQuery = "SELECT id FROM doctor where department_id = " + clinicDepartment.getId();
        List<Integer> clinicDepartmentDoctorIds = jdbcTemplate.executeSelect(getDepartmentDoctorsQuery, new ResultSetProcessor<List<Integer>>() {
            @Override
            public List<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                List<Integer> doctorIds = new ArrayList<>();
                while (resultSet.next()) {
                    doctorIds.add(resultSet.getInt("id"));
                }
                return doctorIds;
            }
        });
        clinicDepartment.setDoctorIds(clinicDepartmentDoctorIds);

        String getDepartmentPatients = "SELECT id FROM patient where department_id = " + clinicDepartment.getId();
        List<Integer> clinicDepartmentPatientIds = jdbcTemplate.executeSelect(getDepartmentPatients, new ResultSetProcessor<List<Integer>>() {
            @Override
            public List<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                List<Integer> patientIds = new ArrayList<>();
                while (resultSet.next()) {
                    patientIds.add(resultSet.getInt("id"));
                }
                return patientIds;
            }
        });
        clinicDepartment.setPatientIds(clinicDepartmentPatientIds);
        return clinicDepartment;
    }

    @Override
    public ClinicDepartment getClinicDepartmentByName(String name) {
        String selectDepartmentIdByNameQuery = "SELECT id from clinic_department where name = '" + name + "'";
        Integer departmentIdByName = new JDBCTemplate().executeSelect(selectDepartmentIdByNameQuery, new ResultSetProcessor<Integer>() {
            @Override
            public Integer processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
                return null;
            }
        });
        if (departmentIdByName != null) {
            return getClinicDepartmentById(departmentIdByName);
        }
        return null;
    }

    @Override
    public Collection<ClinicDepartment> getAllClinicDepartments() {
        String selectAllDepartmentIds = "SELECT id from clinic_department";
        Collection<Integer> allDepartmentIds = new JDBCTemplate().executeSelect(selectAllDepartmentIds, new ResultSetProcessor<Collection<Integer>>() {
            @Override
            public Collection<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                Collection<Integer> allDepartmentIdsInSql = new ArrayList<>();
                while (resultSet.next()) {
                    allDepartmentIdsInSql.add(resultSet.getInt("id"));
                }
                return allDepartmentIdsInSql;
            }
        });
        List<ClinicDepartment> allDepartments = new ArrayList<>();
        for (Integer departmentId : allDepartmentIds) {
            allDepartments.add(getClinicDepartmentById(departmentId));
        }
        return allDepartments;
    }
}
