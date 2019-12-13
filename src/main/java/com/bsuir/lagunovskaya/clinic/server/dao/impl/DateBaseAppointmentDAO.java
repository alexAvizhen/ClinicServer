package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;
import com.bsuir.lagunovskaya.clinic.server.dao.AppointmentDAO;
import com.bsuir.lagunovskaya.clinic.server.db.DbUtils;
import com.bsuir.lagunovskaya.clinic.server.db.JDBCTemplate;
import com.bsuir.lagunovskaya.clinic.server.db.processors.PreparedStatementPostProcessor;
import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DateBaseAppointmentDAO implements AppointmentDAO {

    private static DateBaseAppointmentDAO INSTANCE = null;

    private DateBaseAppointmentDAO() {
    }

    public static DateBaseAppointmentDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DateBaseAppointmentDAO();
        }
        return INSTANCE;
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        String selectAppointmentInfoByIdQuery = "SELECT * FROM appointment where id = " + id;
        return new JDBCTemplate().executeSelect(selectAppointmentInfoByIdQuery, new ResultSetProcessor<Appointment>() {
            @Override
            public Appointment processResultSet(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int appointmentId = resultSet.getInt("id");
                    Date appointmentDate = new Date(resultSet.getTimestamp("appointment_date").getTime());
                    String commentToAppointment = resultSet.getString("comment_to_appointment");
                    int doctorId = resultSet.getInt("doctor_id");
                    int patientId = resultSet.getInt("patient_id");
                    Appointment appointment = new Appointment(doctorId, patientId, appointmentDate, commentToAppointment);
                    appointment.setId(appointmentId);
                    return appointment;
                }

                return null;
            }
        });
    }

    @Override
    public Appointment createAppointment(final Appointment appointment) {
        Integer nextAppointmentId = DbUtils.generateNextIdForTable("appointment");
        appointment.setId(nextAppointmentId);
        String createAppointmentQuery = "INSERT INTO `appointment` (`id`, `appointment_date`, `comment_to_appointment`, `doctor_id`, `patient_id`) VALUES (?, ?, ?, ?, ?)";
        new JDBCTemplate().executeUpdate(createAppointmentQuery, new PreparedStatementPostProcessor() {
            @Override
            public void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, appointment.getId());
                preparedStatement.setTimestamp(2, new Timestamp(appointment.getAppointmentDate().getTime()));
                preparedStatement.setString(3, appointment.getCommentToAppointment());
                preparedStatement.setInt(4, appointment.getDoctorId());
                preparedStatement.setInt(5, appointment.getPatientId());
            }
        });
        return appointment;
    }

    @Override
    public Collection<Appointment> getAllAppointments() {
        String selectAllAppointmentsQuery = "SELECT id FROM appointment";

        List<Integer> allAppointmentIds = new JDBCTemplate().executeSelect(selectAllAppointmentsQuery, new ResultSetProcessor<List<Integer>>() {
            @Override
            public List<Integer> processResultSet(ResultSet resultSet) throws SQLException {
                List<Integer> allAppointmentIdsInsideSql = new ArrayList<>();
                while (resultSet.next()) {
                    allAppointmentIdsInsideSql.add(resultSet.getInt("id"));
                }

                return allAppointmentIdsInsideSql;
            }
        });
        List<Appointment> allAppointments = new ArrayList<>();
        for (Integer appointmentId : allAppointmentIds) {
            allAppointments.add(getAppointmentById(appointmentId));
        }
        return allAppointments;
    }
}
